//package com.example.hp.activitytest.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//
//import com.example.hp.activitytest.R;
//import com.example.hp.activitytest.model.US_MessageItem;
//import com.example.hp.activitytest.util.US_MessageAdapter;
//import com.example.hp.activitytest.util.adminTimerTask;
//import com.example.hp.activitytest.util.myApplication;
//import com.example.hp.activitytest.util.userTimerTask;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Timer;
//
//public class US_MessageActivity extends AppCompatActivity {
//
//    private List<US_MessageItem> itemList = new ArrayList<>();
//    private Timer timer;
//    private Handler handler;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        myApplication.myActivity.add(this);
//        setContentView(R.layout.activity_us_message);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//
//        handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if(msg.what==1){
//                    HashMap<String,String> message = (HashMap<String,String>)msg.obj;
//                    String subType = message.get("subType");
//                    String content = message.get("content");
//                    String addTime = message.get("addTime");
//                    initUsMessage(subType, content, addTime, "group");
//                    System.out.println("get message");
//                }else if(msg.what==2){
//                    HashMap<String,String> message = (HashMap<String,String>)msg.obj;
//                    String subType = message.get("subType");
//                    String content = message.get("content");
//                    String addTime = message.get("addTime");
//                    initUsMessage(subType, content, addTime, "leave");
//                    System.out.println("get message");
//                }
//            }
//        };
//
//
//        handleMessage();
//    }
//
//    private void initUsMessage(String subType, String content, String addTime, String titleType) {
//        //TODO 新建线程定时从服务器获取消息,添加到itemList数组中
//        US_MessageItem item = new US_MessageItem(subType, content, addTime, R.drawable.defaultcon, titleType);
//        itemList.add(item);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.us_recycler_view);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        US_MessageAdapter adapter = new US_MessageAdapter(itemList);
//        recyclerView.setAdapter(adapter);
//    }
//
//    public void handleMessage(){
//        timer = new Timer();
//        timer.scheduleAtFixedRate(new userTimerTask(){
//
//            @Override
//            public void dealWithUserGroupMessage(HashMap<String,String> message){
//
//                Message msg = new Message();
//                msg.what=1;
//                msg.obj = message;
//                handler.sendMessage(msg);
//                userTimerTask myuserTimerTask = new userTimerTask();
//                myuserTimerTask.endDealWithUGM();
//
//            }
//
//            @Override
//            public void dealWithUserLeaveMessage(HashMap<String,String> message){
//                //doNothing();
//
//                Message msg = new Message();
//                msg.what = 2;
//                msg.obj = message;
//                handler.sendMessage(msg);
//                userTimerTask myuserTimerTask = new userTimerTask();
//                myuserTimerTask.endDealWithULM();
//            }
//        },0,1000*5);
//    }
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }
//}
