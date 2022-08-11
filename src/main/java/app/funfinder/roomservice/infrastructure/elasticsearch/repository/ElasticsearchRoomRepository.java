package app.funfinder.roomservice.infrastructure.elasticsearch.repository;

import app.funfinder.roomservice.application.mapper.ApplicationMapper;
import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.domain.models.common.SearchScope;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESPoint;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.entities.ESRoomEntity;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESRoomSearchRequest;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static app.funfinder.roomservice.utils.CoordsUtils.DEFAULT_SEARCH_RANGE_IN_METERS;
import static app.funfinder.roomservice.utils.CoordsUtils.getGeoPointFormString;

@Repository
public class ElasticsearchRoomRepository {

    @Autowired
    private ESRoomRepository esRoomRepository;  //FIXME this is used just for save for indexing, find a way to remove it
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

        QueryBuilder geoQuery = QueryBuilders.geoDistanceQuery("location")
                .distance(distance, DistanceUnit.METERS)
                .point(new GeoPoint(request.getPoint().getLatitude(), request.getPoint().getLongitude()));
        queryBuilder.must(geoQuery);

        request.getLanguages().forEach(v -> queryBuilder.should(QueryBuilders.matchQuery("languages", v)).minimumShouldMatch(1));

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        List<ESRoomEntity> entities = elasticsearchOperations.search(query, ESRoomEntity.class).stream().map(SearchHit::getContent).collect(Collectors.toList());

        return entities.stream().map(entity -> mapper.map(entity)).collect(Collectors.toList());
    }
}
