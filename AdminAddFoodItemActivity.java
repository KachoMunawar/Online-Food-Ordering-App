package com.example.adminapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AdminAddFoodItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText foodNameEditText, foodPriceEditText, foodDescriptionEditText;
    private ImageView foodImageView;
    private Button selectImageButton, addFoodItemButton;
    private Uri imageUri;

    private DatabaseReference menuItemsRef;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_food_item);

        // Initialize UI components
        foodNameEditText = findViewById(R.id.foodNameEditText);
        foodPriceEditText = findViewById(R.id.foodPriceEditText);
        foodDescriptionEditText = findViewById(R.id.foodDescriptionEditText);
        foodImageView = findViewById(R.id.foodImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        addFoodItemButton = findViewById(R.id.addFoodItemButton);

        // Initialize Firebase references
        menuItemsRef = FirebaseDatabase.getInstance().getReference("menuItems");
        storageRef = FirebaseStorage.getInstance().getReference("menuImages");

        // Set up button click listeners
        selectImageButton.setOnClickListener(view -> openFileChooser());
        addFoodItemButton.setOnClickListener(view -> uploadImageAndSaveFoodItem());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            foodImageView.setImageURI(imageUri);
        }
    }

    private void uploadImageAndSaveFoodItem() {
        if (imageUri != null) {
            String fileName = UUID.randomUUID().toString();
            StorageReference fileReference = storageRef.child(fileName);

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveFoodItem(imageUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(AdminAddFoodItemActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFoodItem(String imageUrl) {
        String name = foodNameEditText.getText().toString().trim();
        double price = Double.parseDouble(foodPriceEditText.getText().toString().trim());
        String description = foodDescriptionEditText.getText().toString().trim();

        if (name.isEmpty() || price <= 0 || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = menuItemsRef.push().getKey();
        MenuItem menuItem = new MenuItem(id, name, price, description, imageUrl);

        menuItemsRef.child(id).setValue(menuItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminAddFoodItemActivity.this, "Food item added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AdminAddFoodItemActivity.this, "Failed to add food item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
