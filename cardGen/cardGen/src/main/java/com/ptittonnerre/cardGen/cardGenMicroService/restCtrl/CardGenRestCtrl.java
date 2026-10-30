package com.ptittonnerre.cardGen.cardGenMicroService.restCtrl;

import com.ptittonnerre.cardGen.cardGenMicroService.globalLibraries.Card;
import com.ptittonnerre.cardGen.cardGenMicroService.model.CardGenRequest;
import com.ptittonnerre.cardGen.cardGenMicroService.service.CardGenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cardGen")
public class CardGenRestCtrl {
    private final CardGenService cardGenService;

    public CardGenRestCtrl(CardGenService cardGenService) {
        this.cardGenService = cardGenService;
    }

    /**
     * Initialise 20 cartes si la base est vide
     * POST /cards/init
     */
    @PostMapping("/newCard")
    public ResponseEntity<Card> newCard(@RequestBody CardGenRequest cardGenRequest) throws InterruptedException {
        Card newCard = cardGenService.generateCard(cardGenRequest.getPrompt(), cardGenRequest.getToken());
        return ResponseEntity.ok(newCard);
    }
}
