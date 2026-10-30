package com.ptittonnerre.users.globalLibraries;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.ptittonnerre.users.userMicroService.User;

@Entity
public class Card {

    @Id
    private String id;

    private String name;
    private String familyName;
    private String description;
    private String imgSrc;

    private int hp;
    private int energy;
    private int attack;
    private int defense;
    private int price;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonBackReference
    private User user;

    public Card() {}

    public Card(String id, String name, String familyName, String description, String imgSrc,
                int hp, int energy, int attack, int defense, int price) {
        this.id = id;
        this.name = name;
        this.familyName = familyName;
        this.description = description;
        this.imgSrc = imgSrc;
        this.hp = hp;
        this.energy = energy;
        this.attack = attack;
        this.defense = defense;
        this.price = price;
    }

    // GETTERS & SETTERS

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImgSrc() { return imgSrc; }
    public void setImgSrc(String imgSrc) { this.imgSrc = imgSrc; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }

    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }

    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // toString


    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", familyName='" + familyName + '\'' +
                ", description='" + description + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", hp=" + hp +
                ", energy=" + energy +
                ", attack=" + attack +
                ", defense=" + defense +
                ", price=" + price +
                ", user=" + user +
                '}';
    }
}
