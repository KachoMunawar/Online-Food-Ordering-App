package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminManageUsersActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private UserAdapter userAdapter;
    private List<User> users = new ArrayList<>();
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_users);

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(users, this::updateUser, this::deleteUser);
        usersRecyclerView.setAdapter(userAdapter);
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        fetchUsers();
    }

    private void fetchUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    users.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                handleDatabaseError(error);
            }
        });
    }

    private void updateUser(User user) {
        // Code to show a dialog and update user information
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update User Information");
        View view = getLayoutInflater().inflate(R.layout.dialog_update_user, null);
        EditText nameEditText = view.findViewById(R.id.nameEditText);
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText roleEditText = view.findViewById(R.id.roleEditText);

        nameEditText.setText(user.getName());
        emailEditText.setText(user.getEmail());
        roleEditText.setText(user.getRole());

        builder.setView(view)
                .setPositiveButton("Update", (dialog, which) -> {
                    String name = nameEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();
                    String role = roleEditText.getText().toString().trim();

                    if (!name.isEmpty() && !email.isEmpty() && !role.isEmpty()) {
                        user.setName(name);
                        user.setEmail(email);
                        user.setRole(role);
                        usersRef.child(user.getId()).setValue(user)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AdminManageUsersActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                                        userAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(AdminManageUsersActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(AdminManageUsersActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteUser(User user) {
        usersRef.child(user.getId()).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminManageUsersActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                        users.remove(user);
                        userAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminManageUsersActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleDatabaseError(DatabaseError error) {
        // Log the error message
        Log.e("AdminManageUserActivity", "Database error: " + error.getMessage());

        // Display a Toast message to the user
        Toast.makeText(AdminManageUsersActivity.this, "Failed to load users: " + error.getMessage(), Toast.LENGTH_SHORT).show();

        // Display an AlertDialog to inform the user
        new AlertDialog.Builder(AdminManageUsersActivity.this)
                .setTitle("Error")
                .setMessage("Failed to load users. Please try again later.\n\nError: " + error.getMessage())
                .setPositiveButton("Retry", (dialog, which) -> {
                    // Retry fetching users
                    fetchUsers();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
