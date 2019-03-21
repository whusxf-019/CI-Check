package com.example.hp.activitytest.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.util.Base64Util;
import com.example.hp.activitytest.util.CameraTool;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.myApplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class FaceIdentityActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    private ImageView picture;
    private Uri imageUri;
    private Handler myHandler;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_face_identity);


        Button takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView)findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(ContextCompat.checkSelfPermission(FaceIdentityActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(FaceIdentityActivity.this,new String[]{Manifest.permission.CAMERA},1);

                }else{
                    imageUri = CameraTool.takePhoto(FaceIdentityActivity.this,TAKE_PHOTO);

                }
//                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
//                try{
//                    if(outputImage.exists()){
//                        outputImage.delete();
//                    }
//                    outputImage.createNewFile();
//
//                }catch(IOException e){
//                    e.printStackTrace();
//                }
//                if(Build.VERSION.SDK_INT>=24){
//                    imageUri = FileProvider.getUriForFile(FaceIdentityActivity.this,"com.example.hp.activitytest.fileprovider",outputImage);
//                }else{
//                    imageUri = Uri.fromFile(outputImage);
//                }
//
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                startActivityForResult(intent,TAKE_PHOTO);
            }
        });
    }







    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    imageUri = CameraTool.takePhoto(FaceIdentityActivity.this,TAKE_PHOTO);
                }else{
                    Toast.makeText(this,"获取授权失败",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }


    }





    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){

        //注册handler处理message传递的服务器的返回结果

        myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 1){
                    if(msg.obj.toString().equals("true")){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(FaceIdentityActivity.this);
                        dialog.setTitle("录入成功");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("完成注册", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                Intent intent = new Intent(FaceIdentityActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }else if(msg.obj.toString().equals("false")){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(FaceIdentityActivity.this);
                        dialog.setTitle("录入失败");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                Intent intent = new Intent(FaceIdentityActivity.this,RegisterActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }else if(msg.obj.toString().equals("can not insert")){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(FaceIdentityActivity.this);
                        dialog.setTitle("无法插入数据库");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                Intent intent = new Intent(FaceIdentityActivity.this,RegisterActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }
                    else{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(FaceIdentityActivity.this);
                        dialog.setTitle("面部信息重复");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                Intent intent = new Intent(FaceIdentityActivity.this,RegisterActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }
                }
                else if(msg.what==4){
                    progressDialog.dismiss();
                }
            }
        };

        switch(requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                         //获得图片

                        picture.setImageBitmap(bitmap);


                        String image = Base64Util.bitmapToBase64(bitmap);//bitmap是拍照之后获得的图片

                        Intent intent1 = getIntent();
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

                        nameValuePairs.add(new BasicNameValuePair("image",image));
                        nameValuePairs.add(new BasicNameValuePair("user_id",intent1.getStringExtra("userAccount")));  //user_id与数据库的id一致
                        nameValuePairs.add(new BasicNameValuePair("group_id","001"));//group_id与数据库的id一致

                        //创建新线程
                        AddFaceInfoToServlet addFaceInfoToServlet = new AddFaceInfoToServlet();
                        addFaceInfoToServlet.setName(nameValuePairs,this.myHandler,intent1.getStringExtra("userAccount"),intent1.getStringExtra("name"),intent1.getStringExtra("psw"));
                        new Thread(addFaceInfoToServlet).start();

                        //开始转转转
                        progressDialog = new ProgressDialog(FaceIdentityActivity.this);
                        progressDialog.setTitle("注册");
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }

                }
                break;
            default:
                break;

        }


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


class AddFaceInfoToServlet implements Runnable
{
    private ArrayList<NameValuePair> nameValuePairs;
    private Handler handler;
    final static String url = "http://wonder.vipgz1.idcfengye.com/ddd/test";
    private String name;
    private String userAccount;
    private String psw;
    public void setName(ArrayList<NameValuePair> nameValuePairs,Handler handler,String userAccount,String userName,String psw)
    {
        this.nameValuePairs = nameValuePairs;
        this.handler = handler;
        this.name = userName;
        this.userAccount = userAccount;
        this.psw = psw;
    }
    public void run()
    {

        String result = TranslateMessage.sendpost1("http://wonder.vipgz1.idcfengye.com/ddd/AddController", nameValuePairs); //返回face_token, 如果注册失败返回0
        Message message = new Message();
        message.what = 1;
        if(result.equals("0")){
            message.obj = "false";

            //取消转转转
            Message msgJump = new Message();
            msgJump.what = 4;
            handler.sendMessage(msgJump);

            handler.sendMessage(message);
        }else if(result.equals("exist")){
            message.obj = "exist";

            //取消转转转
            Message msgJump = new Message();
            msgJump.what = 4;
            handler.sendMessage(msgJump);

            handler.sendMessage(message);
        }
        else{
            message.obj = "true";

            //完成注册，向表中加入数据
            HashMap<String,String> map = new HashMap<>();
            map.put("user","true");
            map.put("userAccount",userAccount);
            try {
                map.put("userName", URLEncoder.encode(name,"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            map.put("password", psw);
            map.put("type","signup_confirm");
            String result1 = TranslateMessage.sendpost(url,map);

            //取消转转转
            Message msgJump = new Message();
            msgJump.what = 4;
            handler.sendMessage(msgJump);
            if (result1.equals("true")){
                handler.sendMessage(message);
            }else{
                message.obj = "can not insert";
                handler.sendMessage(message);
            }
        }
    }

}