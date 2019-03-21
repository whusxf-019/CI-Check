package com.example.hp.activitytest.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.ProductGridItemDecoration;
import com.example.hp.activitytest.util.SU_MemberItemAdapter;
import com.example.hp.activitytest.util.myApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SU_GroupListNew extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SU_MemberItemAdapter su_memberItemAdapter;
    private List<StandardUser> standardUserList = new ArrayList<>();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_su__group_list_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_su_group_list_new = (FloatingActionButton) findViewById(R.id.fab_su_group_list_new);
        fab_su_group_list_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                AlertDialog.Builder dialog = new AlertDialog.Builder(SU_GroupListNew.this);
                dialog.setTitle("确认退出？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StandardUser.exitGroup(myApplication.getUserAccount());
                        startActivity(new Intent(SU_GroupListNew.this,SU.class));
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();

            }
        });

        recyclerView =(RecyclerView)findViewById(R.id.su_group_list_new);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL,false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position % 3 == 2 ? 2 : 1;
            }
        });
        int largePadding = getResources().getDimensionPixelSize(R.dimen.grid_spacing_small);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding,smallPadding));

        myHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<HashMap<String,String>> results=StandardUser.getWorkmate("check_me_by_user",LoginActivity.myapp.standardUser.getId());
                Message msg=new Message();
                msg.what=1;
                msg.obj=results;
                handler.sendMessage(msg);
            }
        }).start();

        recyclerView.setLayoutManager(layoutManager);


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
                    if(standardUserList.size()>0)
                        standardUserList.clear();
                    ArrayList<HashMap<String,String>> results=(ArrayList<HashMap<String, String>>)msg.obj;
                    for(int i=0;i<results.size();i++)
                    {
                        HashMap<String,String> result=results.get(i);
                        standardUserList.add(new StandardUser(result.get("userAccount"),result.get("userName"),"@drawable/ic_action_tick"));
                    }
                    su_memberItemAdapter= new SU_MemberItemAdapter(standardUserList);
                    recyclerView.setAdapter(su_memberItemAdapter);
                }
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = this;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
