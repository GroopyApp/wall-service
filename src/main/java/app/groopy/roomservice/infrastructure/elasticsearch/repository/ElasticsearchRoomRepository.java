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
import java.util.stream.Collectors;

import static app.groopy.roomservice.domain.models.common.RoomStatus.*;
import static app.groopy.roomservice.utils.CoordsUtils.DEFAULT_SEARCH_RANGE_IN_METERS;

@Repository
public class ElasticsearchRoomRepository {

    @Autowired
    private ESRoomRepository esRoomRepository;  //FIXME this is used just for save for indexing, find a way to remove it

    @Autowired
    private ESUserRepository esUserRepository;  //FIXME this is used just for save for indexing, find a way to remove it
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ApplicationMapper mapper;


    public void save(ESRoomEntity room) {
        esRoomRepository.save(room);
    }

    public List<RoomDetails> findBySearchRequest(ESRoomSearchRequest request, SearchScope searchScope) {

        Integer distance = null;

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        switch (searchScope) {
            case STANDARD_SEARCH:
                distance = request.getPoint().getDistanceAvailability();
                request.getHashtags().forEach(v -> queryBuilder.must(QueryBuilders.matchQuery("hashtags", v)));
                break;
            case LOOKING_FOR_SIMILAR:
                distance = DEFAULT_SEARCH_RANGE_IN_METERS;
                request.getHashtags().forEach(v -> queryBuilder.should(QueryBuilders.matchQuery("hashtags", v)).minimumShouldMatch(2));
                break;
        }

        GeoDistanceQueryBuilder geoBuilder = QueryBuilders.geoDistanceQuery("location");
        geoBuilder.point(new GeoPoint(request.getPoint().getLatitude(), request.getPoint().getLongitude()));

        if (distance > 0) {
         geoBuilder.distance(distance, DistanceUnit.METERS);
        }

        queryBuilder.must(geoBuilder);

        request.getLanguages().forEach(v -> queryBuilder.should(QueryBuilders.matchQuery("languages", v)).minimumShouldMatch(1));

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        List<ESRoomEntity> entities = elasticsearchOperations.search(query, ESRoomEntity.class).stream().map(SearchHit::getContent).collect(Collectors.toList());

        return entities.stream().map(entity -> mapper.map(entity)).collect(Collectors.toList());
    }

    public List<RoomDetails> findByUserId(String userId) throws UserNotFoundException {

        ESUserEntity user = esUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        List<ESRoomEntity> rooms = esRoomRepository.getESRoomEntitiesByRoomIdInAndStatusIn(user.getSubscribedRooms(),
                List.of(
                        PENDING,
                        CREATED
                ));

        return rooms.stream().map(entity -> mapper.map(entity)).collect(Collectors.toList());
    }

    public void subscribeUserToRoom(String userId, String roomId) throws UserNotFoundException, RoomNotFoundException {
        esRoomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        esUserRepository.findAll();
        ESUserEntity user = esUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (user.getSubscribedRooms() == null) {
            user.setSubscribedRooms(new ArrayList<>());
        }
        user.getSubscribedRooms().add(roomId);
        esUserRepository.save(user);
    }
}
