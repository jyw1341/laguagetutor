package com.jyw.laguagetutor.recyclerData;

public class TicketData {
    private String image,name,count,date,mobile_number;

    public TicketData(String image, String name, String count, String date, String mobile_number) {
        this.image = image;
        this.name = name;
        this.count = count;
        this.date = date;
        this.mobile_number = mobile_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
