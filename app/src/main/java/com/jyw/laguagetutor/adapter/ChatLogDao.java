package com.jyw.laguagetutor.adapter;

import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.Query;

import com.jyw.laguagetutor.recyclerData.ChatLogData;


@Dao
public interface ChatLogDao {

    @Insert
    void insert(ChatLogData chatLogData);

    @Query("SELECT*FROM ChatLogData WHERE host=:host")
    ChatLogData select(String host);

    @Query("UPDATE ChatLogData SET log=:log WHERE host=:host")
    void update(String host, String log);

    @Query("SELECT log From ChatLogData WHERE host=:host")
    String getLog(String host);

    @Query("DELETE FROM ChatLogData")
    void deleteAll();
}
