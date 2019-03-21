package com.example.hp.activitytest.model;

import java.io.Serializable;

public class AD_TimeItem implements Serializable {
    private String starting;
    private String ending;
    private Boolean isActivate;
    private String weekday;
    private String location;



    public AD_TimeItem(String starting, String ending, Boolean isACtivate, String weekday, String location) {
        this.starting = starting;
        this.ending = ending;
        this.isActivate = isACtivate;
        this.weekday = weekday;
        this.location = location;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public String getEnding() {
        return ending;
    }

    public void setEnding(String ending) {
        this.ending = ending;
    }

    public Boolean getActivate() {
        return isActivate;
    }

    public void setActivate(Boolean activate) {
        isActivate = activate;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
