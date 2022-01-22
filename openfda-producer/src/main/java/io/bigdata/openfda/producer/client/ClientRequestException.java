package io.bigdata.openfda.producer.client;

import org.springframework.http.HttpStatus;

public class ClientRequestException extends RuntimeException {
    public ClientRequestException(final HttpStatus httpStatus) {
        super (String.format("Request not processed by FDA API. Response status code: %s", httpStatus.value()));
    }
}
