package com.example.hp.activitytest.activities;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.example.hp.activitytest.R;
import com.example.hp.activitytest.util.PersonalButton;
import com.example.hp.activitytest.util.myApplication;

import java.util.ArrayList;
import java.util.List;


public class FirstActivity extends AppCompatActivity {

    private PersonalButton bt_activity_first_login;
    private PersonalButton bt_activity_first_register;
    private RelativeLayout rlContent;
    private RelativeLayout r2Content;
    private Handler handler;
    private Animator animator;
    //所需权限
    private List<String> permissionList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.first_layout);
        bt_activity_first_login=(PersonalButton) findViewById(R.id.bt_activity_first_login);
        bt_activity_first_register = (PersonalButton) findViewById(R.id.bt_activity_first_register);
        bt_activity_first_login.setText("登录");
        bt_activity_first_register.setText("注册");
        rlContent=findViewById(R.id.rl_content1);
        r2Content = findViewById(R.id.rl_content2);

        rlContent.getBackground().setAlpha(0);
        r2Content.getBackground().setAlpha(0);
        handler=new Handler();
        //如果没连网，显示未联网
        if(isConnectInternet()==false)
        {
            Toast.makeText(this,"网络异常",Toast.LENGTH_LONG).show();
        }

        //开始界面获取所有的权限
        if(ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if(ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        if(ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.CAMERA);

        if(!permissionList.isEmpty())
        {
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(FirstActivity.this,permissions,1);
        }

        bt_activity_first_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_activity_first_login.startAnim();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //跳转
                        gotoNew1();
                    }
                },500);
            }
        });
        bt_activity_first_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_activity_first_register.startAnim();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //跳转
                        gotoNew2();
                    }
                },500);
            }
        });
    }

    private void gotoNew1() {
        bt_activity_first_login.gotoNew();
        final Intent intent=new Intent(this,LoginActivity.class);
        int xc=(bt_activity_first_login.getLeft()+bt_activity_first_login.getRight())/2;
        int yc=(bt_activity_first_login.getTop()+bt_activity_first_login.getBottom())/2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator= ViewAnimationUtils.createCircularReveal(rlContent,xc,yc,0,1111);
        }
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                    }
                },500);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
        rlContent.getBackground().setAlpha(255);

    }
    private void gotoNew2() {
        bt_activity_first_register.gotoNew();
        final Intent intent=new Intent(this,RegisterActivity.class);
        int xc=(bt_activity_first_register.getLeft()+bt_activity_first_register.getRight())/2;
        int yc=(bt_activity_first_register.getTop()+bt_activity_first_register.getBottom())/2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator= ViewAnimationUtils.createCircularReveal(r2Content,xc,yc,0,1111);
        }
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                    }
                },500);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        r2Content.getBackground().setAlpha(255);
    }

    @Override
    protected void onStop() {
        super.onStop();
        animator.cancel();
        rlContent.getBackground().setAlpha(0);
        r2Content.getBackground().setAlpha(0);
        bt_activity_first_login.regainLogin();
        bt_activity_first_register.regainRegister();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }
                break;
                default:
        }
    }

    //判断是否联网
    private boolean isConnectInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
