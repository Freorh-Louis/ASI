package com.ptittonnerre.market.marketMicroService;


import com.ptittonnerre.market.globalLibraries.Card;
import com.ptittonnerre.market.globalLibraries.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class MarketService {
    private final MarketRepo marketRepo;
    private final WebClient cardWebClient;
    private final WebClient userWebClient;

    public MarketService(MarketRepo marketRepo) {
        this.marketRepo = marketRepo;
        this.cardWebClient = WebClient.create("http://localhost:8088/cards");
        this.userWebClient = WebClient.create("http://localhost:8088/users");

    }
    private void addCardToUser(Long userId, String cardId) {
        userWebClient.post()
                .uri("/{userId}/cards/{cardId}", userId, cardId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private void removeCardFromUser(Long userId, String cardId) {
        userWebClient.delete()
                .uri("/{userId}/cards/{cardId}", userId, cardId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Market createTransaction(String cardId, float prix, Long userId, TransactionType transactionType) {
        if (transactionType == TransactionType.SELL) {
            Card card = cardWebClient.get()
                    .uri("/{id}", cardId)
                    .retrieve()
                    .bodyToMono(Card.class)
                    .block();

            if (card == null) {
                throw new RuntimeException("Erreur : Carte introuvable");
            }

            User vendeur = userWebClient.get()
                    .uri("/{id}", userId)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();

            if (vendeur == null) {
                throw new RuntimeException("Erreur : Vendeur introuvable");
            }

            // Retirer la carte de l'inventaire du vendeur
            removeCardFromUser(userId, cardId);

            return marketRepo.save(new Market(card, prix, vendeur, transactionType));

        } else if (transactionType == TransactionType.BUY) {
            Market market = marketRepo.findByCardIdAndTransactionType(cardId, TransactionType.SELL)
                    .orElseThrow(() -> new RuntimeException("Erreur : Carte non disponible à l'achat"));

            User acheteur = userWebClient.get()
                    .uri("/{id}", userId)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();

            if (acheteur == null) {
                throw new RuntimeException("Erreur : Acheteur introuvable");
            }

            marketRepo.delete(market);

            // Ajouter la carte à l'inventaire de l'acheteur
            addCardToUser(userId, cardId);

            return marketRepo.save(new Market(market.getCard(), prix, acheteur, TransactionType.BUY));
        } else {
            throw new RuntimeException("Erreur : Type de transaction invalide");
        }
    }



    public List<Market> getAllTransactions() {
        return marketRepo.findAll();
    }

    public List<Market> getCardsForSale() {
        return marketRepo.findByTransactionType(TransactionType.SELL);
    }

    public Optional<Card> getCardById(String cardId) {
        try {
            Card card = cardWebClient.get()
                    .uri("/{id}", cardId)
                    .retrieve()
                    .bodyToMono(Card.class)
                    .block();
            return Optional.ofNullable(card);
        } catch (Exception e) {
            return Optional.empty(); // en cas d'erreur ou 404
        }
    }


    public Optional<User> getUserById(Integer userId) {
        try {
            User user = userWebClient.get()
                    .uri("/{id}", userId)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }



}
