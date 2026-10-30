package com.ptittonnerre.users.client;

import com.ptittonnerre.users.globalLibraries.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class CardClient {

    private final WebClient cardWebClient;

    public CardClient() {
        //  Adapter le port/host si besoin
        this.cardWebClient = WebClient.create("http://localhost:8082/cards");
    }

    /**
     * Récupère une carte à partir de son ID via requête HTTP GET.
     */
    public Optional<Card> getCardById(String cardId) {
        try {
            Mono<Card> cardMono = cardWebClient.get()
                    .uri("/{id}", cardId)
                    .retrieve()
                    .bodyToMono(Card.class);
            return Optional.ofNullable(cardMono.block());
        } catch (Exception e) {
            return Optional.empty(); // retourne vide si erreur ou carte inexistante
        }
    }

    /**
     * Récupère une liste de cartes aléatoires (par défaut 5 pour un pack de départ).
     */
    public List<Card> getRandomCards(int count) {
        try {
            return cardWebClient.get()
                    .uri("/random?count=" + count)
                    .retrieve()
                    .bodyToFlux(Card.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return List.of(); // vide en cas d’échec
        }
    }
}
