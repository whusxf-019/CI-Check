package com.example.hp.activitytest.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslateMessage {
    //url:服务器地址
    //parms:参数如 {"userAccount","ppp"}
    private static void getSize(Bitmap bitmap1) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        double ww= byteArrayOutputStream.toByteArray().length/1024;
        Log.i("压缩后的大小",ww+"KB");
    }

    public static Bitmap changeMapSize(Bitmap bit){
        double MaxSize = 1024;//图片允许最大空间
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte [] b=bos.toByteArray();//字节
        //字节转换为 KB
        double mid=b.length/1024;//KB
        Bitmap bitmap1=null;
        if(MaxSize<mid){
            //图片超过规定大小
            double n=mid/MaxSize; //允许压缩倍数
            double newWidth = bit.getWidth() /n;
            double newHeight = bit.getHeight() / n;
            Matrix matrix=new Matrix();
            matrix.postScale(((float)newWidth)/bit.getWidth(),((float)newHeight)/bit.getHeight());
            bitmap1=Bitmap.createBitmap(bit,0,0,bit.getWidth(),bit.getHeight(),matrix,true);
        }
        getSize(bitmap1);
        return bitmap1;
    }

    public static String sendpost0(String generalUrl, HashMap<String,Object> map)throws Exception{
        URL url = new URL(generalUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        String params = GsonUtils.toJson(map);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes("UTF-8"));
        out.flush();
        out.close();
        String encoding = "UTF-8";
        connection.connect();
        Map<String, List<String>> headers = connection.getHeaderFields();
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result = result + getLine + "\n";
        }
        in.close();
        System.err.println("result:" + result);
        return result;
    }
    public static String sendpost(String url,HashMap<String,String> parms){
        try {
            boolean firstTime = true;
            for (String key : parms.keySet()) {
                String value = parms.get(key);
                if(firstTime) {
                    url += "?" + key + "=" + value;
                    firstTime = false;
                }
                else url+="&"+key+"="+value;
            }
            URL myurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)myurl.openConnection();
            conn.setRequestMethod("POST");

            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            StringBuffer sb=new StringBuffer();
            //读取服务器返回的信息
            while((str=reader.readLine())!=null)
            {
                sb.append(str);
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "false";
        }
    }
    public static String sendpost1(String url, ArrayList<NameValuePair>nameValuePairs) {
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httpPost);

            String str = EntityUtils.toString(response.getEntity());
            return str;

        } catch (Exception e) {

            Log.e("log_tag", "Error in http connection " + e.toString());

        }
        return "";
    }
}
