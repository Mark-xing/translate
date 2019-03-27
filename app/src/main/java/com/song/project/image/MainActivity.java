package com.song.project.image;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.iflytek.cloud.SpeechUtility;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.song.project.bean.EnTTSUtils;
import com.song.project.bean.EnglishResultBean;
import com.song.project.bean.PlantBean;
import com.song.project.bean.TTSutil;
import com.song.project.demo.TransApi;

import org.apache.log4j.chainsaw.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.song.project.bean.PlantBean.parsearray;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    TextView tv_click;
    ImageButton bt_cream;
    Button bt_all;
    byte[] bt;
    ImageView iv;
    CameraManager cameraManager;
    int CAMERA_RESULT_CODE = 1; 
    AipImageClassify client;
    TextView tv_title,tv_English;
    ImageView c_pron,e_pron;
    Button bt_food,bt_palnt,bt_an,bt_car,bt_logo;
    Uri fileUri;  String access_token,imgName,TAG ="123456";
    String AppId = "15415203";
    String ApiKey = "LHKHPj7squ1N6DKFuY6rHrqn";
    int flag=1;
    private TTSutil ttSutil;
    private EnTTSUtils enTTSUtils;
    String SecretKey = "lhEGD8zFpAx5xQZr3QGjOTWDw8Nc1Dcz";
     //百度翻译使用的两个密钥
    private static final String APP_ID = "20190115000257009";
    private static final String SECURITY_KEY = "rsGPj8ita3nrR6M3ggsO";


//    ==================================
//    public LocationClient mLocationClient;
//    private TextView positionText;


    private DrawerLayout MDrawerLayout;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mLocationClient=new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new MainActivity.MyLocationListener());

        setContentView(R.layout.activity_main);
        //?????????
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        SpeechUtility.createUtility(MainActivity.this,"appid=5c3d66a4");
        //找到控件id
//        findViewById(R.id.bt_anmial).setOnClickListener(this);
//        findViewById(R.id.bt_car).setOnClickListener(this);
//        findViewById(R.id.bt_food).setOnClickListener(this);
//        findViewById(R.id.bt_logo).setOnClickListener(this);
//        findViewById(R.id.bt_plant).setOnClickListener(this);
//        bt_food=findViewById(R.id.bt_food);
//        bt_palnt=findViewById(R.id.bt_plant);
//        bt_an = findViewById(R.id.bt_anmial);
//        bt_car = findViewById(R.id.bt_car);
//        bt_logo = findViewById(R.id.bt_logo);

        bt_cream = findViewById(R.id.bt_cream);
        iv = findViewById(R.id.iv);  //图片
        tv_title = findViewById(R.id.tv_title);
        tv_English = findViewById(R.id.tv_english);
        c_pron=findViewById(R.id.c_pronunciation);
        e_pron=findViewById(R.id.e_pronunciation);
        //设置监听器
        tv_title.setOnClickListener(this);
        tv_English.setOnClickListener(this);
        c_pron.setOnClickListener(this);
        e_pron.setOnClickListener(this);

        long t2=System.currentTimeMillis();
        imgName = String.valueOf(t2)+".jpg";
        ttSutil =new  TTSutil(MainActivity.this);
        enTTSUtils =new EnTTSUtils(MainActivity.this);

        bt_cream.setOnClickListener(onClickListener);
        fileUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory("").getPath()+imgName));


