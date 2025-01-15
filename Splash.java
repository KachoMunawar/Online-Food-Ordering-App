package com.example.fooddelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.Customer.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {
    RelativeLayout splash;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        splash=(RelativeLayout)findViewById(R.id.splash);
        splash.postDelayed(new Runnable() {
            @Override
            public void run() {


                if(isNetworkAvailable()){
                    //Mobile data is enabled and do whatever you want here
                    if(user==null) {
                        Intent i = new Intent(Splash.this, RegisterActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(Splash.this, WelcomeScreen.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    //Mobile data is disabled here
                    AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                    builder.setTitle("Error");
                    builder.setIcon(R.drawable.ic_warning_black_24dp);
                    builder.setMessage("No mobile data connection detected.\nPlease check network connection and visit again.");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }, 2000);
    }
    private boolean isNetworkAvailable(){
        boolean isNetworkAvailable = true;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();

          //  isNetworkAvailable = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isNetworkAvailable;
    }
}
