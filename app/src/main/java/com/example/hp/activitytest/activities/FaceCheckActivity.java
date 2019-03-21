package com.example.hp.activitytest.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.Base64Util;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.myApplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("deprecation")
public class FaceCheckActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback{
    private static final String TAG = "FaceCheckActivity";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView mView;
    private FaceTask mFaceTask;
    private ProgressDialog progressDialog;
    private static int j =0;//与人脸识别的最长等待时间相关的变量
    private final int longestTime = 5;
    public static Boolean d = true;
    public static Boolean c = true;//dc相当于两把锁，防止一些奇怪的事发生
    @Override
    // 创建Activity时执行的动作
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_face_check);

        mView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = mView.getHolder();
        mHolder.addCallback(this);
        new Thread(new ScanThread()).start();
    }



    @Override
    // apk暂停时执行的动作：把相机关闭，避免占用导致其他应用无法使用相机
    protected void onPause() {
        super.onPause();

        mCamera.setPreviewCallback(null);

        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    // 恢复apk时执行的动作
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = this;
        if (null!=mCamera){
            mCamera = getCameraInstance();
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
                mCamera.setPreviewCallback(this);
            } catch(IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }
    }


    // SurfaceHolder.Callback必须实现的方法
    public void surfaceCreated(SurfaceHolder holder){
        mCamera = getCameraInstance();
        mCamera.setPreviewCallback(this);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch(IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    // SurfaceHolder.Callback必须实现的方法
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        refreshCamera(); // 这一步是否多余？在以后复杂的使用场景下，此步骤是必须的。
        int rotation = getDisplayOrientation(); //获取当前窗口方向
        mCamera.setDisplayOrientation(rotation); //设定相机显示方向
    }

    // SurfaceHolder.Callback必须实现的方法
    public void surfaceDestroyed(SurfaceHolder holder){
        mHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    // === 以下是各种辅助函数 ===

    // 获取camera实例
    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(1);
            c.setPreviewCallback(this);
        } catch(Exception e){
            Log.d("TAG", "camera is not available");
        }
        return c;
    }

    // 获取当前窗口管理器显示方向
    private int getDisplayOrientation(){
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation){
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        Camera.CameraInfo camInfo =
                new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        // 这里其实还是不太懂：为什么要获取camInfo的方向呢？相当于相机标定？？
        int result = (camInfo.orientation - degrees + 360) % 360;

        return result;
    }

    // 刷新相机
    private void refreshCamera(){
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch(Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if(null != mFaceTask){
            switch(mFaceTask.getStatus()){
                case RUNNING:
                    return;
                case PENDING:
                    mFaceTask.cancel(false);
                    break;
            }
        }
        while(d) {
            d =false;
            mFaceTask = new FaceTask(bytes);
            mFaceTask.execute((Void) null);
        }
    }
    private class FaceTask extends AsyncTask<Void, Void, String> {

        private byte[] mData;
        //构造函数
        FaceTask(byte[] data){
            this.mData = data;
        }

        @Override
         synchronized protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Size size = mCamera.getParameters().getPreviewSize(); //获取预览大小
            final int w = size.width;  //宽度
            final int h = size.height;
            final YuvImage image = new YuvImage(mData, ImageFormat.NV21, w, h, null);
            ByteArrayOutputStream os = new ByteArrayOutputStream(mData.length);
            if(!image.compressToJpeg(new Rect(0, 0, w, h), 100, os)){
                return null;
            }
            byte[] tmp = os.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0,tmp.length);
            //TODO  自己定义的实时分析预览帧视频的算法
            String image2 = Base64Util.bitmapToBase64(bmp);//bitmap是拍照之后获得的图片
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("image",image2));
            nameValuePairs.add(new BasicNameValuePair("user_id", myApplication.getUserAccount()));  //user_id与数据库的id一致
            nameValuePairs.add(new BasicNameValuePair("group_id","001"));

            String result = TranslateMessage.sendpost1("http://wonder.vipgz1.idcfengye.com/ddd/SearchController", nameValuePairs);
            String result1 = null;
            if(result.equals("exist")){
                //注意，需要通知数据库修改数据
                result1 = StandardUser.checkin(myApplication.getUserAccount());

            }
            return result1;
        }

        @Override
        synchronized protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null&&s.equals("true")){
                Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_LONG).show();//修改
                finish();
            }
            if(j==longestTime){
                AlertDialog.Builder dialog = new AlertDialog.Builder(FaceCheckActivity.this);
                dialog.setTitle("超时,识别失败");
                dialog.setCancelable(false);
                dialog.setPositiveButton("继续打卡", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        j=0;//重置i，继续识别xx秒
                    }
                });
                dialog.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                    }
                });
                dialog.show();
            }
            j++;
            d =true;
            System.out.println(s);
        }

    }
    class ScanThread implements Runnable{

        public void run() {
            // TODO Auto-generated method stub
            while(!Thread.currentThread().isInterrupted()){
                try {
                    if(null != mCamera)
                    {
                        //myCamera.autoFocus(myAutoFocusCallback);
                        mCamera.setOneShotPreviewCallback(FaceCheckActivity.this);
                        Log.i("00", "setOneShotPreview...");
                    }
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }

        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}



