package com.ptittonnerre.cards.cardMicroService.globalLibrairies;

import com.ptittonnerre.cards.cardMicroService.globalLibrairies.HttpServiceModel;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class HttpGetService extends HttpServiceModel {

    public HttpGetService() {
        super();
    }

    public <ResponseType> Mono<ResponseType> request(WebClient webClient, String uri, Class<ResponseType> responseType) {
        Mono<ResponseType> response = webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .bodyToMono(responseType);
        return response;
    }
}
