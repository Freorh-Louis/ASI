package com.ptittonnerre.cardGen.cardGenMicroService.model;

public class DescriptionHttpGet {
    private String apiReqId;
    private String originalPromptTxt;
    private String responsePromptTxt;
    private String state;

    public DescriptionHttpGet(String apiReqId, String originalPromptTxt, String responsePromptTxt, String state) {
        this.apiReqId = apiReqId;
        this.originalPromptTxt = originalPromptTxt;
        this.responsePromptTxt = responsePromptTxt;
        this.state = state;
    }

    public String getApiReqId() {
        return apiReqId;
    }

    public void setApiReqId(String apiReqId) {
        this.apiReqId = apiReqId;
    }

    public String getOriginalPromptTxt() {
        return originalPromptTxt;
    }

    public void setOriginalPromptTxt(String originalPromptTxt) {
        this.originalPromptTxt = originalPromptTxt;
    }

    public String getResponsePromptTxt() {
        return responsePromptTxt;
    }

    public void setResponsePromptTxt(String responsePromptTxt) {
        this.responsePromptTxt = responsePromptTxt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
