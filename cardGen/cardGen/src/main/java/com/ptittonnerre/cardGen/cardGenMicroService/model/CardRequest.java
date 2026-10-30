package com.ptittonnerre.cardGen.cardGenMicroService.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cardRequest")
public class CardRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String cardId;
    private String imgSrc;
    private String description;
    private int hp;
    private int energy;
    private int attack;
    private int defense;

    public CardRequest() {}

    public CardRequest(String cardId, String imgSrc, String description, int hp, int energy, int attack, int defense) {
        this.cardId = cardId;
        this.imgSrc = imgSrc;
        this.description = description;
        this.hp = hp;
        this.energy = energy;
        this.attack = attack;
        this.defense = defense;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
