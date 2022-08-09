package app.funfinder.roomservice.infrastructure.elasticsearch.repository;

import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.ESRoomInformation;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.ESRoomSearchRequest;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
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
public class RoomRepository {

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    public List<ESRoomInformation> findBySearchRequest(ESRoomSearchRequest request) {

        QueryBuilder geoQuery = QueryBuilders.geoDistanceQuery("location").distance(request.getDistanceAvailability(), DistanceUnit.METERS).point(new GeoPoint(request.getLatitude(), request.getLongitude()));

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(geoQuery)
                .build();

        return elasticsearchTemplate.search(query, ESRoomInformation.class).stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
}