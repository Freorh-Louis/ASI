package com.ptittonnerre.users.userMicroService;

public class UserDTO {
    private int id;
    private String name;
    private String mail;
    private int wallet;

    public UserDTO() {}

    public UserDTO(int id, String name, String mail, int wallet) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.wallet = wallet;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public int getWallet() { return wallet; }
    public void setWallet(int wallet) { this.wallet = wallet; }
}
