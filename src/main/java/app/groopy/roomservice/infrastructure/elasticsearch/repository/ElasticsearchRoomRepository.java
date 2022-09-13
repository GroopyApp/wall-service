package app.groopy.roomservice.infrastructure.elasticsearch.repository;

import app.groopy.roomservice.application.mapper.ApplicationMapper;
import app.groopy.roomservice.domain.exceptions.RoomNotFoundException;
import app.groopy.roomservice.domain.exceptions.UserNotFoundException;
import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.domain.models.common.SearchScope;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.models.entities.ESRoomEntity;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESRoomSearchRequest;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.models.entities.ESUserEntity;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static app.groopy.roomservice.domain.models.common.RoomStatus.*;

@Repository
public class ElasticsearchRoomRepository {

    private static final Integer DEFAULT_SEARCH_RANGE_IN_METERS = 10000;
    @Autowired
    private ESRoomRepository esRoomRepository;  //FIXME this is used just for save for indexing, find a way to remove it

    @Autowired
    private ESUserRepository esUserRepository;  //FIXME this is used just for save for indexing, find a way to remove it
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ApplicationMapper mapper;


    public RoomDetails save(ESRoomEntity room) {
        return mapper.map(esRoomRepository.save(room));
    }

    public List<RoomDetails> findBySearchRequest(ESRoomSearchRequest request, SearchScope searchScope) {

        Integer distance = DEFAULT_SEARCH_RANGE_IN_METERS;

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        switch (searchScope) {
            case STANDARD_SEARCH:
                distance = request.getPoint().getDistanceAvailability();
                request.getHashtags().forEach(v -> queryBuilder.must(QueryBuilders.matchQuery("hashtags", v)));
                break;
            case LOOKING_FOR_SIMILAR:
                request.getHashtags().forEach(v -> queryBuilder.should(QueryBuilders.matchQuery("hashtags", v)));
                break;
        }

        request.getLanguages().forEach(v -> queryBuilder.must(QueryBuilders.matchQuery("languages", v)));

        GeoDistanceQueryBuilder geoBuilder = QueryBuilders.geoDistanceQuery("location");
        geoBuilder.point(new GeoPoint(request.getPoint().getLatitude(), request.getPoint().getLongitude()));

        geoBuilder.distance(distance, DistanceUnit.METERS);
        queryBuilder.must(geoBuilder);

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        List<ESRoomEntity> entities = elasticsearchOperations.search(query, ESRoomEntity.class).stream().map(SearchHit::getContent).collect(Collectors.toList());
        Stream<RoomDetails> stream = entities.stream().map(entity -> mapper.map(entity));

        //TODO find a better way with ES query to fetch values with at least n elements in common
        if (!entities.isEmpty()) {
            if (searchScope.equals(SearchScope.LOOKING_FOR_SIMILAR)) {
                stream = stream.filter(room -> {
                    Set<String> result = request.getHashtags().stream()
                            .distinct()
                            .filter(room.getHashtags()::contains)
                            .collect(Collectors.toSet());
                    return result.size() >= 3;
                });
            }
        }
        return stream.collect(Collectors.toList());
    }

    public List<RoomDetails> findByUserId(String userId) throws UserNotFoundException {

        ESUserEntity user = esUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        //TODO find a better way to manage this case
        List<ESRoomEntity> rooms = user.getSubscribedRooms() != null ? esRoomRepository.getESRoomEntitiesByRoomIdInAndStatusIn(user.getSubscribedRooms(),
                List.of(
                        PENDING,
                        CREATED
                )) : new ArrayList<>();

        return rooms.stream().map(entity -> mapper.map(entity)).collect(Collectors.toList());
    }

    public RoomDetails subscribeUserToRoom(String userId, String roomId) throws UserNotFoundException, RoomNotFoundException {
        ESRoomEntity room = esRoomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        esUserRepository.findAll();
        ESUserEntity user = esUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (user.getSubscribedRooms() == null) {
            user.setSubscribedRooms(new ArrayList<>());
        }
        user.getSubscribedRooms().add(roomId);
        esUserRepository.save(user);
        return mapper.map(room);
    }

    public RoomDetails findByRoomName(String roomName) {
        ESRoomEntity room = esRoomRepository.findByRoomName(roomName).orElse(null);
        //FIXME findByRoomName apparently doesn't work, find a way to match exactly the word and remove && room.getRoomName().equals(roomName) from code below
        return room != null && room.getRoomName().equals(roomName) ? mapper.map(room) : null;
    }
}
