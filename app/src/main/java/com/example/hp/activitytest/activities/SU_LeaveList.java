package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.MessageLeaveApply;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.LeaveItemAdapter;
import com.example.hp.activitytest.util.SU_LeaveItemAdapter;
import com.example.hp.activitytest.util.SU_MemberItemAdapter;
import com.example.hp.activitytest.util.myApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SU_LeaveList extends AppCompatActivity {

    RecyclerView su_leave_list;
    SU_LeaveItemAdapter leaveItemAdapter;
    List<MessageLeaveApply> messageLeaveApplies = new ArrayList<>();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_su__leave_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //得到请假记录
                ArrayList<HashMap<String,String>> result= StandardUser.leave_record("check_leave_by_user",LoginActivity.myapp.standardUser.getId());
                Message msg=new Message();
                msg.what=1;
                msg.obj=result;
                handler.sendMessage(msg);
            }
        }).start();

        su_leave_list = (RecyclerView) findViewById(R.id.su_leave_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        su_leave_list.setLayoutManager(layoutManager);


    }

    private void myHandler() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    if(messageLeaveApplies.size()>0)
                        messageLeaveApplies.clear();
                    ArrayList<HashMap<String,String>> results=(ArrayList<HashMap<String, String>>)msg.obj;
                    for(int i=0;i<results.size();i++)
                    {
                        HashMap<String,String> result=results.get(i);

                        MessageLeaveApply messageLeaveApply=new MessageLeaveApply();
                        messageLeaveApply.setAdminAccount(result.get("adminAccount"));
                        messageLeaveApply.setHandTime(result.get("handTime"));
                        messageLeaveApply.setStartTime(result.get("startTime"));
                        messageLeaveApply.setEndTime(result.get("endTime"));
                        messageLeaveApply.setSate(result.get("state"));
                        messageLeaveApply.setReason(result.get("reason"));
                        messageLeaveApply.setLeave_type(result.get("leave_type"));

                        messageLeaveApplies.add(messageLeaveApply);
                    }
                    leaveItemAdapter= new SU_LeaveItemAdapter(messageLeaveApplies);
                    su_leave_list.setAdapter(leaveItemAdapter);
                }
            }
        };
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = this;
    }
}