//        ========================
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        //浮动按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                //可以交互Snackbar
                Snackbar.make(view, "启动位置检索", Snackbar.LENGTH_LONG)
                        .setAction("进入", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=null;
                                intent=new Intent(MainActivity.this,travelActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

//        ActionBar设置
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setHomeAsUpIndicator(R.drawable.head);
        }
        //滑动菜单控件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationView navigationView_1 = (NavigationView) findViewById(R.id.nav_right_view);
        navigationView_1.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //加载main.xml文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    //处理navigation中item的点击事件
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            drawer.closeDrawer(GravityCompat.START);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            drawer.closeDrawer(GravityCompat.START);
            //图片
        } else if (id == R.id.nav_slideshow) {
            drawer.closeDrawer(GravityCompat.START);
            //视频
        } else if (id == R.id.nav_manage) {
            drawer.closeDrawer(GravityCompat.START);
            //工具
        } else if (id == R.id.nav_share) {
            drawer.closeDrawer(GravityCompat.START);
            //分享
        } else if (id == R.id.nav_send) {
            drawer.closeDrawer(GravityCompat.START);
            //发送
        }

        switch (id){
            case R.id.anmial:
                flag =3;
                openSysCamera();
                break;
            case R.id.food:
                flag =5;
                openSysCamera();
                break;
            case R.id.plant:
                flag =1;
                openSysCamera();
                break;
            case R.id.logo:
                flag =4;
                openSysCamera();
                break;
            case R.id.car:
                flag =2;
                openSysCamera();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


//    ==========================================
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttSutil.destory();
        enTTSUtils.destory();
    }

    /**
     * 打开系统相机
     */
    private void openSysCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        //openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
        startActivityForResult(cameraIntent, 1);
    }
    private void Auth() {
        OkGo.<String>get("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=你的应用key&client_secret=你的应用secret")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            access_token = jsonObject.getString("access_token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }
    //按键点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.bt_anmial:
//               flag =3;
////                bt_an.setBackgroundColor(Color.BLUE);
////                bt_logo.setBackgroundColor(505500);
////                bt_food.setBackgroundColor(505500);
////                bt_palnt.setBackgroundColor(505500);
////                bt_car.setBackgroundColor(505500);
//                break;
//            case R.id.bt_car:
////                bt_car.setBackgroundColor(Color.BLUE);
////                bt_logo.setBackgroundColor(505500);
////                bt_food.setBackgroundColor(505500);
////                bt_an.setBackgroundColor(505500);
////                bt_palnt.setBackgroundColor(505500);
//                flag =2;
//                break;
//            case R.id.bt_logo:
////                bt_logo.setBackgroundColor(Color.BLUE);
////                bt_palnt.setBackgroundColor(505500);
////                bt_food.setBackgroundColor(505500);
////                bt_an.setBackgroundColor(505500);
////                bt_car.setBackgroundColor(505500);
//                flag=4;
//                break;
//            case R.id.bt_food:
////                bt_food.setBackgroundColor(Color.BLUE);
////                bt_logo.setBackgroundColor(505500);
////                bt_palnt.setBackgroundColor(505500);
////                bt_an.setBackgroundColor(505500);
////                bt_car.setBackgroundColor(505500);
//                flag =5;
//                break;
//            case R.id.bt_plant:
////                bt_palnt.setBackgroundColor(Color.BLUE);
////                bt_logo.setBackgroundColor(505500);
////                bt_food.setBackgroundColor(505500);
////                bt_an.setBackgroundColor(505500);
////                bt_car.setBackgroundColor(505500);
//                flag =1;
//                break;
            case R.id.e_pronunciation:
                String t = tv_English.getText().toString();
                enTTSUtils.play(MainActivity.this,t);
                break;
            case R.id.c_pronunciation:
                String m = tv_title.getText().toString();
                ttSutil.play(MainActivity.this,m);
                break;
        }
    }

    //点击拍照功能获得允许

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_cream:
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this
                                , new String[]{Manifest.permission.CAMERA
                                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        ,Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                                        Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }else{
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.END);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1) {
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                //  fileUri = data.getData();
                bt = getBitmapByte(bm);
                iv.setImageBitmap(bm);
                client = new AipImageClassify(AppId,ApiKey,SecretKey);
                new Thread(networkTask).start();
                //;
                }
                if(requestCode == 2){
                    Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    try {
                        Log.i(TAG, "onActivityResult: "+fileUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                        iv.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            HashMap<String,String> options=new HashMap<String, String>();
            JSONObject res =new JSONObject() ;
            if(flag ==1){//植物
                options.put("baike_num", "5");
                res = client.plantDetect(bt,options);
            }else if(flag ==2){//car
                options.put("top_num", "3");
                options.put("baike_num", "5");
                res = client.carDetect(bt, options);
            }else if(flag == 3){//动物
                options.put("top_num", "3");
                options.put("baike_num", "5");
                res = client.animalDetect(bt, options);
            }else if(flag ==4){//logo
                options.put("custom_lib", "false");
                res = client.logoSearch(bt, options);
            }else if(flag == 5){//food
                options.put("top_num", "3");
                options.put("filter_threshold", "0.7");
                options.put("baike_num", "5");
                res = client.dishDetect(bt, options);
            }
            Log.i(TAG, "run: "+res.toString());
            try {
                String result = res.getString("result");
                Log.i(TAG, "run: "+result);
                JSONArray array = new JSONArray(result);
                List<PlantBean> plantlist = parsearray(array);

                for(int i =0;i<plantlist.size();i++){
                 final  String  name = plantlist.get(i).getName();
                 //输出中文名字并发音
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_title.setText(name);
                            ttSutil.play(MainActivity.this,name);
                        }
                    });
                    TransApi api = new TransApi(APP_ID, SECURITY_KEY);
                   String en =api.getTransResult(name, "auto", "en");
                    JSONObject object = new JSONObject(en);
                    JSONArray array2 = new JSONArray(object.getString("trans_result"));
                    List<EnglishResultBean> bean = EnglishResultBean.parsearray(array2);
                    for(int j=0;j<bean.size();j++){
                        final String enname = bean.get(j).getDst();
                        //翻译成英文显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_English.setText(enname);
                               // ttSutil.play(MainActivity.this,name);
                            }
                        });
                    }
                    Log.i(TAG, "run: "+api.getTransResult(name, "auto", "en"));
                }


               // Log.i(TAG, "run: "+name+res.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    public byte[] getBitmapByte(Bitmap bitmap){   //将bitmap转化为byte[]类型也就是转化为二进制
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    private String file2String(File file) {
        byte[] buffer = new byte[0];
        try {
            FileInputStream inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }
}
