package com.example.adminapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUpdateUserActivity extends AppCompatActivity {

    private EditText userIdEditText, nameEditText, emailEditText;
    private Button updateUserButton;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_user_actvity);

        // Initialize UI components
        userIdEditText = findViewById(R.id.userIdEditText);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        updateUserButton = findViewById(R.id.updateUserButton);

        // Initialize Firebase reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Set up button click listener
        updateUserButton.setOnClickListener(v -> updateUser());
    }

    private void updateUser() {
        String userId = userIdEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update user information
        usersRef.child(userId).child("name").setValue(name);
        usersRef.child(userId).child("email").setValue(email);

        Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
    }
}
