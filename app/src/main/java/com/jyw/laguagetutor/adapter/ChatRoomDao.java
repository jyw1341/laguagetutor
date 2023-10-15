package com.jyw.laguagetutor.adapter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.jyw.laguagetutor.recyclerData.ChatRoom;

import java.util.List;

@Dao
public interface ChatRoomDao {
    @Query("SELECT * FROM ChatRoom WHERE total_message > 0 AND host=:host ORDER BY last_message_date DESC")
    List<ChatRoom> getAll(String host);

    @Query("SELECT * FROM ChatRoom WHERE room_id =:room_id")
    ChatRoom getRoom(int room_id);

    @Query("SELECT room_id FROM ChatRoom WHERE room_people=:members")
    int getRoomNumber(String members);

    @Query("UPDATE ChatRoom SET last_message =:last_message, last_message_date =:last_message_date, total_message = total_message+1 WHERE room_id=:room_id")
    void updateLastMessage(String last_message,String last_message_date, int room_id);

    @Query("UPDATE ChatRoom SET checked_message = total_message WHERE room_id=:room_id")
    void updateCheckedMessage(int room_id);

    @Query("UPDATE ChatRoom SET total_message =:total_message WHERE room_id=:room_id")
    void updateTotalMessage(int total_message, int room_id);

    @Query("DELETE FROM ChatRoom")
    void deleteAll();

    @Query("DELETE FROM ChatRoom WHERE room_id =:room_id")
    void delete(int room_id);

    @Insert
    void insert(ChatRoom chatRoom);

    @Update
    void update(ChatRoom chatRoom);

    @Delete
    void delete(ChatRoom chatRoom);
}
