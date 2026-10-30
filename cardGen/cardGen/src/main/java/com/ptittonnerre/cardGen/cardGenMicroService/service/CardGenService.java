package com.ptittonnerre.cardGen.cardGenMicroService.service;

import com.ptittonnerre.cardGen.cardGenMicroService.globalLibraries.Card;
import com.ptittonnerre.cardGen.cardGenMicroService.model.DescriptionHttpGet;
import com.ptittonnerre.cardGen.cardGenMicroService.model.DescriptionHttpPost;
import com.ptittonnerre.cardGen.cardGenMicroService.model.ImgHttpGet;
import com.ptittonnerre.cardGen.cardGenMicroService.model.ImgHttpPost;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tp.cpe.ImgToProperties;

import java.util.Map;
import java.util.Objects;

@Service
public class CardGenService {
    private final WebClient descriptionWebClient = WebClient.create("http://tp.cpe.fr:8088/llm-service");
    private final WebClient imgWebClient = WebClient.create("http://tp.cpe.fr:8088/img-service");
    private final HttpPostService httpPostService = new HttpPostService();
    private final HttpGetService httpGetService = new HttpGetService();

    public CardGenService() {}

    public Map<String, Float> getAttributes(String imgSrc) {
        return ImgToProperties.getPropertiesFromImg(imgSrc, 100f, 4, 0.3f, false);
    }


    private Mono<ResponseEntity<DescriptionHttpGet>> postDescriptionRequest(String prompt, String token) {
        String uri;
        if (token == null) {
            uri = "/fake/prompt/req";
        } else {
            uri = "/prompt/req";
        }
        DescriptionHttpPost descriptionHttpPost = new DescriptionHttpPost(prompt);
        Mono<ResponseEntity<DescriptionHttpGet>> response = httpPostService.request(descriptionWebClient, uri, DescriptionHttpGet.class, DescriptionHttpPost.class, descriptionHttpPost, token);
        return response;
    }

    private String postImgRequest(String prompt, String token) {
        String uri;
        if (token == null) {
            uri = "/fake/prompt/req";
        } else {
            uri = "/prompt/req";
        }
        ImgHttpPost imgHttpPost = new ImgHttpPost(prompt); // Crée une instance de la classe ImgHttpPost
        Mono<ResponseEntity<String>> response = httpPostService.request(imgWebClient, uri, String.class, ImgHttpPost.class, imgHttpPost, token);
        return response.block().getBody();
    }


    public Card generateCard(String prompt, String token) throws InterruptedException {
        String imgSrc = null;
        String description = null;

        // Send a request to generate an image and a description
        String imgUUID = postImgRequest(prompt, token);
        Mono<ResponseEntity<DescriptionHttpGet>> descriptionPostRequest = postDescriptionRequest(prompt, token);
        String descriptionUUID = descriptionPostRequest.block().getBody().getApiReqId();

        // Waiting for the APIs to generate an image and a description
        boolean imgRequestFinished = false;
        boolean descriptionRequestFinished = false;
        while (!imgRequestFinished || !descriptionRequestFinished) {
            Thread.sleep(500);
            if (!imgRequestFinished) {
                Mono<ImgHttpGet> imgHttpGetMono = httpGetService.request(imgWebClient, "/prompt/req/api/" + imgUUID, ImgHttpGet.class);
                if (Objects.equals(imgHttpGetMono.block().getState(), "FINISHED")) {
                    imgSrc = "http://tp.cpe.fr:8088/img-service" + imgHttpGetMono.block().getUrl();
                    imgRequestFinished = true;
                }
            }
            if (!descriptionRequestFinished) {
                Mono<DescriptionHttpGet> descriptionHttpGetMono = httpGetService.request(descriptionWebClient, "/prompt/req/" + descriptionUUID, DescriptionHttpGet.class);
                if (Objects.equals(descriptionHttpGetMono.block().getState(), "FINISHED")) {
                    description = descriptionHttpGetMono.block().getResponsePromptTxt();
                    description = description.substring(0, 250) + "...";
                    descriptionRequestFinished = true;
                }
            }
        }

        // When we have our description and image, we create the attribute from the image
        Map<String, Float> attributes = getAttributes(imgSrc);
        int hp = Math.round(attributes.get("HP"));
        int energy = Math.round(attributes.get("ENERGY"));
        int attack = Math.round(attributes.get("ATTACK"));
        int defense = Math.round(attributes.get("DEFENSE"));
        return new Card(null, null, null, description, imgSrc, hp, energy, attack, defense, 0);
    }

//    public static void main(String[] args) throws InterruptedException {
//        CardGenService cardGenService = new CardGenService();
//        Card newCard = cardGenService.generateCard("An epic dragon warrior", null);
//        System.out.println(newCard);
//    }
}
