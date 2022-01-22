package io.bigdata.openfda.producer.client;

import org.springframework.http.HttpStatus;

public class ResultsNotFoundException extends RuntimeException {
    public ResultsNotFoundException(final HttpStatus httpStatus) {
        super (String.format("Results not found in FDA API. Response status code: %s", httpStatus.value()));
    }
}
