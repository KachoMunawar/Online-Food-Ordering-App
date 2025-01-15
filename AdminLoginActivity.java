package com.example.adminapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAdmin();
            }
        });
    }

    private void loginAdmin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Authenticate admin
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(AdminLoginActivity.this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            if (isAdmin(user.getEmail())) {
                                Toast.makeText(AdminLoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                // Start admin dashboard
                                startActivity(new Intent(AdminLoginActivity.this, AdminDashBoardActivity.class));
                                finish();
                            } else {
                                Toast.makeText(AdminLoginActivity.this, "You are not authorized as an admin.", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                            }
                        }
                    } else {
                        // If sign in fails
                        Toast.makeText(AdminLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isAdmin(String email) {
        // Define a list of authorized admin emails
        String[] adminEmails = {"kachomunawar19@gmail.com"};
        for (String adminEmail : adminEmails) {
            if (adminEmail.equals(email)) {
                return true;
            }
        }
        return false;
    }
}
