package com.ptittonnerre.market.marketMicroService;


import com.ptittonnerre.market.globalLibraries.Card;
import com.ptittonnerre.market.globalLibraries.User;
import jakarta.persistence.*;

@Entity
@Table(name = "market_transactions") // Nom de la table en base
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "vendeur_id", nullable = false)
    private User vendeur;

    private float prix;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // Achat ou Vente

    public Market() {}

    public Market(Card card, float prix, User vendeur, TransactionType transactionType) {
        this.card = card;
        this.prix = prix;
        this.vendeur = vendeur;
        this.transactionType = transactionType;
    }

    public Long getId() { return id; }
    public Card getCard() { return card; }
    public float getPrix() { return prix; }
    public User getVendeur() { return vendeur; }
    public TransactionType getTransactionType() { return transactionType; }

    public void setId(Long id) { this.id = id; }
    public void setCard(Card card) { this.card = card; }
    public void setPrix(float prix) { this.prix = prix; }
    public void setVendeur(User vendeur) { this.vendeur = vendeur; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
}
