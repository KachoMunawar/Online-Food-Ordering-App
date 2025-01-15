package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderManagementActivity extends AppCompatActivity {

    private RecyclerView orderManagementRecyclerView;
    private OrderManagementAdapter orderManagementAdapter;
    private List<Order> orderList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        orderManagementRecyclerView = findViewById(R.id.orderManagementRecyclerView);
        orderManagementRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderManagementAdapter = new OrderManagementAdapter(orderList, this::updateOrderStatus);
        orderManagementRecyclerView.setAdapter(orderManagementAdapter);

        fetchOrders();
    }

    private void fetchOrders() {
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }
                orderManagementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderManagementActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderStatus(Order order) {
        if (order.getStatus().equals("Placed")) {
            order.setStatus("Completed");
            ordersRef.child(order.getOrderId()).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(OrderManagementActivity.this, "Order marked as completed", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(OrderManagementActivity.this, "Failed to update order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}