package com.ptittonnerre.cardGen.cardGenMicroService.model;

public class ImgHttpPost {
    private String promptTxt;

    public ImgHttpPost(String promptTxt) {
        this.promptTxt = promptTxt;
    }

    public String getPromptTxt() {
        return promptTxt;
    }

    public void setPromptTxt(String promptTxt) {
        this.promptTxt = promptTxt;
    }
}
