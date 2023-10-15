package com.jyw.laguagetutor.recyclerData;

public class FavoriteForTeacherData {
    private String mobile,name,age,gender,region,image;

    public FavoriteForTeacherData(String mobile,String name, String age, String gender, String region, String image) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.region = region;
        this.image = image;
        this.mobile =mobile;

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
