package com.example.fooddelivery.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fooddelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerProfileActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, addressEditText;
    private Button saveButton, logoutButton;
    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        // Initialize UI components
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        saveButton = findViewById(R.id.saveButton);
        logoutButton=findViewById(R.id.logoutButton);
        progressBar=findViewById(R.id.progressBar);

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid());

        // Fetch and display user profile information
        fetchUserProfile();

        // Set up save button click listener
        saveButton.setOnClickListener(view -> saveUserProfile());
        logoutButton.setOnClickListener(view -> {
            auth.signOut();
            startActivity(new Intent(CustomerProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void fetchUserProfile() {
        progressBar.setVisibility(View.VISIBLE);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserProfile userProfile = snapshot.getValue(UserProfile.class);
                    if (userProfile != null) {
                        nameEditText.setText(userProfile.getName());
                        emailEditText.setText(userProfile.getEmail());
                        phoneEditText.setText(userProfile.getPhone());
                        addressEditText.setText(userProfile.getAddress());
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        UserProfile userProfile = new UserProfile(name, email, phone, address);
        userRef.setValue(userProfile).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(CustomerProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CustomerProfileActivity.this, HomePageActivity.class));
            } else {
                Toast.makeText(CustomerProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
