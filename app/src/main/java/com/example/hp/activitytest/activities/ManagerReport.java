package com.example.hp.activitytest.activities;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.hp.activitytest.util.TranslateMessage;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ManagerReport {
    //管理员需要有日统计和月统计
    //管理员需要有饼状考勤图：考勤率，缺勤率，
    //管理员需要有请假类型图：各类请假原因的占比

}
class dailyReport extends AsyncTask<Void,Void,List<Integer>> {
    private List<Integer> DailySignCount = new ArrayList<>();
    @Override
    protected List<Integer> doInBackground(Void... voids) {
        //獲取第一個結果
        String url = "http://wonder.vipgz1.idcfengye.com/ddd/report";
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map1.put("type", "admin_daily_report_all");
        String result1 = TranslateMessage.sendpost(url, map1);
        //獲取第二個結果
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map2.put("type", "admin_daily_report_checked");
        String result2 = TranslateMessage.sendpost(url, map2);
        //獲取第三個結果
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map3.put("type", "admin_daily_report_unchecked");
        String result3 = TranslateMessage.sendpost(url, map3);

        int a = Integer.parseInt(result1);
        DailySignCount.add(a);

        int b = Integer.parseInt(result2);
        DailySignCount.add(b);

        int c = Integer.parseInt(result3);
        DailySignCount.add(c);

        int leaveNum = DailySignCount.get(0) - DailySignCount.get(1) - DailySignCount.get(2);
        DailySignCount.add(leaveNum);
        return DailySignCount;
    }

    @Override
    protected void onPostExecute(List<Integer> integers) {
        super.onPostExecute(integers);
    }
}

class monthlyReport extends AsyncTask<Void,Void,List<Integer>> {
    String month = ManagerMainActivity.Monthdate;

    private List<Integer> MonthlySignCount = new ArrayList<>();
    @Override
    protected List<Integer> doInBackground(Void... voids) {
        //獲取第一個結果
        String url = "http://wonder.vipgz1.idcfengye.com/ddd/report";
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map1.put("month",month);
        map1.put("type", "admin_monthly_report_all");
        String result1 = TranslateMessage.sendpost(url, map1);
        //獲取第二個結果
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map2.put("month",month);
        map2.put("type", "admin_monthly_report_checked");
        String result2 = TranslateMessage.sendpost(url, map2);
        //獲取第三個結果
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map3.put("month",month);
        map3.put("type", "admin_monthly_report_unchecked");
        String result3 = TranslateMessage.sendpost(url, map3);

        //獲取第四個結果
        HashMap<String, String> map4 = new HashMap<>();
        map4.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map4.put("month",month);
        map4.put("type", "admin_monthly_report_ill");
        String result4 = TranslateMessage.sendpost(url, map4);

        //獲取第五個結果
        HashMap<String, String> map5 = new HashMap<>();
        map5.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map5.put("month",month);
        map5.put("type", "admin_monthly_report_marry");
        String result5 = TranslateMessage.sendpost(url, map5);

        //獲取第六個結果
        HashMap<String, String> map6 = new HashMap<>();
        map6.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map6.put("month",month);
        map6.put("type", "admin_monthly_report_die");
        String result6 = TranslateMessage.sendpost(url, map6);

        //獲取第七個結果
        HashMap<String, String> map7 = new HashMap<>();
        map7.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map7.put("month",month);
        map7.put("type", "admin_monthly_report_something");
        String result7 = TranslateMessage.sendpost(url, map7);

        //獲取第八個結果
        HashMap<String, String> map8 = new HashMap<>();
        map8.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map8.put("month",month);
        map8.put("type", "admin_monthly_report_born");
        String result8 = TranslateMessage.sendpost(url, map8);

        //獲取第九個結果
        HashMap<String, String> map9 = new HashMap<>();
        map9.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map9.put("month",month);
        map9.put("type", "admin_monthly_report_other");
        String result9 = TranslateMessage.sendpost(url, map9);

        //獲取第十個結果
        HashMap<String, String> map10 = new HashMap<>();
        map10.put("adminAccount", LoginActivity.myapp.administrator.getAu_id());
        map10.put("month",month);
        map10.put("type", "admin_monthly_report_leave_all");
        String result10 = TranslateMessage.sendpost(url, map10);


        int a = Integer.parseInt(result1);
        MonthlySignCount.add(a);

        int b = Integer.parseInt(result2);
        MonthlySignCount.add(b);

        int c = Integer.parseInt(result3);
        MonthlySignCount.add(c);

        int leaveNum = MonthlySignCount.get(0) - MonthlySignCount.get(1) - MonthlySignCount.get(2);
        MonthlySignCount.add(leaveNum);

        int d = Integer.parseInt(result4);
        MonthlySignCount.add(d);

        int e = Integer.parseInt(result5);
        MonthlySignCount.add(e);

        int f = Integer.parseInt(result6);
        MonthlySignCount.add(f);

        int g = Integer.parseInt(result7);
        MonthlySignCount.add(g);

        int h = Integer.parseInt(result8);
        MonthlySignCount.add(h);

        int i = Integer.parseInt(result9);
        MonthlySignCount.add(i);

        int j = Integer.parseInt(result10);
        MonthlySignCount.add(j);

        return MonthlySignCount;
    }

    @Override
    protected void onPostExecute(List<Integer> integers) {
        super.onPostExecute(integers);
    }
}
