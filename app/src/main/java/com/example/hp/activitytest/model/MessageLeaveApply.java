package com.example.hp.activitytest.model;

import java.io.Serializable;

public class MessageLeaveApply implements Serializable{
    String userAccount;
    String handTime;
    String startTime;
    String endTime;
    String adminAccount;
    String reason;
    String sate;
    String leave_type;

    public MessageLeaveApply(String userAccount, String handTime, String startTime, String endTime,  String reason, String sate, String leave_type) {
        this.userAccount = userAccount;
        this.handTime = handTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.sate = sate;
        this.leave_type = leave_type;
    }
    public MessageLeaveApply()
    {

    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getHandTime() {
        return handTime;
    }

    public void setHandTime(String handTime) {
        this.handTime = handTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public void setAdminAccount(String adminAccount) {
        this.adminAccount = adminAccount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSate() {
        return sate;
    }

    public void setSate(String sate) {
        this.sate = sate;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }
}
