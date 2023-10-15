package com.jyw.laguagetutor.recyclerData;

public class Student {

    private String name,title,gender,age,location, mobileNumber,studyType;


    public Student(String name, String title, String gender, String age, String location, String mobileNumber, String studyType) {
        this.name = name;
        this.title = title;
        this.gender = gender;
        this.age = age;
        this.location = location;
        this.mobileNumber = mobileNumber;
        this.studyType = studyType;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
