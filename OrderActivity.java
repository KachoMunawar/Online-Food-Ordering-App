package com.example.fooddelivery.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.Customer.Adapter.CartAdapter;
import com.example.fooddelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<MenuItem> cartItems = new ArrayList<>();
    private TextView totalPriceTextView;
    private EditText addressEditText;
    private Button placeOrderButton;
    private DatabaseReference ordersRef;
    private DatabaseReference orderHistoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize UI components
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        addressEditText = findViewById(R.id.addressEditText);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems, this::updateTotalPrice);
        cartRecyclerView.setAdapter(cartAdapter);

        // Initialize Firebase references
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        orderHistoryRef = FirebaseDatabase.getInstance().getReference("orderHistory");

        // Handle the passed menu item
        MenuItem menuItem = (MenuItem) getIntent().getSerializableExtra("menuItem");
        if (menuItem != null) {
            addMenuItemToCart(menuItem);
        }

        // Set up button click listener
        placeOrderButton.setOnClickListener(view -> placeOrder());

        // Fetch cart items from Firebase
        fetchCartItems();
    }

    private void fetchCartItems() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = itemSnapshot.getValue(MenuItem.class);
                    if (menuItem != null) {
                        cartItems.add(menuItem);
                    }
                }
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderActivity.this, "Failed to load cart items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMenuItemToCart(MenuItem menuItem) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

        cartRef.child(menuItem.getId()).setValue(menuItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cartItems.add(menuItem);
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
            } else {
                Toast.makeText(OrderActivity.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void placeOrder() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String address = addressEditText.getText().toString().trim();
        double totalPrice = calculateTotalPrice();

        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderId = ordersRef.push().getKey();
        if (orderId != null) {
            Order order = new Order(orderId, userId, cartItems, address, "Placed", totalPrice);

            ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    orderHistoryRef.child(userId).child(orderId).setValue(order);
                    Toast.makeText(OrderActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity after placing the order
                } else {
                    Toast.makeText(OrderActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        totalPriceTextView.setText("Total Price: Rs. " + totalPrice);
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;
        for (MenuItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }
}
