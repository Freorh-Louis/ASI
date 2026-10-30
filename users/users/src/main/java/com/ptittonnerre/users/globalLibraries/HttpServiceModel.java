package com.ptittonnerre.users.globalLibraries;

import org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Mono;

public class HttpServiceModel {

    public HttpServiceModel() {}

    protected Mono<? extends Throwable> handleErrorResponse(HttpStatusCode statusCode) {

        // Handle non-success status codes here (e.g., logging or custom error handling)
        return Mono.error(new RuntimeException("Request failed. Status code: " + statusCode));
    }
}
