package app.funfinder.roomservice.infrastructure.elasticsearch.repository;

import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.ESRoomInformation;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.ESRoomSearchRequest;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ESRoomRepository {

    @Autowired
    ElasticsearchOperations elasticsearchRestTemplate;

    public List<ESRoomInformation> findBy(ESRoomSearchRequest searchRequest) {

        //FIXME study elasticsearch to implement correct criteria!!!
        BoolQueryBuilder query = new BoolQueryBuilder();

        if (searchRequest.getLatitude() != 0 && searchRequest.getLongitude() != 0 && searchRequest.getDistanceAvailability() != 0) {
            GeoDistanceQueryBuilder geoDistanceFilter = QueryBuilders.geoDistanceQuery("location")
                    .point(searchRequest.getLatitude(), searchRequest.getLongitude()).distance(searchRequest.getDistanceAvailability(), DistanceUnit.METERS);
            query.should(geoDistanceFilter);
        }

        if (searchRequest.getHashtags() != null && !searchRequest.getHashtags().isEmpty()) {
            TermsQueryBuilder hashtagsFilter = QueryBuilders.termsQuery("hashtags", searchRequest.getHashtags());
            query.should(hashtagsFilter);
        }

        if (searchRequest.getLanguages() != null && !searchRequest.getLanguages().isEmpty()) {
            TermsQueryBuilder languagesFilter = QueryBuilders.termsQuery("languages", searchRequest.getLanguages());
            query.should(languagesFilter);
        }


        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
//                .withPageable(PageRequest.of(page, size))
                .build();

        SearchHits<ESRoomInformation> hits = elasticsearchRestTemplate.search(nativeSearchQuery, ESRoomInformation.class);
        return hits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public void save(ESRoomInformation roomInformation) {
        elasticsearchRestTemplate.save(roomInformation);
    }
}
