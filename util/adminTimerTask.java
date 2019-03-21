package com.example.hp.activitytest.util;


import android.app.Application;

import com.example.hp.activitytest.activities.LoginActivity;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


//这个类用来为admin检查有没有消息，如果有就用对应的处理函数处理，默认不处理，如果需要处理必须重载函数
//注意！必须每个页面一个适当的地方new timer,然后让timer定时执行下面这个类的方法
public class adminTimerTask extends TimerTask {
    private myApplication myApp;
    public adminTimerTask(){
        myApp = LoginActivity.myapp;
    }
    @Override
    public void run() {
        myApp.lock.lock();
        //用户点击其他页面时应该立刻中止当前页面的timer并且启动新页面的timer
        //timer初次运行会立刻执行一次这个类的方法，如果放的位置与函数合理，每次进入新页面都会刷新一次，需要的话
        for(int i=0;i<myApp.getAGMlength();i++){
            dealWithAdminGroupMessage(myApp.getAdminGroupMessage(0));
        }
        for(int j = 0;j<myApp.getALMlength();j++){
            dealWithAdminLeaveMessage(myApp.getAdminLeaveMessage(0));
        }
        myApp.lock.lock();
        //没有消息就直接返回
    }
    //message结构为{type:xxx,content:xxx,addTime:xxx,adminAccount:xxx,subType:xxx,state:xxx}
    //其中，,adminAccount,subType, addTime
    //content为消息内容，视具体情况对前台有用的数据为content而定
    //subType表示具体的类型，如adimin有apply_join,quit,is_agree,someone_join,分别对应申请加入分组，离开分组，是否接受邀请,新成员加入
    public void dealWithAdminGroupMessage(HashMap<String,String> message){
        //doNothing();
        switch(message.get("subType")) {
            case "apply_join":
                dealWithApplyJoin(message);
                break;
            case "quit":
                dealWithquit(message);
                break;
            case "is_agree":
                dealWithIsAgree(message);
                break;
            case "someone_join":
                dealWithSomeoneJoin(message);
                break;
            default:
                break;
        }
    }
    //subType有apply_leave
    public void dealWithAdminLeaveMessage(HashMap<String,String> message){
        //doNothing();
        switch(message.get("subType")) {
            case "apply_leave":
                dealWithApplyLeave(message);
                break;
        }
    }
    public void endDealWithAGM(){
        //从缓冲池丢弃已经处理的消息
            myApp.popFromAGM();

    }
    public void endDealWithALM(){
            myApp.popFromALM();
    }
    public void dealWithApplyJoin(HashMap<String,String> message){
        //doNothing
    }
    public void dealWithquit(HashMap<String,String> message){
        //doNothing
    }
    public void dealWithIsAgree(HashMap<String,String> message){
        //doNothing
    }
    public void dealWithApplyLeave(HashMap<String,String> message){
        //doNothing
    }
    public void dealWithSomeoneJoin(HashMap<String,String> message){
        //doNothing
    }
}
