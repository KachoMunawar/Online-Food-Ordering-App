package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUpdateFoodItemActivity extends AppCompatActivity {

    private EditText nameEditText, priceEditText;
    private Switch availabilitySwitch;  // New switch for availability
    private Button updateFoodItemButton;
    private DatabaseReference menuItemsRef;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_food_item);

        // Initialize UI components
        nameEditText = findViewById(R.id.nameEditText);
        priceEditText = findViewById(R.id.priceEditText);
        availabilitySwitch = findViewById(R.id.availabilitySwitch);  // Initialize the switch
        updateFoodItemButton = findViewById(R.id.updateFoodItemButton);

        // Initialize Firebase reference
        menuItemsRef = FirebaseDatabase.getInstance().getReference("menuItems");

        // Get the passed menu item
        menuItem = (MenuItem) getIntent().getSerializableExtra("menuItem");
        if (menuItem != null) {
            nameEditText.setText(menuItem.getName());
            priceEditText.setText(String.valueOf(menuItem.getPrice()));
            availabilitySwitch.setChecked(menuItem.isAvailable());  // Set the switch state
        }

        // Set up button click listener
        updateFoodItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFoodItem();
            }
        });
    }

    private void updateFoodItem() {
        String name = nameEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        boolean isAvailable = availabilitySwitch.isChecked();  // Get the availability state

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        menuItem.setName(name);
        menuItem.setPrice(Double.parseDouble(price));
        menuItem.setAvailable(isAvailable);  // Set the availability state

        menuItemsRef.child(menuItem.getId()).setValue(menuItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminUpdateFoodItemActivity.this, "Food item updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AdminUpdateFoodItemActivity.this, "Failed to update food item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
