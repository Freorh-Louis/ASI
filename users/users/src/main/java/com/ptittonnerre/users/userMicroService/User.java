package com.ptittonnerre.users.userMicroService;

import com.ptittonnerre.users.globalLibraries.Card;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "T_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true)
    private String mail;

    private String password;

    private int wallet;

    @JsonIgnore
    @OneToMany
    @JoinTable(
            name = "TJ_USER_CARD",
            joinColumns = @JoinColumn(name = "idUser"),
            inverseJoinColumns = @JoinColumn(name = "idCard")
    )
    private Set<Card> cards = new HashSet<>();

    //  Constructeurs
    public User() {}

    public User(Integer id, String name, String mail, String password, int wallet) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.wallet = wallet;
    }

    //  Getters / Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards != null ? cards : new HashSet<>();
    }

    //  Méthodes utilitaires

    /**
     * Ajoute une carte à l'utilisateur si elle n'est pas déjà présente.
     * @param card la carte à ajouter
     * @return true si ajoutée, false si déjà existante
     */
    public boolean addCard(Card card) {
        return cards.add(card); // Set empêche les doublons si Card a bien equals/hashCode
    }

    /**
     * Supprime une carte précise de l'utilisateur.
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /**
     * Supprime une carte par son ID.
     */
    public boolean removeCardById(String cardId) {
        return cards.removeIf(card -> card.getId().equals(cardId));
    }

    @Override
    public String toString() {
        return "USER [" + id + "] name:" + name + ", mail:" + mail + ", wallet:" + wallet + ", nbCards:" + cards.size();
    }
}
