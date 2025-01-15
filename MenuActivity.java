package com.example.fooddelivery.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fooddelivery.Customer.Adapter.MenuAdapter;
import com.example.fooddelivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView menuRecyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems = new ArrayList<>();
    private List<MenuItem> selectedItems = new ArrayList<>();
    private Button proceedToCartButton;
    private ProgressBar progressBar;
    private DatabaseReference menuRef;

    private static final int REQUEST_CODE_FOOD_DETAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize UI components
        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        proceedToCartButton = findViewById(R.id.proceedToCartButton);
        progressBar = findViewById(R.id.progressBar);

        // Set up RecyclerView
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(menuItems, this::onMenuItemClick);
        menuRecyclerView.setAdapter(menuAdapter);

        // Initialize Firebase reference
        menuRef = FirebaseDatabase.getInstance().getReference("menuItems");

        // Fetch menu items from Firebase
        fetchMenuItems();

        // Set up button click listener
        proceedToCartButton.setOnClickListener(view -> proceedToCart());
    }

    private void fetchMenuItems() {
        progressBar.setVisibility(View.VISIBLE);
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = itemSnapshot.getValue(MenuItem.class);
                    menuItems.add(menuItem);
                }
                menuAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MenuActivity.this, "Failed to load menu items", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void onMenuItemClick(MenuItem menuItem) {
        Intent intent = new Intent(MenuActivity.this, FoodDetailActivity.class);
        intent.putExtra("menuItem", menuItem);
        startActivityForResult(intent, REQUEST_CODE_FOOD_DETAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOOD_DETAIL && resultCode == RESULT_OK && data != null) {
            MenuItem menuItem = (MenuItem) data.getSerializableExtra("menuItem");
            if (menuItem != null) {
                selectedItems.add(menuItem);
            }
        }
    }
    private void proceedToCart() {
        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Please add items to the cart", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MenuActivity.this, CartActivity.class);
        intent.putExtra("selectedItems", new ArrayList<>(selectedItems));
        startActivity(intent);
    }
}
