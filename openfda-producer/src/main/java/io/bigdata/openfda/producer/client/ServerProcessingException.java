package io.bigdata.openfda.producer.client;

import org.springframework.http.HttpStatus;

public class ServerProcessingException extends RuntimeException {
    public ServerProcessingException(final HttpStatus httpStatus) {
        super (String.format("Request not processed by FDA API. Response status code: %s", httpStatus.value()));
    }
}
