package app.funfinder.roomservice.infrastructure.elasticsearch.repository;

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

@Repository
public class ElasticsearchRoomRepository {

    @Autowired
    private ESRoomRepository esRoomRepository;  //FIXME this is used just for save for indexing, find a way to remove it
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    public void save(ESRoomEntity room) {
        esRoomRepository.save(room);
    }

    public List<ESRoomEntity> findBySearchRequest(ESRoomSearchRequest request) {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (request.getPoint() != null && isValidDistance(request.getPoint())) {
            QueryBuilder geoQuery = QueryBuilders.geoDistanceQuery("location")
                    .distance(request.getPoint().getDistanceAvailability(), DistanceUnit.METERS)
                    .point(new GeoPoint(request.getPoint().getLatitude(), request.getPoint().getLongitude()));
            queryBuilder.must(geoQuery);
        }

        if (request.getHashtags() != null && !request.getHashtags().isEmpty()) {
            request.getHashtags().forEach(v -> queryBuilder.must(QueryBuilders.matchQuery("hashtags", v)));
        }

        if (request.getLanguages() != null && !request.getLanguages().isEmpty()) {
            request.getLanguages().forEach(v -> queryBuilder.should(QueryBuilders.matchQuery("languages", v)).minimumShouldMatch(1));
        }

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        return elasticsearchOperations.search(query, ESRoomEntity.class).stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    private boolean isValidDistance(ESPoint point) {
        return point.getLatitude() != null
                && point.getLongitude() != null
                && point.getDistanceAvailability() != null
                && point.getDistanceAvailability() > 0;
    }
}
