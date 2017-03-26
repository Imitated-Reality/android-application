package com.example.kruti.imitatedreality;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShowStatusActivity extends AppCompatActivity {

    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_status);
        b1=(Button)findViewById(R.id.start);
        //b1.setEnabled(false);
        //b1.setAlpha(.5f);
    }

    public void startStreaming(View view)
    {
        Intent intent=new Intent(ShowStatusActivity.this,StartStreaming.class);
        startActivity(intent);
    }
}
