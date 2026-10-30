package com.ptittonnerre.cardGen.cardGenMicroService.model;

public class CardGenRequest {
    private String prompt;
    private String token;

    public CardGenRequest(String prompt, String token) {
        this.prompt = prompt;
        this.token = token;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
