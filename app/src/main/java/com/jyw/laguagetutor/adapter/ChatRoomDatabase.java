package com.jyw.laguagetutor.adapter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.jyw.laguagetutor.recyclerData.ChatLogData;
import com.jyw.laguagetutor.recyclerData.ChatMessage;
import com.jyw.laguagetutor.recyclerData.ChatRoom;

@Database(entities = {ChatRoom.class, ChatMessage.class, ChatLogData.class}, version = 8)
public abstract class ChatRoomDatabase extends RoomDatabase {
    public abstract ChatRoomDao chatRoomDao();
    public abstract ChatMessageDao chatMessageDao();
    public abstract ChatLogDao chatLogDao();
}
