package com.pharmacy.onlinepharmacy;

public class IDModel {

    private String id;
    private String collection;

    public IDModel(String id, String collection) {
        this.id = id;
        this.collection = collection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }
}
