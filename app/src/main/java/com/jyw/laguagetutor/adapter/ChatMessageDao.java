package com.jyw.laguagetutor.adapter;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.jyw.laguagetutor.recyclerData.ChatMessage;

import java.util.List;

@Dao
public interface ChatMessageDao {
    @Query("SELECT*FROM ChatMessage")
    List<ChatMessage> getAll();

    @Query("SELECT*FROM ChatMessage WHERE room_id=:room_id AND type = 'client'")
    List<ChatMessage> getMessageByRoomId(String room_id);

    @Query("SELECT*FROM ChatMessage ORDER BY ROWID DESC LIMIT 1")
    ChatMessage getLastMessage();

    @Query("DELETE FROM ChatMessage")
    void deleteAll();

    @Query("DELETE FROM ChatMessage WHERE room_id=:room_id")
    void delete(String room_id);

    @Query("UPDATE ChatMessage SET readers= readers ||:mobile_number WHERE room_id=:room_id AND type = 'client' AND room_members LIKE '%' || :mobile_number || '%' AND readers NOT LIKE '%' || :mobile_number || '%'")
    void updateReaders(String mobile_number, String room_id);

    @Query("SELECT*FROM ChatMessage WHERE type = 'client' ORDER BY ROWID DESC LIMIT 1")
    ChatMessage getMessage();

    @Insert
    void insert(ChatMessage chatMessage);
}
