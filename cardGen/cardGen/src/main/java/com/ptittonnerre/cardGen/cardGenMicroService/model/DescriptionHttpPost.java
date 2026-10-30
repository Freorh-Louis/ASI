package com.ptittonnerre.cardGen.cardGenMicroService.model;

public class DescriptionHttpPost {
    private String promptTxt;
    private String contextTxt;

    public DescriptionHttpPost(String promptTxt) {
        this.promptTxt = promptTxt;
        this.contextTxt = "";
    }

    public String getPromptTxt() {
        return promptTxt;
    }

    public void setPromptTxt(String promptTxt) {
        this.promptTxt = promptTxt;
    }

    public String getContextTxt() {
        return contextTxt;
    }

    public void setContextTxt(String contextTxt) {
        this.contextTxt = contextTxt;
    }
}
