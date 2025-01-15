package com.example.fooddelivery.Customer;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;

public class HomePageActivity extends AppCompatActivity {

    private Button menuButton;
    private Button cartButton;
    private Button profileButton;
    private Button orderButton;
    private Button HistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        menuButton = findViewById(R.id.menuButton);
        cartButton = findViewById(R.id.cartButton);
        profileButton = findViewById(R.id.profileButton);
        orderButton = findViewById(R.id.OrderButton);
        HistoryButton=findViewById(R.id.HistoryButton);

        menuButton.setOnClickListener(view -> startActivity(new Intent(HomePageActivity.this, MenuActivity.class)));
        cartButton.setOnClickListener(view -> startActivity(new Intent(HomePageActivity.this, CartActivity.class)));
        profileButton.setOnClickListener(view -> startActivity(new Intent(HomePageActivity.this, CustomerProfileActivity.class)));
        orderButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomePageActivity.this, CompleteOrderActivity.class);
         //   intent.putExtra("orders",order); // Order should be Serializable
            startActivity(intent);
        });
        HistoryButton.setOnClickListener(view -> startActivity(new Intent(HomePageActivity.this, OrderHistoryActivity.class)));


    }}
