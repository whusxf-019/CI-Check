package com.example.hp.activitytest.util;

import android.app.Application;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;

public class myApplication extends Application {
    private static myApplication singleton;
    public Lock lock =  new ReentrantLock();
    private static final String url = "http://wonder.vipgz1.idcfengye.com/ddd/message";
    private static String useAccount = "6667";//退出登录的时候记得清空
    private static String adminAccount = "111";
    public static String type = "user";

    public static StandardUser standardUser;
    public static Administrator administrator;

    private static ArrayList<HashMap<String,String>> admin_group_message = new ArrayList<>();
    private static ArrayList<HashMap<String,String>> admin_leave_message = new ArrayList<>();

    //下方属性均为uer的
    private static ArrayList<HashMap<String,String>> user_group_message = new ArrayList<>();
    private static ArrayList<HashMap<String,String>> user_leave_message = new ArrayList<>();
    private static int can_check = -1;//未初始化，此时打卡界面检查此变量不会改变状态，-1为不可打卡,0为未通知，可打卡，1为可打卡，已通知，未改按钮，1为可打卡，已通知，已改按钮
    private static String end_time_for_check;
    private static String address;
    public static myApplication getInstance() {
        return singleton;
    }

    public static String getAddress() {
        return address;
    }

    public void startReceiveMessage() {


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //从服务器获取消息并分类存放

                HashMap<String, String> map = new HashMap<>();
                if(type.equals("user")) {//当前用户为user
                    map.put("userAccount", useAccount);
                    map.put("type", type);
                    String result = TranslateMessage.sendpost(url, map);
                    try {
                        JSONObject jsonObject1 = new JSONObject(result);
                        JSONArray b = jsonObject1.getJSONArray("result");
                        for (int i = 0; i < b.length(); i++) {
                            JSONObject jsonObject2 = b.getJSONObject(i);
                            HashMap<String,String> hashMap = new HashMap<>();
                            if (jsonObject2.toString().equals("")) break;//没有消息，直接退出
                            hashMap.put("type", jsonObject2.getString("type"));
                            hashMap.put("content", jsonObject2.getString("content"));
                            hashMap.put("addTime", jsonObject2.getString("addTime"));
                            hashMap.put("userAccount", jsonObject2.getString("userAccount"));
                            hashMap.put("subType", jsonObject2.getString("subType"));
                            hashMap.put("state", jsonObject2.getString("state"));
                            if (jsonObject2.getString("type").equals("check_time")) {
                                can_check = 0;
                                String[] content = jsonObject2.getString("content").split("_");
                                end_time_for_check = content[0];
                                address = content[1];
                            } else if (jsonObject2.getString("type").equals("user_group_message")) {
                                user_group_message.add(hashMap);
                            } else if (jsonObject2.getString("type").equals("user_leave_message")) {
                                user_leave_message.add(hashMap);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{//当前用户为管理员
                    map.put("adminAccount", adminAccount);
                    map.put("type", type);
                    String result = TranslateMessage.sendpost(url, map);
                    try {
                        JSONObject jsonObject1 = new JSONObject(result);
                        JSONArray b = jsonObject1.getJSONArray("result");
                        for (int i = 0; i < b.length(); i++) {
                            JSONObject jsonObject2 = b.getJSONObject(i);
                            if (jsonObject2.toString().equals("")) break;//没有消息，直接退出
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("type", jsonObject2.getString("type"));
                            hashMap.put("content", jsonObject2.getString("content"));
                            hashMap.put("addTime", jsonObject2.getString("addTime"));
                            hashMap.put("adminAccount", jsonObject2.getString("adminAccount"));
                            hashMap.put("subType", jsonObject2.getString("subType"));
                            hashMap.put("state", jsonObject2.getString("state"));
                            if (jsonObject2.getString("type").equals("check_time")) {

                            } else if (jsonObject2.getString("type").equals("admin_group_message")) {
                                admin_group_message.add(hashMap);
                            } else if (jsonObject2.getString("type").equals("admin_leave_message")) {
                                admin_leave_message.add(hashMap);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1000, 5000);
    }
    public static boolean UGMisEmpty(){
        return user_group_message.isEmpty();
    }
    public static boolean ULMisEmpty(){
        return user_leave_message.isEmpty();
    }
    public static HashMap<String,String> getUserGroupMessage(){
        HashMap<String,String> result = user_group_message.get(0);
        return result;
    }
    public static HashMap<String,String> getUserLeaveMessage(){
        HashMap<String,String> result =  user_leave_message.get(0);
        return result;
    }
    public static int getCanCheck(){
        return can_check;
    }
    public static String getEnd_time_for_check(){
        return end_time_for_check;
    }
    public static void setCanCheck(int i){
        can_check = i;
    }
    public static boolean AGMisEmpty(){
        return user_group_message.isEmpty();
    }
    public static boolean ALMisEmpty(){
        return user_leave_message.isEmpty();
    }
    public static HashMap<String,String> getAdminGroupMessage(int i){
        HashMap<String,String> result = admin_group_message.get(i);
        return result;
    }
    public static HashMap<String,String> getAdminLeaveMessage(int i){
        HashMap<String,String> result =  admin_leave_message.get(i);
        return result;
    }
    //下面方法将已处理的message重新丢掉
    public void popFromUGM(){
        user_group_message.remove(0);
    }
    public void popFromULM(){
        user_leave_message.remove(0);
    }
    public void popFromAGM(){
        admin_group_message.remove(0);
    }
    public void popFromALM(){
        admin_leave_message.remove(0);
    }
    public int getUGMlength(){
        return user_group_message.size();
    }
    public int getULMlength(){
        return user_group_message.size();
    }
    public int getAGMlength(){
        return  admin_group_message.size();
    }
    public int getALMlength(){
        return  admin_leave_message.size();
    }
     @Override
     public final void onCreate() {
        super.onCreate();
        singleton = this;
         SDKInitializer.initialize(this);
        }

        public static String getAdminAccount()
        {
            return  adminAccount;
        }

}