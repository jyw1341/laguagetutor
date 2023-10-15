package com.jyw.laguagetutor.recyclerData;

import java.util.HashSet;
import java.util.StringTokenizer;

public class ChatData {

    private String room_id;
    private String content;
    private String name;
    private String date;
    private String viewType;
    private HashSet<String> roomMembers;
    private HashSet<String> readers;
    private String unread;

    public ChatData(String room_id, String name,String content , String date,String viewType, String roomMembers, String readers) {
        this.room_id = room_id;
        this.content = content;
        this.name = name;
        this.date = date;
        this.viewType = viewType;

        StringTokenizer stringTokenizer1 = new StringTokenizer(roomMembers,",");
        HashSet<String> setRoomMembers = new HashSet<>();
        while(stringTokenizer1.hasMoreTokens()){
            setRoomMembers.add(stringTokenizer1.nextToken());
        }
        this.roomMembers = setRoomMembers;

        StringTokenizer stringTokenizer2 = new StringTokenizer(readers,",");
        HashSet<String> setReaders = new HashSet<>();
        while(stringTokenizer2.hasMoreTokens()){
            setReaders.add(stringTokenizer2.nextToken());
        }
        this.readers = setReaders;
    }

    public HashSet<String> getRoomMembers() {
        return roomMembers;
    }

    public void setRoomMembers(HashSet<String> roomMembers) {
        this.roomMembers = roomMembers;
    }

    public void addReaders(String num){
        readers.add(num);
    }

    public String getUnread() {
        return Integer.toString(roomMembers.size()-readers.size());
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }
}
