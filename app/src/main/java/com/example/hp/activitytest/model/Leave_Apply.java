package com.example.hp.activitytest.model;

import java.util.Calendar;

//Author:陈庚
public class Leave_Apply {
    String su_id;
    String au_id;
    String type;
    String start_time;
    String end_time;
    String stst_of_apply;
    String describ;
    String handTime;


    String[] types={"病假","婚假","丧假","事假","产假","其他"};
    String[] stats={"未处理","成功","失败"};

    public Leave_Apply(String su_id, String au_id, String type, String start_time, String end_time, String stst_of_apply, String describ) {
        this.su_id = su_id;
        this.au_id = au_id;
        this.type = type;
        this.start_time = start_time;
        this.end_time = end_time;
        this.stst_of_apply = stst_of_apply;
        this.describ = describ;
    }

    public String getSu_id() {
        return su_id;
    }

    public void setSu_id(String su_id) {
        this.su_id = su_id;
    }

    public String getAu_id() {
        return au_id;
    }

    public void setAu_id(String au_id) {
        this.au_id = au_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStst_of_apply() {
        return stst_of_apply;
    }

    public void setStst_of_apply(String stst_of_apply) {
        this.stst_of_apply = stst_of_apply;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public String getHandTime() {
        return handTime;
    }

    public void setHandTime(String handTime) {
        this.handTime = handTime;
    }
}
