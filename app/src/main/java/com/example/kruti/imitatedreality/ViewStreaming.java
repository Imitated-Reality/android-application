package com.example.kruti.imitatedreality;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ClientCertRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.net.InetAddress;


public class ViewStreaming extends AppCompatActivity implements SensorEventListener {


    SensorManager mSensorManager;
    Sensor mSensorAccelerometer;
    Sensor mSensorMagnetometer;
    TextView lupdate;
    TextView rupdate;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private float[] mLastRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    WebView w1,w2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_streaming);
        lupdate = (TextView) findViewById(R.id.leftOrient);
        rupdate = (TextView) findViewById(R.id.rightOrient);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        mSensorMagnetometer = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
        mSensorManager.registerListener(this, mSensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorMagnetometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });




        w1=(WebView)findViewById(R.id.web1);
        w2=(WebView)findViewById(R.id.web2);


        w1.setInitialScale(1);
        w1.getSettings().setJavaScriptEnabled(true);
        w1.getSettings().setLoadWithOverviewMode(true);
        w1.getSettings().setUseWideViewPort(true);
        w1.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        w1.setScrollbarFadingEnabled(false);

        w2.setInitialScale(1);
        w2.getSettings().setJavaScriptEnabled(true);
        w2.getSettings().setLoadWithOverviewMode(true);
        w2.getSettings().setUseWideViewPort(true);
        w2.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        w2.setScrollbarFadingEnabled(false);


        w1.loadUrl("http://192.168.43.102:5000/cam1/");
        w2.loadUrl("http://192.168.43.102:5000/cam2/");


    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        delayedHide(100);
    }



    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;


        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;


        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }


    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        changeActivity1();
    }

    public void changeActivity1()
    {
        String rotation=getRotation(this);
        if(rotation=="portrait" || rotation=="reverse portrait")
        {
            Intent intent1=new Intent(ViewStreaming.this,StartStreaming.class);
            startActivity(intent1);
        }
    }

    public String getRotation(Context context){


        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                return "reverse landscape";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callWebView();
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        callWebView();
    }

    public void callWebView(){
        w1.getSettings().setJavaScriptEnabled(true);
        w2.getSettings().setJavaScriptEnabled(true);
        try{
            byte[] ipAddr = new byte[]{10, 80, 7, 85};
            if(InetAddress.getByAddress(ipAddr).isReachable(5)){
                w1.loadUrl("http://192.168.43.102:5000/cam1/");
                w2.loadUrl("http://192.168.43.102:5000/cam2/");
            }
            else{
                w1.loadUrl("file:///android_asset/CustomErrorHandling");
                w2.loadUrl("file:///android_asset/CustomErrorHandling");
            }
        }catch(Exception e){
            w1.loadUrl("file:///android_asset/CustomErrorHandling");
            w2.loadUrl("file:///android_asset/CustomErrorHandling");
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mSensorAccelerometer) {
            System.arraycopy(event.values, 0, mAccelerometerReading, 0, mAccelerometerReading.length);
        }
        else if (event.sensor == mSensorMagnetometer) {
            System.arraycopy(event.values, 0, mMagnetometerReading, 0, mMagnetometerReading.length);
        }
        mSensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        mLastRotationMatrix = mRotationMatrix;
        int x = (int)(mOrientationAngles[0]*180/3.14);
        if(x < 0) x = 360+x;

        String str = "x: " + x + (char) 0x00B0;
        lupdate.setText(str);
        rupdate.setText(str);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
