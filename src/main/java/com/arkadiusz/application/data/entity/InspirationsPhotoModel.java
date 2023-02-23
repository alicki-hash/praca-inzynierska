package com.arkadiusz.application.data.entity;


public class InspirationsPhotoModel {

    private int id;
    private String url;
    private String originalUrl;
    private String largeUrl;
    private String mediumUrl;
    private String photographer;
    private String altText;

    public InspirationsPhotoModel(int id, String url, String originalUrl, String largeUrl,
                                  String mediumUrl, String photographer, String altText) {
        this.id = id;
        this.url = url;
        this.originalUrl = originalUrl;
        this.largeUrl = largeUrl;
        this.mediumUrl = mediumUrl;
        this.photographer = photographer;
        this.altText = altText;
    }

    @Override
    public String toString() {
        return "InspirationsPhotoModel{" +
                "id=" + id +
                ", originalUrl='" + originalUrl + '\'' +
                ", mediumUrl='" + mediumUrl + '\'' +
                '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLargeUrl() {
        return largeUrl;
    }

    public void setLargeUrl(String largeUrl) {
        this.largeUrl = largeUrl;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }
}
