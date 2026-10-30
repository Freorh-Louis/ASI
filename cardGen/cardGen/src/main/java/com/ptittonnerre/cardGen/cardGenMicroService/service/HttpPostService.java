package com.ptittonnerre.cardGen.cardGenMicroService.service;

import com.ptittonnerre.cardGen.cardGenMicroService.model.DescriptionHttpGet;
import com.ptittonnerre.cardGen.cardGenMicroService.model.DescriptionHttpPost;
import com.ptittonnerre.cardGen.cardGenMicroService.model.ImgHttpPost;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class HttpPostService extends HttpServiceModel {

    public HttpPostService() {
        super();
    }

    public <ResponseType, BodyType> Mono<ResponseEntity<ResponseType>> request(WebClient webClient,
                                                                               String uri,
                                                                               Class<ResponseType> responseType,
                                                                               Class<BodyType> bodyType,
                                                                               BodyType body,
                                                                               String token) {
        Mono<ResponseEntity<ResponseType>> response = webClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, token)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(Mono.just(body), bodyType)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .toEntity(responseType);
        return response;
    }
}
