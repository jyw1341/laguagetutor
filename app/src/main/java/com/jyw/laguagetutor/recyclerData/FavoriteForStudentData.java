package com.jyw.laguagetutor.recyclerData;

public class FavoriteForStudentData {
    private String image,name,education,region,subject,price,mobile;
    private float rating;

    public FavoriteForStudentData(String mobile, String image, String name, String education, String region, String subject, String price, float rating) {
        this.mobile = mobile;
        this.image = image;
        this.name = name;
        this.education = education;
        this.region = region;
        this.subject = subject;
        this.price = price;
        this.rating = rating;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
