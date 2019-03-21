package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.hp.activitytest.R;

import java.util.ArrayList;
import java.util.List;

public class MapTestActivity extends AppCompatActivity  implements View.OnClickListener{

    public LocationClient mLocationClient;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private Button button;

    private boolean isFirstLocation = true;

    //保存“开始打卡”字符
    private String initStr;
    //当前位置
    private BDLocation curr_location;
    //判断是否在打卡范围，为了限制跳转页面无限循环过程
    private Boolean isUnder=false;
    //打卡地址
    private String address;
    //存取address经纬度
    private double latitude;
    private double longtitude;
    // 创建地理编码检索实例
    GeoCoder geoCoder = GeoCoder.newInstance();
    OnGetGeoCoderResultListener listener=new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MapTestActivity.this, "抱歉，未能找到结果",
                        Toast.LENGTH_LONG).show();
            } else {
                // 获取地理编码结果
                LatLng addressTemp = geoCodeResult.getLocation();
                latitude=addressTemp.latitude;
                longtitude=addressTemp.longitude;
            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationClient = new LocationClient(getApplicationContext());

        mLocationClient.registerLocationListener((BDLocationListener) new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_map_test);

        mMapView = (MapView) findViewById(R.id.bmapView);
        button=(Button)findViewById(R.id.btn_CheckIn) ;

        //按钮的初始信息
        initStr= button.getText().toString();

        //设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);

        /*获取百度map控件*/
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        //test
        address="武汉银泰创意城";
        geoCoder.geocode(new GeoCodeOption().city("").address(address));

        List<String> permissionList = new ArrayList<>();
        if (android.support.v4.content.ContextCompat.checkSelfPermission(MapTestActivity.this, android.Manifest.
                permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (android.support.v4.content.ContextCompat.checkSelfPermission(MapTestActivity.this, android.Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.
                    size()]);
            /*使用ActivityCompat 统一申请权限 */
            android.support.v4.app.ActivityCompat.requestPermissions(MapTestActivity.this, permissions, 1);
        }
        /*开始定位*/
        requestLocation();

    }

    private void requestLocation() {
        initLocation();
        /*开始定位*/
        mLocationClient.start();
    }

    /*设置2000ms更新一次坐标位置信息*/
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(2000);
        option.setCoorType("bd09ll");
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    /*将当前位置显示在地图上*/
    private void navigateTo(BDLocation location) {
        if (isFirstLocation) {
            com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(location.getLatitude(), location.getLongitude());
            com.baidu.mapapi.map.MapStatusUpdate update = com.baidu.mapapi.map.MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.animateMapStatus(update);
            update = com.baidu.mapapi.map.MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);

            LatLng llCircle = new LatLng(latitude,longtitude);
            OverlayOptions ooCircle = new CircleOptions().fillColor(0x4D40E0D0)
                    .center(llCircle).stroke(new com.baidu.mapapi.map.Stroke(5, 0x99FFFFFF))
                    .radius(800);
            mBaiduMap.addOverlay(ooCircle);

            isFirstLocation = false;
        }

        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }

    public class MyLocationListener implements BDLocationListener {
            @Override
            public void onReceiveLocation(BDLocation location) {

            curr_location=location;
            LatLng curr_Loc=new LatLng(curr_location.getLatitude(),curr_location.getLongitude());
            LatLng target_Loc=new LatLng(latitude,longtitude);
            double curr_distance= DistanceUtil. getDistance(curr_Loc,target_Loc);
            //当前时间
             long sysTime = System.currentTimeMillis();
             CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);
             button.setText(initStr+'\n'+sysTimeStr.toString());
            if(curr_distance<=800&&isUnder==false)
            {
                isUnder=true;
                Toast.makeText(MapTestActivity.this, "已在打卡范围",
                        Toast.LENGTH_LONG).show();
                //add your code here
                //监听时间和地址，条件为真时，设置按钮可用

            }
            navigateTo(location);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        geoCoder.destroy();
    }

    @Override
    public void onClick(View view) {
        //点击进入人脸识别界面，经当在打卡时间范围及在地址范围 才可用，否则无法点击
        //add your code here

    }

}