package com.pharmacy.onlinepharmacy;

public class ReviewDataModel {

    private String name;
    private float ratingStars;
    private String review;
    private String id;

    public ReviewDataModel(String name, float ratingStars, String review, String id) {
        this.name = name;
        this.ratingStars = ratingStars;
        this.review = review;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(int ratingStars) {
        this.ratingStars = ratingStars;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
