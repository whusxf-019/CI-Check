package com.example.hp.activitytest.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.AD_TimeItem;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.AD_TimeItemAdapter;
import com.example.hp.activitytest.util.SU_TimeItemAdapter;
import com.example.hp.activitytest.util.NotificationUtil;
import com.example.hp.activitytest.util.myApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.hp.activitytest.util.Img.getImg;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class SU extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long mExitTime;
    private boolean mIsExit;
    private FloatingActionButton fab_su;
    private ImageView nav_header_su_image;
    private TextView nav_header_su_name;
    private TextView nav_header_su_account;
    private MyHandler myHandler;
    private RecyclerView recyclerView;

    private Handler handler;

    //Recycle View
    private static List<AD_TimeItem> timeList = new ArrayList<>();
    private static SU_TimeItemAdapter adapter = new SU_TimeItemAdapter(timeList);
    private SwipeRefreshLayout swipeRefreshLayout;


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
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        myApplication.getInstance().startReceiveMessage();
        myHandler = new MyHandler(this);
        myApplication.suTimeHandler = myHandler;
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_su);


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1)
                {
                    HashMap<String,String> hashMap=(HashMap<String, String>)msg.obj;
                    nav_header_su_name.setText("姓名："+hashMap.get("userName"));
                    nav_header_su_account.setText("账号："+hashMap.get("userAccount"));
                }
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab_su = (FloatingActionButton) findViewById(R.id.fab_su);
        fab_su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SU.this, MapTestActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TODO 获取侧边栏的个人信息
        View headView = navigationView.getHeaderView(0);
        nav_header_su_image = (CircleImageView) headView.findViewById(R.id.nav_header_su_image);
        nav_header_su_name = (TextView) headView.findViewById(R.id.nav_header_su_name);
        nav_header_su_account = (TextView) headView.findViewById(R.id.nav_header_su_account);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> map = StandardUser.user_lookSelf(LoginActivity.myapp.standardUser.getId());
                Message msg = new Message();
                msg.what=1;
                msg.obj=map;
                handler.sendMessage(msg);
            }
        }).start();

        Glide.with(SU.this).load(Uri.parse(getImg(LoginActivity.myapp.standardUser.getId().charAt(LoginActivity.myapp.standardUser.getId().length()-1)))).into(nav_header_su_image);

        nav_header_su_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SU.this, SU_EditInfoActivity.class);
                intent.putExtra("isSU", true);
                startActivity(intent);

            }
        });
        //刷新Recycle View
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.su_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeItem();
            }
        });

        //Recycle View
        initTimeItems();
        recyclerView = (RecyclerView) findViewById(R.id.su_recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }

    //TODO 刷新
    private void refreshTimeItem() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
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

    //TODO 初始化
    private void initTimeItems(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = StandardUser.getTimeByUser("get_check_time_table",myApplication.getUserAccount());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                myHandler.sendMessage(msg);
            }
        }).start();
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
        getMenuInflater().inflate(R.menu.su, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.exit_group) {
//            StandardUser.exitGroup(myApplication.getUserAccount());
//            //推出分组
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.su_group_member) {
            if (StandardUser.isJoinGroup(myApplication.getUserAccount())) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.su_fragment, new SU_GroupList());
//                transaction.addToBackStack(null);
//                transaction.commit();
                startActivity(new Intent(SU.this, SU_GroupListNew.class));

            } else {
                Toast.makeText(SU.this, "你还未加入分组", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.su_add_group) {
            if (!StandardUser.isJoinGroup(myApplication.getUserAccount())) {
                Intent intent = new Intent(SU.this, SU_AddGroup.class);
                startActivity(intent);
            } else {
                Toast.makeText(SU.this, "你已经加入分组了", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.su_leave_list) {
            Intent intent = new Intent(SU.this, SU_LeaveList.class);
            startActivity(intent);

        } else if (id == R.id.su_leave_form) {
            if (StandardUser.isJoinGroup(myApplication.getUserAccount())) {
                Intent intent = new Intent(SU.this, LeaveFormActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SU.this, "你还未加入分组", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.su_message) {
            Intent intent2 = new Intent(SU.this, AD_MessageActivity.class);
            startActivity(intent2);

        } else if (id == R.id.su_daka_info) {

            startActivity(new Intent(SU.this, AD_DAKAInfo.class));

        } else if (id == R.id.su_exit) {
            //TODO 退出的功能
            myApplication.exit(myApplication.ActivityExcept);
            Intent intent = new Intent(SU.this, FirstActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.su_report) {
            Intent intent2 = new Intent(SU.this, UserReportActivity.class);
            startActivity(intent2);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public static class MyHandler extends Handler{
        private final WeakReference<SU> messageActivityWeakReference;
        private MyHandler(SU su){
            this.messageActivityWeakReference = new WeakReference<>(su);
        }
        public boolean isExist(){
            SU su = messageActivityWeakReference.get();
            if(su!=null) {
                return true;
            }else {
                return false;
            }
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SU su = messageActivityWeakReference.get();
            if (su != null) {

                if (msg.what == 1) {
                    timeList.clear();
                    JSONObject a = null;
                    JSONArray b = null;
                    try {
                        a = new JSONObject(msg.obj.toString());
                        b = a.getJSONArray("result");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < b.length(); i++) {
                        AD_TimeItem timeItem = null;
                        try {
                            timeItem = new AD_TimeItem(b.getJSONObject(i).get("setStartTime").toString(), b.getJSONObject(i).get("setEndTime").toString(), true, b.getJSONObject(i).get("check_day_for_week").toString(), b.getJSONObject(i).get("address").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        timeList.add(timeItem);

                    }

                    adapter.notifyDataSetChanged();
                    su.swipeRefreshLayout.setRefreshing(false);
                }
                else if(msg.what==2){
                    if(SU_TimeItemAdapter.cardView!=null){
                        su.swipeRefreshLayout.invalidate();
//                        SU_TimeItemAdapter.cardView.setCardBackgroundColor(Color.RED);
                        for(SU_TimeItemAdapter.items ar:SU_TimeItemAdapter.arrayList){

                            Date date = (Date)msg.obj;
                            Date date2 = ar.startTime;
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            String d1 = sdf.format(date);
                            String d2 = sdf.format(date2);
                            if(d1.equals(d2)){
                                ar.cardView.setCardBackgroundColor(0xFFFFFF);
                            }
                        }
                    }
                }
                else if(msg.what==3){
                    if(SU_TimeItemAdapter.cardView!=null){
                        su.swipeRefreshLayout.invalidate();
                        for(SU_TimeItemAdapter.items ar:SU_TimeItemAdapter.arrayList){
                            if(ar.startTime==(Date) msg.obj){
                                ar.cardView.setCardBackgroundColor(Color.WHITE);
                            }
                        }
 //                       SU_TimeItemAdapter.cardView.setCardBackgroundColor(Color.WHITE);
                    }
                }

            }
        }
    }


}
