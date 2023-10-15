package com.jyw.laguagetutor.recyclerData;

public class ClassData1 {
    private String image,mobile,name,education,time,date;
    private float rating;

    public ClassData1(String image, String mobile, String name, String education, String time, String date, float rating) {
        this.image = image;
        this.mobile = mobile;
        this.name = name;
        this.education = education;
        this.time = time;
        this.date = date;
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
