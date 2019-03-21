package com.example.hp.activitytest.model;

import android.widget.Button;

import com.example.hp.activitytest.util.GsonUtils;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.myApplication;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Author:陈庚
public class StandardUser implements Serializable {
    String su_id;
    String name;
    Boolean isJoin;
    String join_company_id;
    String join_company_name;
    String password;
    String image;
    String gender;
    String tel;
    String add;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public static String getUrl() {
        return url;
    }

    final static String url = "http://wonder.vipgz1.idcfengye.com/ddd/";
    List<Leave_Apply> applies = new LinkedList<Leave_Apply>();

    public StandardUser() {

    }

    public StandardUser(String su_id, String name, String join_company_id, String join_company_name) {
        this.su_id = su_id;
        this.name = name;
        this.join_company_id = join_company_id;
        this.join_company_name = join_company_name;
    }


    public StandardUser(String su_id, String name, String image) {
        this.su_id = su_id;
        this.name = name;
        this.image = image;
    }

    public StandardUser(String su_id, String name, Boolean isJoin, String join_company_id, String password) {
        this.su_id = su_id;
        this.name = name;
        this.isJoin = isJoin;
        this.join_company_id = join_company_id;
        this.password = password;
    }
    //加入人脸信息

    public String getId() {
        return su_id;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public Boolean getJion() {
        return isJoin;
    }

    public String getJoin_company_id() {
        return join_company_id;
    }

    //uid must be 6 dig
    public void setId(String su_id) {
        this.su_id = su_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJion(Boolean jion) {
        isJoin = jion;
    }

    public void setJoin_company_id(String join_company_id) {
        this.join_company_id = join_company_id;
    }


    public Boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJoin_company_name() {
        return join_company_name;
    }

    public void setJoin_company_name(String join_company_name) {
        this.join_company_name = join_company_name;
    }

    //查看请假记录
    public static ArrayList<HashMap<String, String>> leave_record(String type, String su_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "check_leave_by_user");
        map.put("userAccount", su_id);
        String url1 = url + "leave";
        String result = TranslateMessage.sendpost(url1, map);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        if (result.equals("false")) return arrayList;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jo = jsonObject.getJSONArray("result");
            for (int i = 0; i < jo.length(); i++) {
                JSONObject d = jo.getJSONObject(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("adminAccount", d.getString("adminAccount"));
                hashMap.put("handTime", d.getString("handTime"));
                hashMap.put("startTime", d.getString("startTime"));
                hashMap.put("endTime", d.getString("endTime"));
                hashMap.put("state", d.getString("state"));
                hashMap.put("reason", d.getString("reason"));
                hashMap.put("leave_type", d.getString("leave_type"));
                arrayList.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return arrayList;
        }
        return arrayList;
    }

    //提交请假信息
    public static Boolean submitLeave(String su_id,String leave_type,String reason,String startTime,String endTime){
        String type = "insertInto_leave";
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("userAccount", su_id);
        try {
            map.put("leave_type",  URLEncoder.encode(leave_type,"UTF-8"));
            map.put("reason", URLEncoder.encode(reason,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        String url1 = url + "leave";
        String result = TranslateMessage.sendpost(url1, map);
        if(result.equals("true")){
            return true;
        }else {
            return false;
        }
    }


    //查看小组成员列表
    public static ArrayList<HashMap<String, String>> getWorkmate(String type, String su_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "check_me_by_user");
        map.put("userAccount", su_id);
        String url1 = url + "other";
        String result = TranslateMessage.sendpost(url1, map);

        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
        if (result.equals("false")) return hashMaps;
        try {
            jsonObject = new JSONObject(result);
            jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userName", jo.getString("userName"));
                hashMap.put("userAccount", jo.getString("userAccount"));
                hashMaps.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMaps;
    }

    //查看成员详细信息
    public static ArrayList<HashMap<String, String>> lookUpMemberInfo(String type, String member_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "see_user_info");
        map.put("memberAccount", member_id);
        String url1 = url + "other";
        String result = TranslateMessage.sendpost(url1, map);

        ;
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        if (result.equals("false")) return arrayList;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jo = jsonObject.getJSONArray("result");
            for (int i = 0; i < jo.length(); i++) {
                JSONObject d = jo.getJSONObject(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("adminAccount", d.getString("adminAccount"));
                hashMap.put("userAccount", d.getString("userAccount"));
                hashMap.put("adminName", d.getString("adminName"));
                hashMap.put("userName", d.getString("userName"));
                arrayList.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    //申请加入小组,返回true就是成功申请
    public static String applyJoin(String type, String su_id, String au_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "apply_join");
        map.put("userAccount", su_id);
        map.put("adminAccount", au_id);
        String url1 = url + "group";
        String result = TranslateMessage.sendpost(url1, map);
        return result;
    }

    //查看管理员具体信息
    public static ArrayList<HashMap<String, String>> lookUpAdminInfo(String type, String au_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "see_admin_info");
        map.put("adminAccount", au_id);
        String url1 = url + "other";
        String result = TranslateMessage.sendpost(url1, map);

        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
        if (result.equals("false")) return hashMaps;
        try {
            jsonObject = new JSONObject(result);
            jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("adminName", jo.getString("adminName"));
                hashMap.put("adminAccount", jo.getString("adminAccount"));
                hashMaps.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMaps;
    }

    //确认加入分组接收邀请
    public static String joinGroup(String type, String su_id, String au_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "join_group");
        map.put("userAccount", su_id);
        map.put("adminAccount", au_id);
        String url1 = url + "group";
        String result = TranslateMessage.sendpost(url1, map);
        return result;
    }

    //搜索管理员
    public static ArrayList<HashMap<String, String>> searchUser(String type, String su) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userAccount", su);
        map.put("type", type);
        String url = "see_admin_info";
        String result = TranslateMessage.sendpost(url, map);

        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
        if (result.equals("false")) return hashMaps;
        try {
            jsonObject = new JSONObject(result);
            jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("adminName", jo.getString("adminName"));
                hashMap.put("adminAccount", jo.getString("adminAccount"));
                hashMaps.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMaps;
    }

    //判断是否已经加入分组
    public static Boolean isJoinGroup(final String userAccount) {
        String myUserAccount = new String(userAccount);
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HashMap<String, String> map = new HashMap<>();
                map.put("userAccount", userAccount);
                map.put("type", "check_if_have_group");
                String url1 = url + "other";
                String result = TranslateMessage.sendpost(url1, map);
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

//退出分组
    public static Boolean exitGroup(final String userAccount) {
        String myUserAccount = new String(userAccount);
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HashMap<String, String> map = new HashMap<>();
                map.put("userAccount", userAccount);
                map.put("type", "quit_group");
                map.put("subType","quit");
                String url1 = url + "group";
                String result = TranslateMessage.sendpost(url1, map);
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

    public static String checkin(String su_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "check_confirm");
        map.put("userAccount", su_id);
        map.put("endTime", myApplication.endtime);
        String url1 = url + "checkin";
        String result = TranslateMessage.sendpost(url1, map);
        return result;
    }
    public static ArrayList<HashMap<String,String>> getCheckInfo(String day,String su_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "get_check_info_by_user");
        map.put("userAccount", su_id);
        map.put("day",day);
        String url1 = url + "checkin";
        String result = TranslateMessage.sendpost(url1, map);
        JSONObject jsonObject=null;
        JSONArray jsonArray=null;
        ArrayList<HashMap<String,String>> hashMaps=new ArrayList<>();
        try {
            jsonObject=new JSONObject(result);
            jsonArray=jsonObject.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jo = jsonArray.getJSONObject(i);
                HashMap<String ,String> hashMap=new HashMap<>();
                hashMap.put("adminAccount",jo.getString("adminAccount"));
                hashMap.put("state",jo.getString("state"));
                hashMap.put("checkedTime",jo.getString("checkedTime"));
                hashMaps.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMaps;
    }
    //判断用户是否已经登陆
    public static String isLogin(String userAccount) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "user_isLogin");
        map.put("userAccount", userAccount);
        String url1 = url + "other";
        String result = TranslateMessage.sendpost(url1, map);
        return result;
    }
    //退出时调用，修改isLogin='0'
    public static String user_exit(String userAccount){
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "user_exit");
        map.put("userAccount", userAccount);
        String url = "http://wonder.vipgz1.idcfengye.com/ddd/other";
        String result = TranslateMessage.sendpost(url, map);
        return result;
    }

    //查看自己的信息
    public static HashMap<String,String> user_lookSelf(String userAccount)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "user_lookSelf");
        map.put("userAccount", userAccount);
        String url1 = url + "other";
        String result = TranslateMessage.sendpost(url1, map);

        JSONObject jsonObject=null;
        JSONArray jsonArray=null;
        HashMap<String,String> hashMap=new HashMap<>();
        try {
            jsonObject=new JSONObject(result);
            jsonArray=jsonObject.getJSONArray("result");
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                hashMap.put("userAccount", jo.getString("userAccount"));
                hashMap.put("userName", jo.getString("userName"));
                hashMap.put("adminAccount", jo.getString("adminAccount"));
                hashMap.put("adminName", jo.getString("adminName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }
    //修改自己的信息
    public static Boolean user_updateInfo(final String userAccount,final String userName) {
        String myUserAccount = new String(userAccount);
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HashMap<String, String> map = new HashMap<>();
                map.put("userAccount", userAccount);
                map.put("type", "user_updateInfo");
                map.put("userName",userName);
                String url1 = url + "other";
                String result = TranslateMessage.sendpost(url1, map);
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
    public static String getTimeByUser(String type,String su_id)
    {
        HashMap<String,String> map = new HashMap<>();
        map.put("userAccount",su_id);
        map.put("type",type);
        String url="http://wonder.vipgz1.idcfengye.com/ddd/checkin";
        String result = TranslateMessage.sendpost(url,map);
        return result;
    }

}
