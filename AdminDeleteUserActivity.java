package com.example.adminapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminDeleteUserActivity extends AppCompatActivity {

    private EditText userIdEditText;
    private Button deleteUserButton;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_user);

        // Initialize UI components
        userIdEditText = findViewById(R.id.userIdEditText);
        deleteUserButton = findViewById(R.id.deleteUserButton);

        // Initialize Firebase reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Set up button click listener
        deleteUserButton.setOnClickListener(v -> deleteUser());
    }

    private void deleteUser() {
        String userId = userIdEditText.getText().toString().trim();

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "Please enter user ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Delete user information
        usersRef.child(userId).removeValue();

        Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
    }
}
