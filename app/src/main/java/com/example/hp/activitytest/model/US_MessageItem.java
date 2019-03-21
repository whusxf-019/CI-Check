package com.example.hp.activitytest.model;

/**
 * Created by Administrator on 2018/7/14.
 */

public class US_MessageItem {
    private String messageType;
    private String message;
    private String time;
    private int imageId;

    private String titleType;

    //private StandardUser standardUser;

    public US_MessageItem(String messageType, String message, String time, int imageId, String titleType) {
        this.messageType = messageType;
        this.message = message;
        this.time = time;
        this.imageId = imageId;
        this.titleType = titleType;
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


    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }
}
