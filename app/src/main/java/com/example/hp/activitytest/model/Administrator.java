package com.example.hp.activitytest.model;

import android.app.Application;

import com.example.hp.activitytest.activities.LoginActivity;
import com.example.hp.activitytest.util.DateTime;
import com.example.hp.activitytest.util.TranslateMessage;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//Author:陈庚
public class Administrator implements Serializable {
    String au_id;
    String name;
    String password;
    String employer;
    //Author:陈庚  time:20180709
    String start_time;
    String end_time;
    String check_city;
    String check_address;
    String day_for_week;
    private Handler myHandler;
    List<StandardUser> members = new LinkedList<StandardUser>();
    String gender;
    String add;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public Administrator() {

    }

    public Administrator(String au_id, String name) {
        this.au_id = au_id;
        this.name = name;
    }

    public static Administrator administrator;

    public static Administrator getAdministrator() {
        return administrator;
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

    public String getCheck_city() {
        return check_city;
    }

    public void setCheck_city(String check_city) {
        this.check_city = check_city;
    }

    public String getAddress() {
        return check_address;
    }

    public void setAddress(String address) {
        this.check_address = address;
    }

    public Administrator(String au_id, String name, String password) {
        this.au_id = au_id;
        this.name = name;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAu_id() {
        return au_id;
    }

    public void setAu_id(String au_id) {
            this.au_id = au_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay_for_week() {
        return day_for_week;
    }

    public void setDay_for_week(String day_for_week) {
        this.day_for_week = day_for_week;
    }

    public List<StandardUser> getMembers() {
        return members;
    }

    public static void openCheckIn( DateTime startTime, DateTime endTime, String week, String address, String adminAccount) {
        HashMap<String, String> map = new HashMap<>();
        map.put("startTime", startTime.toString("HH:mm:ss"));
        map.put("endTime", endTime.toString("HH:mm:ss"));
        map.put("week",week);
        try {
            map.put("address", URLEncoder.encode(address,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("adminAccount","111");
        map.put("type", "set_check_time");

        String url="";
        String result = TranslateMessage.sendpost(url, map);
    }
    //查看员工请假记录<---此处注释有误
    public static ArrayList<HashMap<String,String>> get_check_LeaveApply(String au_id)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "check_leave_by_admin");
        map.put("subType","check");//查找未审核的
        map.put("adminAccount", au_id);
        String url = "http://wonder.vipgz1.idcfengye.com/ddd/leave";
        String result = TranslateMessage.sendpost(url, map);
        JSONObject jsonObject=null;
        JSONArray jsonArray=null;
        ArrayList<HashMap<String,String>> maps=new ArrayList<>();
        try {
            jsonObject=new JSONObject(result);
            jsonArray=jsonObject.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jo = jsonArray.getJSONObject(i);
                HashMap<String ,String> hashMap=new HashMap<>();
                hashMap.put("userAccount",jo.getString("userAccount"));
                hashMap.put("handTime",jo.getString("handTime"));
                hashMap.put("startTime",jo.getString("startTime"));
                hashMap.put("endTime",jo.getString("endTime"));
                hashMap.put("reason",jo.getString("reason"));
                hashMap.put("state",jo.getString("state"));
                hashMap.put("leave_type",jo.getString("leave_type"));
                maps.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maps;

    }
    //请假审批,获取未审批记录
    public static ArrayList<HashMap<String,String>> get_unCheck_LeaveApply(String au_id)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "check_leave_by_admin");
        map.put("subType","uncheck");//此处代码有误
        map.put("adminAccount", au_id);
        String url = "http://wonder.vipgz1.idcfengye.com/ddd/leave";
        String result = TranslateMessage.sendpost(url, map);
        JSONObject jsonObject=null;
        JSONArray jsonArray=null;
        ArrayList<HashMap<String,String>> maps=new ArrayList<>();
        try {
            jsonObject=new JSONObject(result);
            jsonArray=jsonObject.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jo = jsonArray.getJSONObject(i);
                HashMap<String ,String> hashMap=new HashMap<>();
                hashMap.put("userAccount",jo.getString("userAccount"));
                hashMap.put("handTime",jo.getString("handTime"));
                hashMap.put("startTime",jo.getString("startTime"));
                hashMap.put("endTime",jo.getString("endTime"));
                hashMap.put("reason",jo.getString("reason"));
                hashMap.put("state",jo.getString("state"));
                hashMap.put("leave_type",jo.getString("leave_type"));
                maps.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maps;
    }
    //修改请假记录
    public static String checkLeaveApply(String type,String su_id,String start_time)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "checkLeaveApply");
        map.put("userAccount",su_id);
        map.put("start_time",start_time);
        String url = "";
        String result = TranslateMessage.sendpost(url, map);
        return result;
    }
    //查看组员列表
    public static String getGroupMenber(String type,String au_id)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("adminAccount", au_id);
        String url = "http://wonder.vipgz1.idcfengye.com/ddd/other";
        String result = TranslateMessage.sendpost(url, map);
        return result;
    }
    //查看成员详细信息
    public static String lookUpGroupMemberInfo(String type,String member_id)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "lookUpGroupMemberInfo");
        map.put("memberAccount", member_id);
        String url = "";
        String result = TranslateMessage.sendpost(url, map);
        return result;
    }
    //邀请成员
    public static String inviteMember(String type,String u_id,String au_id)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("userAccount", u_id);
        map.put("adminAccount",au_id);
        String url = "http://wonder.vipgz1.idcfengye.com/ddd/group";
        String result = TranslateMessage.sendpost(url, map);
        return result;
    }
    //获取管理员已经设置过的记录
    public static String getTimeRecords(String type,String au_id)
    {
        HashMap<String,String> map = new HashMap<>();
        map.put("adminAccount",au_id);
        map.put("type",type);
        String url="http://wonder.vipgz1.idcfengye.com/ddd/checkin";
        String result = TranslateMessage.sendpost(url,map);
        return result;
    }
    public static ArrayList<HashMap<String,String>>  searchUser(String type, String su)
    {
        HashMap<String,String> map = new HashMap<>();
        map.put("userAccount",su);
        map.put("type",type);
        String url="http://wonder.vipgz1.idcfengye.com/ddd/other";
        String result = TranslateMessage.sendpost(url,map);
        JSONObject jsonObject=null;
        JSONArray jsonArray=null;
        ArrayList<HashMap<String,String>> maps=new ArrayList<>();
        try {
            jsonObject=new JSONObject(result);
            jsonArray=jsonObject.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jo = jsonArray.getJSONObject(i);
                HashMap<String ,String> hashMap=new HashMap<>();
                hashMap.put("userName",jo.getString("userName"));
                hashMap.put("userAccount",jo.getString("userAccount"));
                hashMap.put("adminAccount",jo.getString("adminAccount"));
                hashMap.put("adminName",jo.getString("adminName"));
                maps.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maps;
    }

    public static String agree(String type,String su_id,String start_time)
    {
        HashMap<String,String> map = new HashMap<>();
        map.put("userAccount",su_id);
        map.put("startTime",start_time);
        map.put("type",type);//type="update_leave_agree"
        map.put("adminAccount",LoginActivity.myapp.administrator.getAu_id());
        String url="http://wonder.vipgz1.idcfengye.com/ddd/leave";
        String result = TranslateMessage.sendpost(url,map);
        return result;
    }
    public static String disagree(String type,String su_id,String start_time)
    {
        HashMap<String,String> map = new HashMap<>();
        map.put("userAccount",su_id);
        map.put("startTime",start_time);
        map.put("type",type);//type="update_leave_disagree"
        String url="http://wonder.vipgz1.idcfengye.com/ddd/leave";
        String result = TranslateMessage.sendpost(url,map);
        return result;
    }

    //移除成员
    public static String remove(String type,String userAccount,String adminAccount)
    {
        HashMap<String,String> map=new HashMap<>();
        map.put("userAccount",userAccount);
        map.put("adminAccount",adminAccount);
        map.put("type",type);
        map.put("subType","by_admin_delete");
        String url="http://wonder.vipgz1.idcfengye.com/ddd/group";
        String result = TranslateMessage.sendpost(url,map);
        return result;
    }
    //查看指定日期的签到信息 日期格式为YYYY-MM-DD
    public static ArrayList<HashMap<String,String>> getCheckInfo_by_day(String day,String adminAccount)
    {
        HashMap<String,String> map = new HashMap<>();
        map.put("adminAccount",adminAccount);
        map.put("day",day);
        map.put("type","getCheckInfoByAdminForDay");
        String url="http://wonder.vipgz1.idcfengye.com/ddd/checkin";//add your code here
        String result = TranslateMessage.sendpost(url,map);
        JSONObject jsonObject=null;
        JSONArray jsonArray=null;
        ArrayList<HashMap<String,String>> maps=new ArrayList<>();
        try {
            jsonObject=new JSONObject(result);
            jsonArray=jsonObject.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jo = jsonArray.getJSONObject(i);
                HashMap<String ,String> hashMap=new HashMap<>();
                hashMap.put("userAccount",jo.getString("userAccount"));
                hashMap.put("checkedTime",jo.getString("checkedTime"));
                hashMap.put("state",jo.getString("state"));
                maps.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maps;
    }

    //判断用户是否已经登陆
    public static String isLogin(String adminAccount) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "admin_isLogin");
        map.put("adminAccount", adminAccount);
        String url1 = "http://wonder.vipgz1.idcfengye.com/ddd/other";
        String result = TranslateMessage.sendpost(url1, map);
        return result;
    }
    //退出时调用，修改isLogin='0'
    public static String admin_exit(String adminAccount){
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "admin_exit");
        map.put("adminAccount", adminAccount);
        String url1 = "http://wonder.vipgz1.idcfengye.com/ddd/other";
        String result = TranslateMessage.sendpost(url1, map);
        return result;
    }
    //查看自己的信息
    public static HashMap<String,String> admin_lookSelf(String adminAccount)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "admin_lookSelf");
        map.put("adminAccount", adminAccount);
        String url =  "http://wonder.vipgz1.idcfengye.com/ddd/other";
        String result = TranslateMessage.sendpost(url, map);

        JSONObject jsonObject=null;
        JSONArray jsonArray=null;
        HashMap<String,String> hashMap=new HashMap<>();
        try {
            jsonObject=new JSONObject(result);
            jsonArray=jsonObject.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                hashMap.put("adminAccount", jo.getString("adminAccount"));
                hashMap.put("adminName", jo.getString("adminName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    //修改自己的信息
    public static Boolean admin_updateInfo(final String adminAccount,final String adminName) {
        String myUserAccount = new String(adminAccount);
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HashMap<String, String> map = new HashMap<>();
                map.put("adminAccount", adminAccount);
                map.put("type", "admin_updateInfo");
                map.put("adminName",adminName);
                String url = "http://wonder.vipgz1.idcfengye.com/ddd/other";
                String result = TranslateMessage.sendpost(url, map);
                return result;
            }
        });
        executor.shutdown();
        while (true){
            if(future.isDone()){
                try {
                    if (future.get().equals("false")){
                        return false;
                    }
                    else{
                        return true;
                    }
                }catch (Exception e){

                }
            }
        }
    }

}
