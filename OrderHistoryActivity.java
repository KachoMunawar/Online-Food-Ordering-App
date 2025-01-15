package com.example.fooddelivery.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fooddelivery.Customer.Adapter.OrderHistoryAdapter;
import com.example.fooddelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderHistoryRecyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Order> orderHistoryList = new ArrayList<>();
    private DatabaseReference orderHistoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Initialize UI components
        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryList);
        orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);

        // Initialize Firebase reference
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        orderHistoryRef = FirebaseDatabase.getInstance().getReference("orderHistory").child(userId);

        // Fetch order history
        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        orderHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderHistoryList.clear();  // Clear the list before adding new items
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        orderHistoryList.add(order);
                    } else {
                        Log.e("OrderHistory", "Order data is null");
                    }
                }
                orderHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderHistoryActivity.this, "Failed to load order history: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("OrderHistory", "Database error: " + error.getMessage());
            }
        });
    }
}
