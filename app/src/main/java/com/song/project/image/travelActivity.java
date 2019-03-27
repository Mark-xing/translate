package com.song.project.image;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

import java.util.ArrayList;
import java.util.List;

public class travelActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    private TextView positionText;
//    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
//        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_travel);
        positionText=(TextView)findViewById(R.id.position_text_view);
//        mapView=(MapView)findViewById(R.id.bmapView);
        List<String> permissionList =new ArrayList<>();
        if (ContextCompat.checkSelfPermission(travelActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED ){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(travelActivity.this,Manifest.permission.READ_PHONE_STATE)
                !=PackageManager.PERMISSION_GRANTED ) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
            if (ContextCompat.checkSelfPermission(travelActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    !=PackageManager.PERMISSION_GRANTED ){
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String [] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(travelActivity.this,permissions,1);
        }else {
            requestionLocation();
        }
    }
//    @Override
//    protected void onResume(){
//        super.onResume();
//        mapView.onResume();
//    }
//    @Override
//    protected void onPause(){
//        super.onPause();
//        mapView.onResume();
//    }
    private void requestionLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
         option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限",Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    requestionLocation();
                }else {
                        Toast.makeText(this,"发生未知错误",Toast.LENGTH_LONG).show();
                        finish();
                        }
                break;
                default:
        }
    }
    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            StringBuilder currentPosition =new StringBuilder();
            currentPosition.append("纬度:").append(location.getLatitude()).append("\n");
            currentPosition.append("经线:").append(location.getLongitude()).append("\n");
            currentPosition.append("国家:").append(location.getCountry()).append("\n");
            currentPosition.append("省:").append(location.getProvince()).append("\n");
            currentPosition.append("市:").append(location.getCity()).append("\n");
            currentPosition.append("区:").append(location.getDistrict()).append("\n");
            currentPosition.append("街道:").append(location.getStreet()).append("\n");
            currentPosition.append("定位方式：");
            if (location.getLocType()==BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if (location.getLocType()==BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络");
            }
//            else if (location.getLocType()==BDLocation.INDOOR_LOCATION_SOURCE_WIFI){
//                currentPosition.append("wifi");
//            }
            positionText.setText(currentPosition);

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
//        mapView.onDestroy();
    }
}
