package com.example.hp.activitytest.model;

import java.util.Calendar;

//Author:陈庚
public class CheckInInfo {
    private String checkInTime;
    private String state;
    private String userAccount;
    private String adminAccount;

    public CheckInInfo(String checkInTime, String state, String userAccount,String adminAccount) {
        this.checkInTime = checkInTime;
        this.state = state;
        this.userAccount = userAccount;
        this.adminAccount = adminAccount;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getAdminAccount() {
        return adminAccount;
    }
}
