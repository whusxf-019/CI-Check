package com.example.hp.activitytest.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.example.hp.activitytest.R;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    //定位，显示初始信息，接近自己的位置；
    public LocationClient mLocationClient;
    private boolean isFirstLocation = true;

    private BaiduMap baiduMap;
    private MapView mapView;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    //通过点击地图得到的地址
    private String address;

    //点击地图出现的附近地址
    List<String> surround=new ArrayList<String>();

    private PoiSearch poiSearch;
    private PoiNearbySearchOption poiNearbySearchOption=new PoiNearbySearchOption();
    private OnGetPoiSearchResultListener onGetPoiSearchResultListener=new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            List<PoiInfo> addr=new ArrayList();
            addr.addAll(poiResult.getAllPoi());
            for(int i=0;i<addr.size();i++)
            {
                surround.add(addr.get(i).address);
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };//end OnGetPoiSearchResultListener


    // 创建地理编码检索实例
    private GeoCoder geoCoder = GeoCoder.newInstance();
    OnGetGeoCoderResultListener onGetGeoCoderResultListener=new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {

            }
            else {
                address = reverseGeoCodeResult.getAddress();//解析到的地址
            }
        }
    };//end OnGetGeoCoderResultListener

    private BaiduMap.OnMapClickListener listener=new BaiduMap.OnMapClickListener() {
        @SuppressLint("ResourceType")
        @Override
        public void onMapClick(final LatLng latLng) {
            baiduMap.clear();
            surround.clear();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //显示图标
                    MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
                    locationBuilder.latitude(latLng.latitude);
                    locationBuilder.longitude(latLng.longitude);
                    MyLocationData locationData = locationBuilder.build();
                    baiduMap.setMyLocationData(locationData);
                }
            }).start();//end thread

            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
            searchNearBy(latLng);


            arrayAdapter=new ArrayAdapter<String>(LocationActivity.this,android.R.layout.simple_list_item_1,surround);
            listView.setAdapter(arrayAdapter);
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi) {
            return false;
        }
    };// end  OnMapClickListener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_location);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener((BDLocationListener) new MyLocationListener());
        geoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
        mapView=(MapView) findViewById(R.id.locationMap);
        listView=(ListView)findViewById(R.id.surrond_map);
        baiduMap=mapView.getMap();
        baiduMap.setOnMapClickListener(listener);
        baiduMap.setMyLocationEnabled(true);
        listView.setOnItemClickListener(this);
        poiSearch=PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
        requestLocation();

    }

    /*将当前位置显示在地图上*/
    private void navigateTo(BDLocation location) {
        if (isFirstLocation) {
            com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(location.getLatitude(), location.getLongitude());
            com.baidu.mapapi.map.MapStatusUpdate update = com.baidu.mapapi.map.MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);
            update = com.baidu.mapapi.map.MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocation = false;
        }

    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void searchNearBy(LatLng latLng)
    {
        //配置周边
        poiNearbySearchOption.keyword("办公");
        poiNearbySearchOption.sortType(PoiSortType.distance_from_near_to_far);
        poiNearbySearchOption.location(latLng);
        poiNearbySearchOption.radius(200);
        poiSearch.searchNearby(poiNearbySearchOption);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            navigateTo(location);
            searchNearBy(new LatLng(location.getLatitude(),location.getLongitude()));
            arrayAdapter=new ArrayAdapter<String>(LocationActivity.this,android.R.layout.simple_list_item_1,surround);
            listView.setAdapter(arrayAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        geoCoder.destroy();
        poiSearch.destroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        address=listView.getItemAtPosition(i).toString();
        Intent intent=new Intent(LocationActivity.this,AD_SetTimeActivityStep1.class);
        intent.putExtra("location",address);
        setResult(RESULT_OK,intent);
        finish();
    }

}
