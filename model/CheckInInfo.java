package com.example.hp.activitytest.model;

import java.util.Calendar;

//Author:陈庚
public class CheckInInfo {
    Calendar start_time=Calendar.getInstance();
    Calendar end_time=Calendar.getInstance();
    int turnOut;
    int absence;

    public CheckInInfo(Calendar start_time, Calendar end_time) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.turnOut = 0;
        this.absence = 0;
    }

    public Calendar getStart_time() {
        return start_time;
    }

    public void setStart_time(Calendar start_time) {
        this.start_time = start_time;
    }

    public Calendar getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Calendar end_time) {
        this.end_time = end_time;
    }

    public int getTurnOut() {
        return turnOut;
    }

    public void setTurnOut(int turnOut) {
        this.turnOut = turnOut;
    }

    public int getAbsence() {
        return absence;
    }

    public void setAbsence(int absence) {
        this.absence = absence;
    }
}
