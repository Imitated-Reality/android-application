package com.example.kruti.imitatedreality;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class IP extends AppCompatActivity {

    CircleImageView civ;
    EditText ip,port;
    Context t;
    Intent intent;
    String webpage;
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
                new LongOperation().execute("");
            }
        });
        t = this;
        intent = new Intent(IP.this, ViewStreaming.class);
        webpage = "";
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

        if (matcher.matches() && port.getText().toString() != "" )
        {
            if(webpage.equals("")){
                Toast toast= Toast.makeText(IP.this,"Host Unreachable" + webpage,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,600);
                toast.show();
            }
            else {
                intent.putExtra("ip", ip.getText().toString());
                intent.putExtra("port", port.getText().toString());
                startActivity(intent);
            }
        }
        else
        {
            Toast toast= Toast.makeText(IP.this,"Invalid IP",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,600);
            toast.show();
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        String ipAddr, portAddr;

        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = new URL("http://"+ipAddr.trim()+":"+portAddr.trim()+"/ping");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                InputStream is = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String webPage = "",data="";

                while ((data = reader.readLine()) != null){
                    webPage += data + "\n";
                }
                webpage = webPage;
                //Toast.makeText(t, webPage, Toast.LENGTH_LONG).show();
            }catch (Exception e){
                //Toast.makeText(t, e.toString(), Toast.LENGTH_LONG).show();
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {
            varifyIP();
        }

        @Override
        protected void onPreExecute() {
            ipAddr = ip.getText().toString();
            portAddr = port.getText().toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
