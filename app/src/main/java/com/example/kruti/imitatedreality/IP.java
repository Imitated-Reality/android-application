package com.example.kruti.imitatedreality;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class IP extends AppCompatActivity {

    CircleImageView civ;
    EditText ip,port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        civ=(CircleImageView)findViewById(R.id.circlebutton);
        ip=(EditText)findViewById(R.id.ip);
        port=(EditText)findViewById(R.id.port);

        port.setFilters(new InputFilter[]{new InputFilterMinMax("0", "65535")});

        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varifyIP();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        ip.setText("");
        port.setText("");
    }


    public void varifyIP()
    {
        final Pattern IP_ADDRESS
                = Pattern.compile(
                "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                        + "|[1-9][0-9]|[0-9]))");
        Matcher matcher = IP_ADDRESS.matcher(ip.getText().toString());

        if (matcher.matches())
        {
            Intent intent = new Intent(IP.this, ViewStreaming.class);
            startActivity(intent);
        }
        else
        {
            Toast toast= Toast.makeText(IP.this,"Invalid IP",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,600);
            toast.show();
        }





        /*
        Intent intent = new Intent(IP.this, ViewStreaming.class);
        startActivity(intent);*/
    }




}
