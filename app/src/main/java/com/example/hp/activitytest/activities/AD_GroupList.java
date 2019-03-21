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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.StackView;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.AD_MemberItemAdapter;
import com.example.hp.activitytest.util.ProductGridItemDecoration;
import com.example.hp.activitytest.util.myApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AD_GroupList extends AppCompatActivity{


    private List<StandardUser> standardUserList = new ArrayList<>();
    private RecyclerView groupList;

    private AD_MemberItemAdapter ad_memberItemAdapter;

    private Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_ad__group_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myHandler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1)
                {
                    if(standardUserList.size()>0)
                        standardUserList.clear();
                    ArrayList<HashMap<String,String>> results=(ArrayList<HashMap<String, String>>)msg.obj;
                    for(int i=0;i<results.size();i++)
                    {
                       HashMap<String,String> result=results.get(i);
                       standardUserList.add(new StandardUser(result.get("userAccount"),result.get("userName"),"@drawable/ic_action_tick"));
                    }
                    ad_memberItemAdapter = new AD_MemberItemAdapter(standardUserList);
                    groupList.setAdapter(ad_memberItemAdapter);
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_activity_ad_group_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AD_GroupList.this,AD_AddMember.class);
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //进入时直接获取小组成员
               String result=Administrator.getGroupMenber("check_me",LoginActivity.myapp.administrator.getAu_id());
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
                       hashMap.put("userName",jo.getString("userName"));
                       hashMap.put("userAccount",jo.getString("userAccount"));
                        hashMaps.add(hashMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg=new Message();
               msg.what=1;
               msg.obj=hashMaps;
               myHandler.sendMessage(msg);
            }
        }).start();

        //standardUserList.add(new StandardUser("wawa","@drawable/ic_action_tick"));
        groupList = (RecyclerView) findViewById(R.id.group_list);
        groupList.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL,false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position % 3 == 2 ? 2 : 1;
            }
        });
        groupList.setLayoutManager(gridLayoutManager);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.grid_spacing_small);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.grid_spacing_small);
        groupList.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

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
