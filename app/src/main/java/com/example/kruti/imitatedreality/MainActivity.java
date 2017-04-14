package com.example.kruti.imitatedreality;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

//remeber this site http://imitatedreality.tk/
public class MainActivity extends AppCompatActivity {

    Button b;
    EditText username,password;
    String user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        b=(Button)findViewById(R.id.signinbutton);
        username=(EditText)findViewById(R.id.uname);
        password=(EditText)findViewById(R.id.pass);

        Intent intent=new Intent(MainActivity.this,IP.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        username.setText("");
        password.setText("");
    }

    //to check login and password if it is correct or not and
    // by using intent moving into another activity....
    public void checkStatus(View view)
    {

        Log.d("abc", "def");
        boolean flag=false;
        user=username.getText().toString();
        pass=password.getText().toString();

        if( user.length() == 0 )
        {
            username.setError("User name is required!");
            flag=true;
        }

        if( pass.length() == 0 )
        {
            password.setError("password is required!");
            flag=true;
        }

        //if its not the true one showing dialogue box... for error
        if(!(user.equals("irvr")&& pass.equals("irvr")))

        {

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

            dlgAlert.setMessage("wrong password or username");
            dlgAlert.setTitle("Error");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            flag=true;

        }

        //moving into another activity...
        if(!flag)
        {
            Intent intent=new Intent(MainActivity.this,IP.class);
            startActivity(intent);
        }
        Log.d("abcd", "defg");



    }
}
