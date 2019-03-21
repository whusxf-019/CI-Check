package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.AD_TimeItem;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.MessageLeaveApply;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.AD_TimeItemAdapter;
import com.example.hp.activitytest.util.ProductGridItemDecoration;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.myApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.hp.activitytest.util.Img.getImg;

public class AD extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private long mExitTime;
    private boolean mIsExit;
    //Recycle View
    private static List<AD_TimeItem> timeList = new ArrayList<>();
    private static AD_TimeItemAdapter adapter = new AD_TimeItemAdapter(timeList);
    private static boolean isFirstTime = true;
    private Handler myHandler;
    final static String url = "http://wonder.vipgz1.idcfengye.com/ddd/checkin";
    //--
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    //--

    private ImageView nav_header_ad_image;
    TextView nav_header_ad_name;
    TextView nav_header_ad_account;


    public static List<AD_TimeItem> getTimeList() {
        return timeList;
    }

    public static void setTimeList(List<AD_TimeItem> timeList) {
        AD.timeList = timeList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出

                myApplication.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myApplication.myActivity.add(this);
        myApplication.getInstance().startReceiveMessage();
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


                }else if(msg.what==2){
                    HashMap<String,String> hashMap=(HashMap<String,String>)msg.obj;
                    nav_header_ad_name.setText("姓名："+hashMap.get("adminName"));
                    nav_header_ad_account.setText("账号："+hashMap.get("adminAccount"));
                    Glide.with(AD.this).load(Uri.parse(getImg(hashMap.get("adminAccount").charAt(hashMap.get("adminAccount").length()-1)))).into(nav_header_ad_image);
                }
            }
        };


        //获取管理员已经设置过的记录
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = Administrator.getTimeRecords("show_checktime_info",myApplication.getAdminAccount());
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
//        if(isFirstTime) {
//            initTimeItems();
//            isFirstTime = false;
//        }
        refreshTimeItem();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.grid_spacing_small);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.grid_spacing_small);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding,smallPadding));
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

        //TODO 获取侧边栏的个人信息！！


        String au_id;
        View headView = navigationView.getHeaderView(0);
        nav_header_ad_image = (CircleImageView) headView.findViewById(R.id.nav_header_ad_image);
        nav_header_ad_name = (TextView) headView.findViewById(R.id.nav_header_ad_name);
        nav_header_ad_account = (TextView) headView.findViewById(R.id.nav_header_ad_account);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> map = Administrator.admin_lookSelf(LoginActivity.myapp.administrator.getAu_id());
                Message msg = new Message();
                msg.what=2;
                msg.obj=map;
                myHandler.sendMessage(msg);
            }
        }).start();

        nav_header_ad_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AD.this,SU_EditInfoActivity.class);
                intent.putExtra("isSU",false);
                startActivity(intent);

            }
        });


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

        if (id == R.id.drawer_ad_group_info) {
            Intent intent=new Intent(AD.this,AD_GroupList.class);
            startActivity(intent);

        } else if (id == R.id.drawer_leave_list) {
            Intent intent1 = new Intent(AD.this,AD_LeaveList.class);
            startActivity(intent1);

        } else if (id == R.id.drawer_message) {
            Intent intent2 = new Intent(AD.this,AD_MessageActivity.class);
            startActivity(intent2);


        } else if (id == R.id.drawer_add_member) {
            Intent intent2 = new Intent(AD.this,AD_AddMember.class);
            startActivity(intent2);


        }
        else if (id == R.id.drawer_daka_info) {
            startActivity(new Intent(AD.this,AD_DAKAInfo.class));

        } else if (id == R.id.drawer_exit) {
            //TODO 写退出
            myApplication.exit(myApplication.ActivityExcept);
            Intent intent = new Intent(AD.this,FirstActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(id == R.id.drawer_report) {
            Intent intent2 = new Intent(AD.this,ManagerMainActivity.class);
            startActivity(intent2);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void initTimeItems(){
//        if(getIntent().getSerializableExtra("timeItem")!=null){
//            timeList.add((AD_TimeItem) getIntent().getSerializableExtra("timeItem"));
//            Toast.makeText(AD.this,"1111",Toast.LENGTH_SHORT).show();
//            refreshTimeItem();
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshTimeItem();
        myApplication.currentActivity = this;
    }

    //刷新
    private void refreshTimeItem(){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try{
//                    Thread.sleep(2000);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        adapter.notifyDataSetChanged();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                });
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                timeList.clear();
                String result = Administrator.getTimeRecords("show_checktime_info",myApplication.getAdminAccount());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                myHandler.sendMessage(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    //--

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("tag", "onNewINtent执行了");
        setIntent(intent);
        String ringName = intent.getStringExtra("ringName");
        Log.e("tag", ringName+"传过来的值");
    }

}
