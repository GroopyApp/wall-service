package app.groopy.roomservice.infrastructure.exceptions;

import app.groopy.providers.elasticsearch.exceptions.ElasticsearchOperationError;

public class ElasticsearchServiceException extends Throwable {

    ElasticsearchOperationError causeError;

    public ElasticsearchServiceException(String description, ElasticsearchOperationError cause) {
        super(String.format("Elasticsearch error -> %s. Root cause: %s", description, cause));
        this.causeError = cause;
    }
}
