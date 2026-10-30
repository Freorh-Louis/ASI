package com.ptittonnerre.market.globalLibraries;

import com.ptittonnerre.market.globalLibraries.Card;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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
    private Set<Card> cards;

    public User() {}

    public User(Integer id, String name, String mail, String password, int wallet) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.wallet = wallet;
        this.cards = new HashSet<>();
    }

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
        if (cards == null) cards = new HashSet<>();
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card) {
        getCards().add(card);
    }

    public void removeCard(Card card) {
        getCards().remove(card);
    }

    @Override
    public String toString() {
        return "USER [" + id + "] name:" + name + ", mail:" + mail + ", wallet:" + wallet + ", nbCards:" + getCards().size();
    }
}
