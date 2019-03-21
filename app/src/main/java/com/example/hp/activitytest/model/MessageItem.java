package com.example.hp.activitytest.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/11.
 */

public class MessageItem implements Serializable {
    private String messageType;
    private String message;
    private String time;
    private int imageId;
    private String type;

    //private StandardUser standardUser;

    public MessageItem(String messageType, String message, String time, int imageId) {
        this.messageType = messageType;
        this.message = message;
        this.time = time;
        this.imageId = imageId;
    }
    public MessageItem(String type , String messageType, String message, String time, int imageId) {
        this.messageType = messageType;
        this.message = message;
        this.time = time;
        this.imageId = imageId;
        this.type = type;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageType() {
        return messageType;
    }

    public int getImageId(){ return imageId; }

    public String getType() {
        return type;
    }
}
