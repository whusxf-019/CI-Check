package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.MessageLeaveApply;
import com.example.hp.activitytest.util.LeaveItemAdapter;
import com.example.hp.activitytest.util.myApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class AD_LeaveList extends AppCompatActivity {
    private Timer timer;
    private Handler handler;
    private String result;
    private RecyclerView leaveList;
    private LeaveItemAdapter leaveItemAdapter;
    private List<MessageLeaveApply> messageLeaveApplies = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_ad__leave_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        leaveList = (RecyclerView) findViewById(R.id.recycle_leave_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        leaveList.setLayoutManager(gridLayoutManager);
        leaveItemAdapter = new LeaveItemAdapter(messageLeaveApplies);
        leaveList.setAdapter(leaveItemAdapter);
    }



    @Override
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                //重新获取数据
                final List<MessageLeaveApply> poiItemList = new ArrayList<>();
                ArrayList<HashMap<String,String>> arrayList = Administrator.get_check_LeaveApply(LoginActivity.myapp.administrator.getAu_id());
                for(int i = 0;i<arrayList.size();i++){
                    MessageLeaveApply messageLeaveApply = new MessageLeaveApply(arrayList.get(i).get("userAccount"),arrayList.get(i).get("handTime"),arrayList.get(i).get("startTime")
                            ,arrayList.get(i).get("endTime"),arrayList.get(i).get("reason"),arrayList.get(i).get("state"),arrayList.get(i).get("leave_type"));

                    poiItemList.add(messageLeaveApply);
                }
                //更新列表
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyData(poiItemList);
                    }
                });
            }
        }).start();
//        Message msg = new Message();
//        msg.what = 1;
//        handler.sendMessage(msg);
    }
    public void newHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    leaveItemAdapter.notifyItemRangeChanged(0,messageLeaveApplies.size());
                }
            }
        };
    }
    public void notifyData(List<MessageLeaveApply> poiItemList) {
        if (poiItemList != null) {
            int previousSize = messageLeaveApplies.size();
            messageLeaveApplies.clear();
            leaveItemAdapter.notifyItemRangeRemoved(0, previousSize);
            messageLeaveApplies.addAll(poiItemList);
            leaveItemAdapter.notifyItemRangeInserted(0, poiItemList.size());
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

}