package com.jyw.laguagetutor.recyclerData;

public class ChatListData {

    private String roomName,lastMessage,date,unreadMessage,image;
    private int roomId;

    public ChatListData(int roomId, String roomName, String lastMessage, String date, String unreadMessage) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.lastMessage = lastMessage;
        this.date = date;
        this.unreadMessage = unreadMessage;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(String unreadMessage) {
        this.unreadMessage = unreadMessage;
    }
}
