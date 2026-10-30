package com.ptittonnerre.market.marketMicroService;



import com.ptittonnerre.market.globalLibraries.Card;
import com.ptittonnerre.market.globalLibraries.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market")
public class MarketRestCtrl {

    private final MarketService marketService;

    public MarketRestCtrl(MarketService marketService) {
        this.marketService = marketService;
    }

    @PostMapping("/transaction")
    public Market createTransaction(@RequestParam String cardId,
                                    @RequestParam float prix,
                                    @RequestParam Integer vendeurId,
                                    @RequestParam TransactionType transactionType) {
        // Récupérer la carte depuis la base
        Card card = marketService.getCardById(cardId)
                .orElseThrow(() -> new RuntimeException("Erreur : Carte introuvable"));

        // Récupérer le vendeur depuis la base
        User vendeur = marketService.getUserById(vendeurId)
                .orElseThrow(() -> new RuntimeException("Erreur : Vendeur introuvable"));

        return marketService.createTransaction(cardId, prix, Long.valueOf(vendeur.getId()), transactionType);
    }

    @GetMapping("/sale")
    public List<Market> getCardsForSale() {
        System.out.println(">>>>> getCardsForSale called");
        return marketService.getCardsForSale();
    }

    @GetMapping("/transactions")
    public List<Market> getAllTransactions() {
        return marketService.getAllTransactions();
    }
}
