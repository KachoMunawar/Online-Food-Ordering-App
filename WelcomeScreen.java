package com.example.fooddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fooddelivery.Customer.LoginActivity;
import com.example.fooddelivery.Customer.RegisterActivity;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_sreen);


    }

    public void register(View view) {
        startActivity(new Intent(WelcomeScreen.this, RegisterActivity.class));
        finish();
    }


    public void onclicklogin(View view) {
        startActivity(new Intent(WelcomeScreen.this, LoginActivity.class));
        finish();
    }
}