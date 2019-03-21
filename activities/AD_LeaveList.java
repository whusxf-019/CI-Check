package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.MessageItem;
import com.example.hp.activitytest.model.MessageLeaveApply;
import com.example.hp.activitytest.util.LeaveItemAdapter;
import com.example.hp.activitytest.util.MessageAdapter;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.adminTimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        setContentView(R.layout.activity_ad__leave_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        newHandler();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<HashMap<String,String>> arrayList = Administrator.get_check_LeaveApply(LoginActivity.myapp.administrator.getAu_id());
//                for(int i = 0;i<arrayList.size();i++){
//                    MessageLeaveApply messageLeaveApply = new MessageLeaveApply(arrayList.get(i).get("userAccount"),arrayList.get(i).get("handTime"),arrayList.get(i).get("startTime")
//                    ,arrayList.get(i).get("endTime"),arrayList.get(i).get("reason"),arrayList.get(i).get("state"),arrayList.get(i).get("leave_type"));
//                    messageLeaveApplies.add(messageLeaveApply);
//                }
//            }
//        }).start();
        leaveList = (RecyclerView) findViewById(R.id.recycle_leave_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        leaveList.setLayoutManager(gridLayoutManager);
        leaveItemAdapter = new LeaveItemAdapter(messageLeaveApplies);
        leaveList.setAdapter(leaveItemAdapter);
    }
//
//    private void getLeaveList(){
//        Intent intent = getIntent();
//        String au_id = intent.getSerializableExtra("au_id").toString();
//        String type = intent.getStringExtra("type").toString();
//
//        if(type.equals("confirm")){
//            result = Administrator.getLeaveApply("getLeaveApply", au_id);
//        }
//        else if(type.equals("record")){
//            result = Administrator.lookUpLeaveApply("lookUpLeaveApply", au_id);
//        }
//
//        JSONObject jsonObject = null;
//        JSONArray jsonArray = null;
//        try {
//            jsonObject = new JSONObject(result);
//            jsonArray = jsonObject.getJSONArray("result");
//
//            for (int i = 0; i < jsonArray.length(); i++){
//                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                MessageItem item = new MessageItem(jsonObject1.getString("leave_type"),
//                        jsonObject1.getString("reason"),
//                        jsonObject1.getString("startTime"), R.drawable.defaultcon);
//                itemList.add(item);
//
//            }
//
//            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_leave_list);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//            recyclerView.setLayoutManager(layoutManager);
//            MessageAdapter adapter = new MessageAdapter(itemList);
//            recyclerView.setAdapter(adapter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }


    @Override
    protected void onResume() {
        super.onResume();

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
}