package com.example.hp.activitytest.activities;

import android.database.Cursor;
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
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.AD_TimeItem;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.AD_MemberItemAdapter;
import com.example.hp.activitytest.util.AD_SearchItemAdapter;
import com.example.hp.activitytest.util.myApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//搜索查找
public class AD_AddMember extends AppCompatActivity {

    private SearchView mSearchView;
    private List<StandardUser> userList = new ArrayList<>();
    private AD_SearchItemAdapter ad_searchItemAdapter;
    private RecyclerView addList;
    private Handler myHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad__add_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_ad_add_member);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        addList = (RecyclerView) findViewById(R.id.ad_add_member_list);
        //standardUserList.clear();

        //处理从数据库获得的用户信息
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what==1)
                {
                    if(userList.size()>0)
                        userList.clear();
                    ArrayList<HashMap<String,String>> results=( ArrayList<HashMap<String,String>>) msg.obj;
                    for(int i=0;i<results.size();i++)
                    {
                        HashMap<String,String> result=results.get(i);
                        userList.add(new StandardUser(result.get("userAccount"),result.get("userName"),result.get("adminAccount"),result.get("adminName")));
                    }
                    ad_searchItemAdapter=new AD_SearchItemAdapter(userList);
                    addList.setAdapter(ad_searchItemAdapter);
                }

            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ad_add_member, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_ad_add_member_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setIconified(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("查找用户名/用户名");


        //监听器
        //搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
                return false;
            }
  
        });
        //搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Open", Toast.LENGTH_SHORT).show();
            }
        });
        //搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<HashMap<String,String>> result= Administrator.searchUser("see_user_info",s);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        myHandler.sendMessage(msg);
                    }
                }).start();
                //获取数据库的List
                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(AD_AddMember.this,1);
                addList.setLayoutManager(gridLayoutManager1);
                AD_SearchItemAdapter ad_searchItemAdapter = new AD_SearchItemAdapter(userList);
                addList.setAdapter(ad_searchItemAdapter);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Toast.makeText(getApplicationContext(), "Open"+s, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setAdapter(){

    }



}
