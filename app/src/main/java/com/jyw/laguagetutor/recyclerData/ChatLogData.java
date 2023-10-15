package com.jyw.laguagetutor.recyclerData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"host"},
        unique = true)})
public class ChatLogData {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "host")
    private String host;
    private String log;

    public ChatLogData(String host, String log) {
        this.host = host;
        this.log = log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", log='" + log + '\'' +
                '}';
    }
}
