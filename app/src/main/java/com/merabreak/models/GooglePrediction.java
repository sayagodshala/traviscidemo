package com.merabreak.models;

public class GooglePrediction {
    private String id;

    private String placeId;

    private String description;

    private String[] types;

    private String reference;

    private String type;

    private String latlng;

    public GooglePrediction(String id, String placeId, String description, String[] types, String reference, String type) {
        this.id = id;
        this.placeId = placeId;
        this.description = description;
        this.types = types;
        this.reference = reference;
        this.type = type;
    }

    public GooglePrediction(String description, String type, String latlng) {
        this.description = description;
        this.type = type;
        this.latlng = latlng;
    }

    public GooglePrediction(String description, String latlng) {
        this.description = description;
        this.latlng = latlng;
    }

    public GooglePrediction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(final String placeId) {
        this.placeId = placeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }
}