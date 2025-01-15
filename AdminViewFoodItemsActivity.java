package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminViewFoodItemsActivity extends AppCompatActivity {

    private RecyclerView foodItemsRecyclerView;
    private FoodItemAdapter foodItemsAdapter;
    private List<MenuItem> foodItemList;

    private DatabaseReference menuItemsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_food_items);

        // Initialize RecyclerView
        foodItemsRecyclerView = findViewById(R.id.foodItemRecyclerView);
        foodItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodItemList = new ArrayList<>();
        foodItemsAdapter = new FoodItemAdapter(foodItemList, this::editFoodItem);
        foodItemsRecyclerView.setAdapter(foodItemsAdapter);

        // Initialize Firebase reference
        menuItemsRef = FirebaseDatabase.getInstance().getReference("menuItems");

        // Fetch food items from Firebase
        fetchFoodItems();
    }

    private void fetchFoodItems() {
        menuItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodItemList.clear();
                for (DataSnapshot foodItemSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = foodItemSnapshot.getValue(MenuItem.class);
                    if (menuItem != null) {
                        foodItemList.add(menuItem);
                    }
                }
                foodItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewFoodItemsActivity.this, "Failed to load food items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editFoodItem(MenuItem menuItem) {
        Intent intent = new Intent(this, AdminUpdateFoodItemActivity.class);
        intent.putExtra("menuItem", menuItem);
        startActivity(intent);
    }
}
