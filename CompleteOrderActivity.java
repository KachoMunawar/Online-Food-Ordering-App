package com.example.fooddelivery.Customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fooddelivery.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompleteOrderActivity extends AppCompatActivity {

    private TextView orderIdTextView;
    private TextView orderStatusTextView;
    private Button confirmDeliveryButton;
    private Order order;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);

        orderIdTextView = findViewById(R.id.orderIdTextView);
        orderStatusTextView = findViewById(R.id.orderStatusTextView);
        confirmDeliveryButton = findViewById(R.id.confirmDeliveryButton);

        // Retrieve the order passed from the previous activity
        order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            orderIdTextView.setText("Order ID: " + order.getOrderId());
            orderStatusTextView.setText("Status: " + order.getStatus());
        }

        confirmDeliveryButton.setOnClickListener(view -> confirmDelivery());

        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
    }

    private void confirmDelivery() {
        if (order != null) {
            ordersRef.child(order.getOrderId()).child("status").setValue("Delivered")
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(CompleteOrderActivity.this, "Order marked as delivered", Toast.LENGTH_SHORT).show();
                            order.setStatus("Delivered");
                            orderStatusTextView.setText("Status: Delivered");
                        } else {
                            Toast.makeText(CompleteOrderActivity.this, "Failed to update order status", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(CompleteOrderActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
