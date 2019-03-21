package com.example.hp.activitytest.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;

import org.w3c.dom.Text;

public class AD_GroupMember extends AppCompatActivity {

    private ImageView group_member_image;
    private TextView group_member_name;
    private TextView group_member_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad__group_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent fromIntent = getIntent();
        final StandardUser standardUser = (StandardUser)fromIntent.getSerializableExtra("GroupMember");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_ad_group_member_delete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AD_GroupMember.this);
                dialog.setTitle("删除");
                dialog.setMessage("确认删除"+standardUser.getName()+"？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       //修改数据库，group
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result=Administrator.remove("quit_group",standardUser.getId(),LoginActivity.myapp.administrator.getAu_id());
                            }
                        }).start();

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


        group_member_image = (ImageView) findViewById(R.id.group_member_image);
        group_member_name = (TextView) findViewById(R.id.group_member_name) ;
        group_member_account = (TextView) findViewById(R.id.group_member_account) ;

        Glide.with(AD_GroupMember.this).load(Uri.parse("https://b-ssl.duitang.com/uploads/item/201508/09/20150809005334_rxVJH.jpeg")).into(group_member_image);
        group_member_name.setText(standardUser.getName());
        group_member_account.setText(standardUser.getId());

    }
}
