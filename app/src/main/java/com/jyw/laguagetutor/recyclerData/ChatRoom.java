package com.jyw.laguagetutor.recyclerData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"room_id"},
        unique = true)})

public class ChatRoom {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "room_id")
    private int room_id;
    private String host;
    private String room_people;
    private String room_type;
    private String room_name;
    private String last_message;
    private int total_message;
    private int checked_message;
    private String last_message_date;

    public ChatRoom(int room_id,String host, String room_people, String room_name, String room_type) {
        this.room_id = room_id;
        this.host = host;
        this.room_people = room_people;
        this.room_name = room_name;
        this.room_type = room_type;
        this.last_message =null;
        this.last_message_date=null;
        this.total_message = 0;
        this.checked_message =0;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getRoom_people() {
        return room_people;
    }

    public void setRoom_people(String room_people) {
        this.room_people = room_people;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public int getTotal_message() {
        return total_message;
    }

    public void setTotal_message(int total_message) {
        this.total_message = total_message;
    }

    public int getChecked_message() {
        return checked_message;
    }

    public void setChecked_message(int checked_message) {
        this.checked_message = checked_message;
    }

    public String getLast_message_date() {
        return last_message_date;
    }

    public void setLast_message_date(String last_message_date) {
        this.last_message_date = last_message_date;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", room_id=" + room_id +
                ", host='" + host + '\'' +
                ", room_people='" + room_people + '\'' +
                ", room_type='" + room_type + '\'' +
                ", room_name='" + room_name + '\'' +
                ", last_message='" + last_message + '\'' +
                ", total_message=" + total_message +
                ", checked_message=" + checked_message +
                ", last_message_date='" + last_message_date + '\'' +
                '}';
    }
}
