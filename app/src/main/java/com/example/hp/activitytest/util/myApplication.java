package com.example.hp.activitytest.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.baidu.mapapi.SDKInitializer;
import com.example.hp.activitytest.R;
import com.example.hp.activitytest.activities.AD;
import com.example.hp.activitytest.activities.AD_MessageActivity;
import com.example.hp.activitytest.activities.FaceCheckActivity;
import com.example.hp.activitytest.activities.LoginActivity;
import com.example.hp.activitytest.activities.MapTestActivity;
import com.example.hp.activitytest.activities.SU;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;

public class myApplication extends Application {
    public static List<Activity> myActivity = new ArrayList<>();
    public static Activity ActivityExcept;
    public static Activity currentActivity =null;
    private static myApplication singleton;
    public Lock lock =  new ReentrantLock();
    private static final String url = "http://wonder.vipgz1.idcfengye.com/ddd/message";
    public static String type = "user";
    public static boolean isFirst = true;
    private static Date startTime = null;
    private static Date endTime = null;
    public static String endtime = null;
    public static String starttime = null;
    public static boolean isCheckTimeNotice = false;
    public static boolean isOtherMessageNotice = false;
    public static AD_MessageActivity.MyHandler myHandler = null;
    public static SU.MyHandler suTimeHandler = null;


    static Timer timer=null;

    public static StandardUser standardUser;
    public static Administrator administrator;

    private static ArrayList<HashMap<String,String>> admin_group_message = new ArrayList<>();//
    private static ArrayList<HashMap<String,String>> admin_leave_message = new ArrayList<>();//

    //下方属性均为uer的
    private static ArrayList<HashMap<String,String>> user_group_message = new ArrayList<>();//
    private static ArrayList<HashMap<String,String>> user_leave_message = new ArrayList<>();//
    private static int can_check = -1;//未初始化，此时打卡界面检查此变量不会改变状态
    private static String address="";//
    private static boolean isFinishing = true;
    public static myApplication getInstance() {
        return singleton;
    }

    public static String getAddress() {
        return address;
    }

