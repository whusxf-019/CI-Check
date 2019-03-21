package com.example.hp.activitytest.util;

import java.util.HashMap;

import java.util.TimerTask;


//这个类用来为user检查有没有消息，如果有就用对应的处理函数处理，默认不处理，如果需要处理必须重载函数
//比如用户在打卡，突然有分组的消息，如果打卡界面设置不处理该消息，就不会有反应
//当用户回到消息界面，消息界面设置处理了该消息，就会作出相应的反应（重复从group集合中取消息，并对每条消息执行dealWithUserGroupMessage函数）
//注意！必须每个页面一个适当的地方new timer,然后让timer定时执行下面这个类的方法
public class userTimerTask extends TimerTask {
    private myApplication myApp;
    public userTimerTask(){
        myApp = (myApplication) myApplication.getInstance();
    }
    @Override
    public void run() {
        myApp.lock.lock();
        //用户点击其他页面时应该立刻中止当前页面的timer并且启动新页面的timer
        //timer初次运行会立刻执行一次这个类的方法，如果放的位置与函数合理，每次进入新页面都会刷新一次，需要的话
        if(myApp.getCanCheck()==0){
            //当getCanCheck为0时表明已经进入签到时间
            //如果用户在开始签到时会进入0状态，此时应该先判断用户所在页面是否为签到页面，
            //如果不在签到页面应该通知用户签到，用户点击通知时改变按钮状态，
            //如果用户不点击通知而从下面点到签到按钮，由于改变页面也会自动重启timer会马上执行下面这个注释
            //如果在签到页面就改变按钮状态（可以点击）
            dealWithCheck0(myApp.getEnd_time_for_check());
        }
        for(int i=0;i<myApp.getUGMlength();i++){
            dealWithUserGroupMessage(myApp.getAdminGroupMessage(0));
        }

        for(int j = 0;j<myApp.getULMlength();j++){
            dealWithUserLeaveMessage(myApp.getAdminLeaveMessage(0));
        }
        myApp.lock.unlock();
        //没有消息就直接返回
    }
    //message结构为{type:xxx,content:xxx,addTime:xxx,userAccount:xxx,subType:xxx,state:xxx}
    //其中，对前台有用的数据为content,userAccount,subType
    //content为消息内容，视具体情况而定
    //subType表示具体的类型，如user有by_admin_delete,by_admin_invite,by_admin_agree,by_admin_disagree
    public void dealWithUserGroupMessage(HashMap<String,String> message){
        //doNothing();
        switch(message.get("subType")) {
            case "by_admin_delete":
                dealWithIsDeleted(message);
                break;
            case "by_admin_invite":
                dealWithIsInvite(message);
                break;
            case "by_admin_agree":
                dealWithAgree(message);
                break;
            case "by_admin_disagree":
                dealWithDisagree(message);
                break;
            default:
                break;
        }
    }
    //subType有by_admin_agree,by_admin_disagree,前台不需要知道为什么跟上一个函数有一样的类型
    public void dealWithUserLeaveMessage(HashMap<String,String> message){
        //doNothing();
        switch(message.get("subType")) {
            case "by_admin_agree":
                dealWithLeaveApplyIsAgree(message);
                break;
            case "by_admin_disagree":
                dealWithLeaveApplyIsDisagree(message);
                break;
            default:
                break;
        }
    }
    public void dealWithCheck0(String endTime){
        //doNothing();
    }
    public void endDealWithUGM(){
        myApp.popFromUGM();
    }
    public void endDealWithULM(){
        myApp.popFromULM();
    }

    //被管理员踢了
    public void dealWithIsDeleted(HashMap<String,String> message){
        //doNothing
    }
    //被邀请加入分组
    public void dealWithIsInvite(HashMap<String,String> message){
        //doNothing
    }
    //管理员拒绝你加入分组
    public void dealWithDisagree(HashMap<String,String> message){
        //doNothing
    }
    //管理员同意你加入分组
    public void dealWithAgree(HashMap<String,String> message){
        //doNothing
    }
    public void dealWithLeaveApplyIsDisagree(HashMap<String,String> message){
        //doNothing
    }
    //管理员同意你加入分组
    public void dealWithLeaveApplyIsAgree(HashMap<String,String> message){
        //doNothing
    }
}
