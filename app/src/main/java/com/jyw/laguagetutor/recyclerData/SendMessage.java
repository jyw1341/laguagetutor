package com.jyw.laguagetutor.recyclerData;

public class SendMessage {



    private String room_id;
    private String type;
    private String task;
    private String mobile_number;
    private String user_name;
    private String message;

    public SendMessage(String room_id, String type, String task, String mobile_number, String user_name, String message) {
        this.room_id = room_id;
        this.type = type;
        this.task = task;
        this.mobile_number = mobile_number;
        this.user_name = user_name;
        this.message = message;
    }

    @Override
    public String toString() {
        return "SendMessage{" +
                "room_id='" + room_id + '\'' +
                ", type='" + type + '\'' +
                ", task='" + task + '\'' +
                ", mobile_number='" + mobile_number + '\'' +
                ", user_name='" + user_name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
