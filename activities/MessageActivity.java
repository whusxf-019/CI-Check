package com.example.hp.activitytest.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.MessageItem;
import com.example.hp.activitytest.util.MessageAdapter;
import com.example.hp.activitytest.util.adminTimerTask;
import com.example.hp.activitytest.util.myApplication;
import com.example.hp.activitytest.util.userTimerTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class MessageActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private List<MessageItem> itemList = new ArrayList<>();
    private Timer timer;
    private Handler handler;
    private RecyclerView recyclerView ;
    private MessageAdapter adapter;

//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        adapter = new MessageAdapter(itemList);//修改
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    HashMap<String,String> message = (HashMap<String,String>)msg.obj;
                    String subType = message.get("subType");
                    String content = message.get("content");
                    String addTime = message.get("addTime");
                    addAD_MessageItem(subType, content, addTime);
                    System.out.println("get message");
                }else if(msg.what==2){
                    HashMap<String,String> message = (HashMap<String,String>)msg.obj;
                    String subType = message.get("subType");
                    String content = message.get("content");
                    String addTime = message.get("addTime");
                    addAD_MessageItem(subType, content, addTime);
                    System.out.println("get message");
                }
            }
        };


      //  handleMessage();
    }

    private void addAD_MessageItem(String subType, String content, String addTime){
        MessageItem item = new MessageItem(subType, content, addTime, R.drawable.defaultcon);
        itemList.add(0,item);

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        MessageAdapter adapter = new MessageAdapter(itemList);
//        recyclerView.setAdapter(adapter);

        //向列表中添加一项并且焦点放在第一项（list满之后再插入自动滚动到第一个列表项）
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }

    public void handleMessage(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new adminTimerTask(){

            @Override
            public void dealWithAdminGroupMessage(HashMap<String,String> message){

                Message msg = new Message();
                msg.what=1;
                msg.obj=message;
                handler.sendMessage(msg);
                this.endDealWithAGM();//改动

            }

            @Override
            public void dealWithAdminLeaveMessage(HashMap<String,String> message){
                //doNothing();

                Message msg = new Message();
                msg.what=2;
                msg.obj=message;
                handler.sendMessage(msg);
                this.endDealWithALM();//改动

            }
        },0,1000*5);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();

    }

    @Override
    protected void onResume() {
        super.onResume();
        handleMessage();
    }
}
