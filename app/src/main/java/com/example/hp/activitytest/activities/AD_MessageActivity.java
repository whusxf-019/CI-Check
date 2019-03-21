package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.MessageItem;
import com.example.hp.activitytest.util.AD_MessageAdapter;
import com.example.hp.activitytest.util.myApplication;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AD_MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static List<MessageItem> messageItems = new ArrayList<>();
    private static AD_MessageAdapter ad_messageAdapter = new AD_MessageAdapter(messageItems);

    public static MessageItem messageItem;//上次选中的message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_ad__message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.ad_message_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(ad_messageAdapter);

        MyHandler myHandler = new MyHandler(AD_MessageActivity.this);

        myApplication.myHandler = myHandler;

    }


    private void addAD_MessageItem(String type,String subType, String content, String addTime){
        MessageItem item = new MessageItem(type,subType, content, addTime, R.drawable.defaultcon);
        messageItems.add(0,item);

        //向列表中添加一项并且焦点放在第一项（list满之后再插入自动滚动到第一个列表项）
        ad_messageAdapter.notifyItemInserted(0);
        ad_messageAdapter.notifyItemRangeChanged(0,messageItems.size());
        Toast.makeText(getApplicationContext(),"==="+messageItems.get(0).getMessage(),Toast.LENGTH_LONG).show();
        recyclerView.scrollToPosition(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myApplication.myHandler = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = this;

        if(messageItems!=null){
            int index = this.messageItems.indexOf(messageItem);
            this.messageItems.remove(messageItem);
            messageItem = null;

            ad_messageAdapter.notifyItemRemoved(index);
        }


    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    public static class MyHandler extends Handler{
        private final WeakReference<AD_MessageActivity> messageActivityWeakReference;
        private MyHandler(AD_MessageActivity messageActivity){
            this.messageActivityWeakReference = new WeakReference<>(messageActivity);
        }
        public boolean isExist(){
            AD_MessageActivity myMessageActivity = messageActivityWeakReference.get();
            if(myMessageActivity!=null) {
                return true;
            }else {
                return false;
            }
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AD_MessageActivity myMessageActivity = messageActivityWeakReference.get();
            if(myMessageActivity!=null){
                if(msg.what==1) {
                    for( HashMap<String, String> message : (ArrayList<HashMap<String,String>>)msg.obj) {
                        String subType = message.get("subType");
                        String content = message.get("content");
                        String addTime = message.get("addTime");
                        String type = message.get("type");
                        myMessageActivity.addAD_MessageItem(type, subType, content, addTime);
                        System.out.println("get message");
                    }
                }
            }
        }
    }
}