    public void startReceiveMessage() {
        isFinishing = false;


        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //从服务器获取消息并分类存放


                HashMap<String, String> map = new HashMap<>();
                if(type.equals("user")) {//当前用户为user
                    if(standardUser==null) return;
                    String userAccount = standardUser.getId();
                    map.put("userAccount", userAccount);
                    map.put("type", type);
                    if(isFirst) {
                        map.put("subType", "first");
                    }else {
                        map.put("subType", "notfirst");
                    }
                    isFirst = false;
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
                                //进入程序时选出第一条最近的考勤记录
                                String[] content = jsonObject2.getString("content").split("_");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    starttime = content[2];
                                    endtime = content[0];
                                    startTime = sdf.parse(content[2]);
                                    endTime = sdf.parse(content[0]);
                                    if(!isFirst) isCheckTimeNotice = false;//因为第一次获取的时间并不一定可以打卡，但是后面的消息一定是可以打卡的
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                address = content[1];
                            } else if (jsonObject2.getString("type").equals("user_group_message")) {
                                user_group_message.add(hashMap);
                            } else if (jsonObject2.getString("type").equals("user_leave_message")) {
                                user_leave_message.add(hashMap);
                            }
                            isOtherMessageNotice = false;
                        }
                        //判断是否能够开启打卡了
                        Date now = new Date();
                        if(startTime!=null&&endTime!=null) {
                            if (startTime.after(now) || endTime.after(now)) {
                                can_check = 0;
                                //发送通知给用户
                                if(!isCheckTimeNotice&&currentActivity!=null&&!isFinishing){
                                    currentActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationUtil notificationUtils = new NotificationUtil(getApplicationContext());
                                            notificationUtils.sendNotification("你有一条重要通知", "又到签到时间啦！",MapTestActivity.class);
                                            isCheckTimeNotice = true;
                                            if(suTimeHandler!=null&&suTimeHandler.isExist()) {//通知su界面，需要刷新一次su界面
                                                Message msg = new Message();
                                                msg.what = 2;
                                                msg.obj = startTime;
                                                suTimeHandler.sendMessage(msg);

                                            }//end if
                                        }//end run
                                    });//end runonUiThread
                                }//end if
                            } else {//如果在打卡时间内，反之
                                can_check = -1;
                                //通知su界面
                                if(suTimeHandler!=null&&suTimeHandler.isExist()) {
                                    Message msg = new Message();
                                    msg.what = 3;
                                    msg.obj = endTime;
                                    suTimeHandler.sendMessage(msg);
                                }
                            }
                        }
                        if(user_leave_message.size()>0||user_group_message.size()>0) {
                            //发送通知给用户
                            if(!isOtherMessageNotice&&currentActivity.getClass()!=AD_MessageActivity.class){
                                final int a = user_group_message.size()+user_leave_message.size();
                                currentActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(currentActivity,"您有"+a+"条消息未读",Toast.LENGTH_LONG).show();
                                        isOtherMessageNotice = true;
                                    }
                                });
                            }
                            if(myHandler!=null&&myHandler.isExist()){
                                ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
                                arrayList.addAll(user_group_message);
                                arrayList.addAll(user_leave_message);
                                Message msg= new Message();
                                msg.what = 1;
                                msg.obj = arrayList;
                                myHandler.sendMessage(msg);
                                user_group_message.clear();
                                user_leave_message.clear();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{//当前用户为管理员
                    if(isFirst) {
                        map.put("subType", "first");
                    }else {
                        map.put("subType", "notfirst");
                    }
                    isFirst = false;//不再进入if语句
                    if(administrator==null) return;
                    String adminAccount = administrator.getAu_id();
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
                            isOtherMessageNotice = false;
                        }
                        if(admin_leave_message.size()>0||admin_group_message.size()>0) {
                            //发送通知给用户
                            if(!isOtherMessageNotice&&currentActivity.getClass()!=AD_MessageActivity.class){
                                final int a = admin_group_message.size()+admin_leave_message.size();
                                currentActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(currentActivity,"您有"+String.valueOf(a)+"条消息未读",Toast.LENGTH_LONG).show();
                                        isOtherMessageNotice = true;
                                    }
                                });
                            }
                            if(myHandler!=null&&myHandler.isExist()){
                                ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
                                arrayList.addAll(admin_leave_message);
                                arrayList.addAll(admin_group_message);
                                Message msg= new Message();
                                msg.what = 1;
                                msg.obj = arrayList;
                                myHandler.sendMessage(msg);
                                admin_leave_message.clear();
                                admin_group_message.clear();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1000, 2000);
    }
    public static boolean UGMisEmpty(){
        return user_group_message.isEmpty();
    }
    public static boolean ULMisEmpty(){
        return user_leave_message.isEmpty();
    }
    public static HashMap<String,String> getUserGroupMessage(int i){
        HashMap<String,String> result = user_group_message.get(i);
        return result;
    }
    public static HashMap<String,String> getUserLeaveMessage(int i){
        HashMap<String,String> result =  user_leave_message.get(i);
        return result;
    }
    public static int getCanCheck(){
        return can_check;
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
        return user_leave_message.size();
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
            if(administrator!=null)
                return  administrator.getAu_id();
            else
                return "false";
        }

    public static String getUserAccount()
    {
        if(standardUser!=null)
            return  standardUser.getId();
        else
            return "false";
    }
    public static void notifictian(Context context){
        //非签到页面需要通知用户签到
        //
        Intent intent=new Intent(context, MapTestActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
        NotificationManager nm= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        @SuppressLint("WrongConstant") Notification baseNF =new Notification.Builder(context)//设置启动的context
                .setContentTitle("签到啦")//设置标题
                .setContentText("ddd")//设置内容
                .setSmallIcon(R.mipmap.ic_launcher)//设置要显示的两个图片 小图片可以设置资源文件，大图片为bitmap类型所以需要decodeResource
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker("签到啦！")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)//设置声音
                .setDefaults(Notification.DEFAULT_LIGHTS)//设置指示灯
                .setDefaults(Notification.DEFAULT_VIBRATE)//设置震动
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                .setContentIntent(pendingIntent)
                .build();

        baseNF.flags|= Notification.FLAG_AUTO_CANCEL;
        nm.notify(0, baseNF);

    }

    public static void exit()
    {
        if(LoginActivity.myapp.standardUser!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result=LoginActivity.myapp.standardUser.user_exit(LoginActivity.myapp.standardUser.getId());
                    LoginActivity.myapp.standardUser=null;
                }
            }).start();
        }
        if(LoginActivity.myapp.administrator!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result=LoginActivity.myapp.administrator.admin_exit(LoginActivity.myapp.administrator.getAu_id());
                    LoginActivity.myapp.administrator = null;
                }
            }).start();
        }
        admin_group_message.clear();
        admin_leave_message.clear();
        user_leave_message.clear();
        user_group_message.clear();
        address="";
        can_check=-1;
        isFirst = true;
        startTime = null;
        endTime = null;
        endtime = null;
        starttime = null;
        myHandler = null;
        isCheckTimeNotice = false;
        isOtherMessageNotice = false;
        isFinishing = true;
        timer.cancel();
        for(Activity x :myActivity){
            if(!x.isFinishing()&&(!x.isDestroyed()))
                x.finish();
        }
    }
    public static void exit(Activity activity)
    {
        if(LoginActivity.myapp.standardUser!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result=LoginActivity.myapp.standardUser.user_exit(LoginActivity.myapp.standardUser.getId());
                    LoginActivity.myapp.standardUser=null;
                }
            }).start();
        }
        if(LoginActivity.myapp.administrator!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result=LoginActivity.myapp.administrator.admin_exit(LoginActivity.myapp.administrator.getAu_id());
                    LoginActivity.myapp.administrator = null;
                }
            }).start();
        }
        admin_group_message.clear();
        admin_leave_message.clear();
        user_leave_message.clear();
        user_group_message.clear();
        address="";
        can_check=-1;
        isFirst = true;
        startTime = null;
        endTime = null;
        endtime = null;
        starttime = null;
        myHandler = null;
        isCheckTimeNotice = false;
        isOtherMessageNotice = false;
        isFinishing = true;
        timer.cancel();
        for(Activity x :myActivity){
            if(!x.equals(activity)){
                if(!x.isFinishing()&&(!x.isDestroyed()))
                    x.finish();
            }
        }
    }

}