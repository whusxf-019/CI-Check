package com.example.hp.activitytest.activities;

import android.os.AsyncTask;

import com.example.hp.activitytest.util.TranslateMessage;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserReport {

}

class userReport1 extends AsyncTask<Void,Void,List<Integer>> {
    String startTime = UserReportActivity.starttime;
    String endTime = UserReportActivity.endtime;

    private List<Integer> userCount = new ArrayList<>();
    @Override
    protected List<Integer> doInBackground(Void... voids) {
        //獲取第一個結果
        String url = "http://wonder.vipgz1.idcfengye.com/ddd/report";
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map1.put("startTime",startTime);
        map1.put("endTime",endTime);
        map1.put("type", "user_report_all");
        String result1 = TranslateMessage.sendpost(url, map1);
        //獲取第二個結果
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map2.put("startTime",startTime);
        map2.put("endTime",endTime);
        map2.put("type", "user_report_checked");
        String result2 = TranslateMessage.sendpost(url, map2);
        //獲取第三個結果
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map3.put("startTime",startTime);
        map3.put("endTime",endTime);
        map3.put("type", "user_report_unchecked");
        String result3 = TranslateMessage.sendpost(url, map3);

        //獲取第四個結果
        HashMap<String, String> map4 = new HashMap<>();
        map4.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map4.put("startTime",startTime);
        map4.put("endTime",endTime);
        map4.put("type", "user_report_ill");
        String result4 = TranslateMessage.sendpost(url, map4);

        //獲取第五個結果
        HashMap<String, String> map5 = new HashMap<>();
        map5.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map5.put("startTime",startTime);
        map5.put("endTime",endTime);
        map5.put("type", "user_report_marry");
        String result5 = TranslateMessage.sendpost(url, map5);

        //獲取第六個結果
        HashMap<String, String> map6 = new HashMap<>();
        map6.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map6.put("startTime",startTime);
        map6.put("endTime",endTime);
        map6.put("type", "user_report_die");
        String result6 = TranslateMessage.sendpost(url, map6);

        //獲取第七個結果
        HashMap<String, String> map7 = new HashMap<>();
        map7.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map7.put("startTime",startTime);
        map7.put("endTime",endTime);
        map7.put("type", "user_report_something");
        String result7 = TranslateMessage.sendpost(url, map7);

        //獲取第八個結果
        HashMap<String, String> map8 = new HashMap<>();
        map8.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map8.put("startTime",startTime);
        map8.put("endTime",endTime);
        map8.put("type", "user_report_born");
        String result8 = TranslateMessage.sendpost(url, map8);

        //獲取第九個結果
        HashMap<String, String> map9 = new HashMap<>();
        map9.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map9.put("startTime",startTime);
        map9.put("endTime",endTime);
        map9.put("type", "user_report_other");
        String result9 = TranslateMessage.sendpost(url, map9);

        //獲取第十個結果
        HashMap<String, String> map10 = new HashMap<>();
        map10.put("userAccount", LoginActivity.myapp.standardUser.getId());
        map10.put("startTime",startTime);
        map10.put("endTime",endTime);
        map10.put("type", "user_report_leave_all");
        String result10 = TranslateMessage.sendpost(url, map10);


        int a = Integer.parseInt(result1);
        userCount.add(a);

        int b = Integer.parseInt(result2);
        userCount.add(b);

        int c = Integer.parseInt(result3);
        userCount.add(c);

        int leaveNum = userCount.get(0) - userCount.get(1) - userCount.get(2);
        userCount.add(leaveNum);

        int d = Integer.parseInt(result4);
        userCount.add(d);

        int e = Integer.parseInt(result5);
        userCount.add(e);

        int f = Integer.parseInt(result6);
        userCount.add(f);

        int g = Integer.parseInt(result7);
        userCount.add(g);

        int h = Integer.parseInt(result8);
        userCount.add(h);

        int i = Integer.parseInt(result9);
        userCount.add(i);

        int j = Integer.parseInt(result10);
        userCount.add(j);

        return userCount;
    }

    @Override
    protected void onPostExecute(List<Integer> integers) {
        super.onPostExecute(integers);
    }
}
