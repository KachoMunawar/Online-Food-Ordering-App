package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminDashBoardActivity extends AppCompatActivity {

    private Button viewFoodItemsButton;
    private Button manageOrdersButton;
    private Button manageUsersButton;
    private Button addfoodButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        viewFoodItemsButton = findViewById(R.id.viewFoodItemsButton);
        manageOrdersButton = findViewById(R.id.manageOrdersButton);
        manageUsersButton = findViewById(R.id.manageUsersButton);
        addfoodButton = findViewById(R.id.addfoodButton);

        viewFoodItemsButton.setOnClickListener(view ->
                startActivity(new Intent(AdminDashBoardActivity.this, AdminViewFoodItemsActivity.class))
        );

        manageOrdersButton.setOnClickListener(view -> {
            // Navigate to Manage Orders Activity
            startActivity(new Intent(AdminDashBoardActivity.this, OrderManagementActivity.class));
        });

        manageUsersButton.setOnClickListener(view -> {
            // Navigate to Manage Users Activity
            startActivity(new Intent(AdminDashBoardActivity.this, AdminManageUsersActivity.class));
        });
        if (isAdminUser()) {
            addfoodButton.setVisibility(View.VISIBLE);
            addfoodButton.setOnClickListener(view -> startActivity(new Intent(AdminDashBoardActivity.this, AdminAddFoodItemActivity.class)));
        } else {
            addfoodButton.setVisibility(View.GONE);
        }
    }

    private boolean isAdminUser() {
        return true; // Replace this with actual logic
    }

    public void updateuser(View view) {
        startActivity(new Intent(AdminDashBoardActivity.this, AdminUpdateUserActivity.class));
        finish();
    }

    public void deleteuser(View view) {
        startActivity(new Intent(AdminDashBoardActivity.this, AdminDeleteUserActivity.class));
        finish();
    }
}
