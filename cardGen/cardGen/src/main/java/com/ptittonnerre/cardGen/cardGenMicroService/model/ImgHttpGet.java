package com.ptittonnerre.cardGen.cardGenMicroService.model;

public class ImgHttpGet {
    private int id;
    private String apiReqId;
    private String url;
    private String state;
    private String endRequestDate;
    private String externalUrl;

    public ImgHttpGet(int id, String apiReqId, String url, String state, String endRequestDate, String externalUrl) {
        this.id = id;
        this.apiReqId = apiReqId;
        this.url = url;
        this.state = state;
        this.endRequestDate = endRequestDate;
        this.externalUrl = externalUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApiReqId() {
        return apiReqId;
    }

    public void setApiReqId(String apiReqId) {
        this.apiReqId = apiReqId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndRequestDate() {
        return endRequestDate;
    }

    public void setEndRequestDate(String endRequestDate) {
        this.endRequestDate = endRequestDate;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }
}
