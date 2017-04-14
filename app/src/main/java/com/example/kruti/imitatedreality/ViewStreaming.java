package com.example.kruti.imitatedreality;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
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
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ViewStreaming extends AppCompatActivity implements SensorEventListener {


    SensorManager mSensorManager;
    Sensor mSensorAccelerometer;
    Sensor mSensorMagnetometer;
    TextView lupdate;
    TextView rupdate;
    String ip, port;
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

        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        port = intent.getStringExtra("port");

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

        if (rotation==Surface.ROTATION_0) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }

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


        w1.loadUrl("http://"+ip+":"+port+"/cam1/");
        w2.loadUrl("http://"+ip+":"+port+"/cam2/");


    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        w1.loadUrl("http://"+ip+":"+port+"/cam1/");
        w2.loadUrl("http://"+ip+":"+port+"/cam2/");
        Toast t = Toast.makeText(this, "Connected", Toast.LENGTH_LONG);
        t.show();
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
        //changeActivity1();
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && rotation==Surface.ROTATION_0) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
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


        try{
            //if(isHostAvailable(ip, port)){
            w1.loadUrl("http://"+ip+":"+port+"/cam1/");
            w2.loadUrl("http://"+ip+":"+port+"/cam2/");
            Toast t = Toast.makeText(this, "Connected", Toast.LENGTH_LONG);
            t.show();
        }catch(Exception e){
            w1.loadUrl("file:///android_asset/CustomErrorHandling");
            w2.loadUrl("file:///android_asset/CustomErrorHandling");
            Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            t.show();
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
