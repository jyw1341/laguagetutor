package com.jyw.laguagetutor.recyclerData;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    private int message_id;
    private String room_id;
    private String sender_id;
    private String sender_name;
    private String message;
    private String date;
    private String readers;
    private String room_members;
    private String type;
    private String view_type;


    public ChatMessage(String room_id, String sender_id, String sender_name, String message, String date, String readers, String room_members, String type, String view_type) {
        this.room_id = room_id;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.message = message;
        this.date = date;
        this.readers = readers;
        this.room_members = room_members;
        this.type = type;
        this.view_type = view_type;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReaders() {
        return readers;
    }

    public void setReaders(String readers) {
        this.readers = readers;
    }

    public String getRoom_members() {
        return room_members;
    }

    public void setRoom_members(String room_members) {
        this.room_members = room_members;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getView_type() {
        return view_type;
    }

    public void setView_type(String view_type) {
        this.view_type = view_type;
    }

    @Override
    public String toString() {
        return "{" +
                "message_id=" + message_id +
                ", room_id='" + room_id + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", readers='" + readers + '\'' +
                ", room_members='" + room_members + '\'' +
                ", type='" + type + '\'' +
                ", view_type='" + view_type + '\'' +
                '}';
    }
}
