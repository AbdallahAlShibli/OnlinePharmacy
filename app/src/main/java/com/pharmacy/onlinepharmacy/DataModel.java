package com.pharmacy.onlinepharmacy;

public class DataModel {

    private String medicalName, medicalDescription, medicalPrice, medicalCoverURL, medicalID;


    public DataModel(String medicalName, String medicalDescription, String medicalPrice, String medicalCoverURL, String medicalID) {
        this.medicalName = medicalName;
        this.medicalDescription = medicalDescription;
        this.medicalPrice = medicalPrice;
        this.medicalCoverURL = medicalCoverURL;
        this.medicalID = medicalID;
    }

    public String getMedicalName() {
        return medicalName;
    }

    public void setMedicalName(String medicalName) {
        this.medicalName = medicalName;
    }

    public String getMedicalDescription() {
        return medicalDescription;
    }

    public void setMedicalDescription(String medicalDescription) {
        this.medicalDescription = medicalDescription;
    }

    public String getMedicalPrice() {
        return medicalPrice;
    }

    public void setMedicalPrice(String medicalPrice) {
        this.medicalPrice = medicalPrice;
    }

    public String getMedicalCoverURL() {
        return medicalCoverURL;
    }

    public void setMedicalCoverURL(String medicalCoverURL) {
        this.medicalCoverURL = medicalCoverURL;
    }

    public String getMedicalID() {
        return medicalID;
    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
    }
}
