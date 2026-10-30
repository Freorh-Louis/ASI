package com.ptittonnerre.cards.cardMicroService;

import com.ptittonnerre.cards.cardMicroService.Card; // la bonne Card ici
import com.ptittonnerre.cards.cardMicroService.globalLibrairies.CardGenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/cards")
@CrossOrigin(origins = "*")
public class CardRestCtrl {

    private final CardService cardService;

    public CardRestCtrl(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/init")
    public ResponseEntity<String> initCards() throws InterruptedException {
        cardService.generateInitialCardsIfEmpty();
        return ResponseEntity.ok("Cartes générées si base vide.");
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Card> cardExists(@PathVariable String id) {
        return cardService.getCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CardDTO>> getAllCards() {
        List<CardDTO> cards = cardService.getAllCardDTOs();
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/random")
    public ResponseEntity<Set<Card>> getRandomCards() {
        try {
            Set<Card> cards = cardService.get5AvailableRandomCards();
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable String id) {
        return cardService.getCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Card> addCard(@RequestBody Card card) {
        Card saved = cardService.saveCard(card);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/{userId}/ids")
    public ResponseEntity<List<String>> getCardIdsByUser(@PathVariable String userId) {
        List<String> cardIds = cardService.getCardIdsByUserId(userId);
        return ResponseEntity.ok(cardIds);
    }

    // @PostMapping("/generate")
    // public Card generateCard(@RequestBody CardGenRequest request) {
    //     return cardService.generateCardFromMicroservice(request.getPrompt(), request.getToken());
    // }

    @GetMapping("/starter/random")
    public ResponseEntity<Set<CardDTO>> getRandomStarterCards() {
        try {
            Set<CardDTO> cards = cardService.get5RandomStarterCards();
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
