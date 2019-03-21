package com.example.hp.activitytest.util;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManagerReport {
    //管理员需要有日统计和月统计
//    //管理员需要有饼状考勤图：考勤率，缺勤率，
//    //管理员需要有请假类型图：各类请假原因的占比
//    private List<Double> DailySignCount = new ArrayList<>();
//    private List<Double> MonthlySignCount = new ArrayList<>();
//    private List<String> DailySignRate = new ArrayList<>();
//    private List<String> MonthlySignRate = new ArrayList<>();
//    private List<String> DailyApplyRate = new ArrayList<>();
//    private List<String> MonthlyApplyRate = new ArrayList<>();
//
//    //获取考勤状况，考勤人数，请假状况的每日统计和月统计
//    public List<Double> getDailySignCount(String account,String dateTime) {
//		List<Double>dailySignCount;
//        //利用account,DailyTime获取打卡人数，未打卡人数，打卡总人数,分别对应dailySignCount[0],1,2,在此处填写代码，之后dailySignCount不再作为参数而是函数变量
//        Date DailyTime = new Date(System.currentTimeMillis());//获取当前时间
//        for (int i = 0; i < dailySignCount.size(); i++) {
//            DailySignCount.add(dailySignCount.get(i));
//        }
//        return DailySignCount;
//    }
//
//    public List<Double> getMonthlySignCount(String account, List<Double> monthlySignCount) {
//        //传递数据库：account,StartTime,EndTime
//        Date EndTime = new Date(System.currentTimeMillis());//此处需要获取当天的时间
//        Date StartTime = EndTime;//此处需要修改为StartTime减去一个月的时间
//        //在此处填写代码，之后dailySignCount不再作为参数而是函数变量
//        for (int i = 0; i < monthlySignCount.size(); i++) {
//            MonthlySignCount.add(monthlySignCount.get(i));
//        }
//        return MonthlySignCount;
//    }
//    public List<String> getDailySignRate(String account,double totalCount,List<Double> dailySignCount)
//    {
//        Date DailyTime = new Date(System.currentTimeMillis());//此处需要获取当天的时间
//        //在此处填写代码，之后totalCount,dailySignCount不再作为参数而是函数变量
//        for(int i = 0;i<dailySignCount.size();i++)
//        {
//            NumberFormat nf = NumberFormat.getPercentInstance();
//            nf.setMinimumFractionDigits(2);//设置保留小数位
//            nf.setRoundingMode(RoundingMode.HALF_UP); //设置舍入模式
//            DailySignRate.add(nf.format(dailySignCount.get(i)/totalCount));
//        }
//        return DailySignRate;
//    }
//    public List<String> getMonthlySignRate(String account,double totalCount,List<Double> monthlySignCount) {
//        //传递数据库：account,StartTime,EndTime
//        Date EndTime = new Date(System.currentTimeMillis());//此处需要获取当天的时间
//        Date StartTime = EndTime;//此处需要修改为StartTime减去一个月的时间
//        //在此处填写代码，之后totalCount,dailySignCount不再作为参数而是函数变量
//        for (int i = 0; i < monthlySignCount.size(); i++) {
//            NumberFormat nf = NumberFormat.getPercentInstance();
//            nf.setMinimumFractionDigits(2);//设置保留小数位
//            nf.setRoundingMode(RoundingMode.HALF_UP); //设置舍入模式
//            MonthlySignRate.add(nf.format(monthlySignCount.get(i)/totalCount));
//        }
//        return MonthlySignRate;
//    }
//    public List<String> getDailyApplyRate(String account,double totalCount,List<Double> dailyApplyCount)
//    {
//        Date DailyTime = new Date(System.currentTimeMillis());//此处需要获取当天的时间
//        //在此处填写代码，之后totalCount,dailySignCount不再作为参数而是函数变量,totalCount为请假总人数，dailyApplyCount为请假类型人数数组
//        for(int i = 0;i<dailyApplyCount.size();i++)
//        {
//            NumberFormat nf = NumberFormat.getPercentInstance();
//            nf.setMinimumFractionDigits(2);//设置保留小数位
//            nf.setRoundingMode(RoundingMode.HALF_UP); //设置舍入模式
//            DailyApplyRate.add(nf.format(dailyApplyCount.get(i)/totalCount));
//        }
//        return DailyApplyRate;
//    }
//    public List<String> getMonthlyApplyRate(String account,double totalCount,List<Double> monthlyApplyCount)
//    {
//        Date EndTime = new Date(System.currentTimeMillis());//此处需要获取当天的时间
//        Date StartTime = EndTime;//此处需要将EndTime减去一个月
//        //需要传入数据库的数据：account,EndTime,StartTime
//        //在此处填写代码，之后totalCount,dailySignCount不再作为参数而是函数变量,totalCount为请假总人数，dailyApplyCount为请假类型人数数组
//        for(int i = 0;i<monthlyApplyCount.size();i++)
//        {
//            NumberFormat nf = NumberFormat.getPercentInstance();
//            nf.setMinimumFractionDigits(2);//设置保留小数位
//            nf.setRoundingMode(RoundingMode.HALF_UP); //设置舍入模式
//            MonthlyApplyRate.add(nf.format(monthlyApplyCount.get(i)/totalCount));
//        }
//        return MonthlyApplyRate;
//    }
}
