package com.example.hp.activitytest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTime {
    private long time;
    private Date mCurDate;

    public DateTime() {
        this(System.currentTimeMillis());
    }

    public DateTime(long currMillis) {
        time = currMillis;
    }

    public String toString(String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        if (mCurDate == null) {
            mCurDate = new Date(time);
        } else {
            mCurDate.setTime(time);
        }
        return dateFormat.format(mCurDate);
    }

    public String toString() {
        return toString("yyyy-MM-dd HH:mm:ss");
    }

    public DateTime plusHour(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + i);
        return new DateTime(calendar.getTimeInMillis());
    }

    public static DateTime parse(String timeStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(timeStr);
            return new DateTime(date.getTime());
        } catch (ParseException e) {
            return new DateTime();
        }
    }

    public Date getDate() {
        return new Date(time);
    }

    public long getMillis() {
        return time;
    }

    public boolean after(DateTime dateTime) {
        return time > dateTime.getMillis();
    }

    public boolean after(long millis) {
        return this.time > millis;
    }
}

