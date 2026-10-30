package com.ptittonnerre.cards.cardMicroService;

import com.ptittonnerre.cards.cardMicroService.globalLibrairies.CardGenRequest;
import com.ptittonnerre.cards.cardMicroService.globalLibrairies.HttpPostService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepo cardRepository;

    private WebClient cardGenWebClient =  WebClient.create("http://localhost:8088/cardGen");

    @PostConstruct
    public void init() throws InterruptedException {
        generateInitialCardsIfEmpty();
    }

    public void generateInitialCardsIfEmpty() throws InterruptedException {
        if (cardRepository.count() == 0) {
            for (int i = 1; i <= 19; i++) {
                Card card = new Card(
                        UUID.randomUUID().toString(),
                        "Hero " + i,
                        "Marvel",
                        "Description de la carte numéro " + i,
                        "https://placehold.it/150x150?text=Card+" + i,
                        1000, 100, 150, 50, 200,
                        null
                );
                cardRepository.save(card);
            }

            Card card = getNewCard("Louis le goat", null); // adapte selon signature
            card.setId(UUID.randomUUID().toString());
            card.setName("Louis le GOAT");
            card.setFamilyName("CLBD");
            card.setPrice(200);
            cardRepository.save(card);
        }
    }

    private Card getNewCard(String prompt, String token) {
        HttpPostService httpPostService = new HttpPostService();
        CardGenRequest cardGenRequest = new CardGenRequest(prompt, token);
        Mono<ResponseEntity<Card>> newCardEntity = httpPostService.request(cardGenWebClient, "/newCard", Card.class, CardGenRequest.class, cardGenRequest, null);
        Card newCard = newCardEntity.block().getBody();
        return newCard;
    }

    public boolean cardExistsById(String id) {
        return cardRepository.existsById(id);
    }

    public Set<Card> get5AvailableRandomCards() {
        List<Card> availableCards = cardRepository.findAll().stream()
                .filter(card -> card.getUserId() == null)
                .collect(Collectors.toList());

        if (availableCards.size() < 5) {
            throw new RuntimeException("Pas assez de cartes disponibles.");
        }

        Collections.shuffle(availableCards);
        return new HashSet<>(availableCards.subList(0, 5));
    }

    public Set<CardDTO> get5RandomStarterCards() {
        return get5AvailableRandomCards().stream()
                .map(card -> new CardDTO(
                        card.getId(),
                        card.getName(),
                        card.getFamilyName(),
                        card.getDescription(),
                        card.getImgSrc(),
                        card.getHp(),
                        card.getEnergy(),
                        card.getAttack(),
                        card.getDefense(),
                        card.getPrice()
                ))
                .collect(Collectors.toSet());
    }


    public List<CardDTO> getAllCardDTOs() {
        return cardRepository.findAll().stream()
                .map(card -> new CardDTO(
                        card.getId(),
                        card.getName(),
                        card.getFamilyName(),
                        card.getDescription(),
                        card.getImgSrc(),
                        card.getHp(),
                        card.getEnergy(),
                        card.getAttack(),
                        card.getDefense(),
                        card.getPrice()
                ))
                .collect(Collectors.toList());
    }

    public Optional<Card> getCardById(String id) {
        return cardRepository.findById(id);
    }

    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    public List<String> getCardIdsByUserId(String userId) {
        return cardRepository.findByUserId(userId).stream()
                .map(Card::getId)
                .collect(Collectors.toList());
    }
}
