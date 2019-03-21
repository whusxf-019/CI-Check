package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.AD_TimeItem;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.MessageLeaveApply;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.AD_TimeItemAdapter;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.myApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AD extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Recycle View
    private static List<AD_TimeItem> timeList = new ArrayList<>();
    private AD_TimeItemAdapter adapter;
    private Handler myHandler;
    final static String url = "http://wonder.vipgz1.idcfengye.com/ddd/checkin";
    //--
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    //--


    public static List<AD_TimeItem> getTimeList() {
        return timeList;
    }

    public static void setTimeList(List<AD_TimeItem> timeList) {
        AD.timeList = timeList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myApplication.getInstance().startReceiveMessage();
        myApplication.getInstance().type = "admin";
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    AD.getTimeList().clear();
                    JSONObject a = null;
                    JSONArray b = null;
                    try {
                        a = new JSONObject(msg.obj.toString());
                        b = a.getJSONArray("result");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for(int i = 0 ; i < b.length() ; i++){
                        AD_TimeItem timeItem = null;
                        try {
                            timeItem = new AD_TimeItem( b.getJSONObject(i).get("setStartTime").toString(),b.getJSONObject(i).get("setEndTime").toString(),true,b.getJSONObject(i).get("check_day_for_week").toString(),b.getJSONObject(i).get("address").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AD.getTimeList().add(timeItem);

                    }
                    adapter.notifyDataSetChanged();


                }
            }
        };


        //获取管理员已经设置过的记录
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = Administrator.getTimeRecords("show_checktime_info","111");
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                myHandler.sendMessage(msg);
            }
        }).start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //刷新Recycle View
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeItem();
            }
        });
        //--



        //Recycle View
        initTimeItems();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AD_TimeItemAdapter(timeList);
        recyclerView.setAdapter(adapter);
        //--


        //跳转到新建页面
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_activity_ad_apply_confirm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(AD.this,AD_SetTimeActivityStep1.class);
                startActivity(intent2);
            }
        });
        //--

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ad_action_edit) {
            //adapter.
            //View view = LayoutInflater.from(this).inflate(R.layout.time_item,parent,false);
//            for (int i=0; i<timeList.size();i++){
//                //adapter.notifyItemRemoved();
//
//
//            }



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent=new Intent(AD.this,AD_GroupList.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Intent intent1 = new Intent(AD.this,AD_LeaveList.class);
            startActivity(intent1);

        } else if (id == R.id.nav_slideshow) {
            Intent intent2 = new Intent(AD.this,MessageActivity.class);
            startActivity(intent2);


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initTimeItems(){
        if(getIntent().getSerializableExtra("timeItem")!=null){
            timeList.add((AD_TimeItem) getIntent().getSerializableExtra("timeItem"));
            refreshTimeItem();
        }
    }

    //刷新
    private void refreshTimeItem(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    //--

}
