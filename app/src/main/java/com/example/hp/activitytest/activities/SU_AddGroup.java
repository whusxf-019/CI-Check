package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.AD_SearchItemAdapter;
import com.example.hp.activitytest.util.SU_GroupItemAdapter;
import com.example.hp.activitytest.util.SU_MemberItemAdapter;
import com.example.hp.activitytest.util.myApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SU_AddGroup extends AppCompatActivity {


    private SearchView mSearchView;
    private List<Administrator> groupList = new ArrayList<>();
    private SU_GroupItemAdapter su_groupItemAdapter;
    private RecyclerView addList;

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_su__add_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addList = (RecyclerView) findViewById(R.id.su_add_group_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        su_groupItemAdapter = new SU_GroupItemAdapter(groupList);
        addList.setLayoutManager(layoutManager);
        addList.setAdapter(su_groupItemAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_su_add_group, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_su_add_group_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setIconified(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("查找组名");

        myHandler();

        //监听器
        //搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }

        });
        //搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // public static  ArrayList<HashMap<String,String>> lookUpAdminInfo(String type,String au_id)
                        ArrayList<HashMap<String,String>> result= StandardUser.lookUpAdminInfo("see_admin_info",s);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                }).start();
                //获取数据库的List
                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(SU_AddGroup.this,1);
                addList.setLayoutManager(gridLayoutManager1);
                 su_groupItemAdapter = new SU_GroupItemAdapter(groupList);
                addList.setAdapter(su_groupItemAdapter);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    public void myHandler()
    {
        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1)
                {
                    if(groupList.size()>0)
                        groupList.clear();
                    ArrayList<HashMap<String,String>> results=(ArrayList<HashMap<String, String>>)msg.obj;
                    for(int i=0;i<results.size();i++)
                    {
                        HashMap<String,String> result=results.get(i);
                        groupList.add(new Administrator(result.get("adminAccount"),result.get("adminName")));
                    }
                    su_groupItemAdapter= new SU_GroupItemAdapter(groupList);
                    addList.setAdapter(su_groupItemAdapter);
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
