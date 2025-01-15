package com.example.fooddelivery.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddelivery.Customer.Adapter.CartAdapter;
import com.example.fooddelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<MenuItem> cartItems = new ArrayList<>();
    private TextView totalPriceTextView;
    private EditText addressEditText;
    private Button placeOrderButton;
    private DatabaseReference ordersRef;
    private DatabaseReference cartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize UI components
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        addressEditText = findViewById(R.id.addressEditText);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems, this);
        cartRecyclerView.setAdapter(cartAdapter);

        // Initialize Firebase references
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        cartRef = FirebaseDatabase.getInstance().getReference("cart");

        // Get cart items from intent
        cartItems = (ArrayList<MenuItem>) getIntent().getSerializableExtra("selectedItems");
        if (cartItems != null) {
            cartAdapter.updateCartItems(cartItems);
            updateTotalPrice();
        }

        // Set up button click listener
        placeOrderButton.setOnClickListener(view -> placeOrder());
    }

    @Override
    public void onCartItemChange() {
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (MenuItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        totalPriceTextView.setText("Total: Rs. " + totalPrice);
    }

    private void placeOrder() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String address = addressEditText.getText().toString().trim();
        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderId = ordersRef.push().getKey();
        double totalPrice = calculateTotalPrice(cartItems);
        Order order = new Order(orderId, userId, cartItems, address, "Placed", totalPrice);
        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CartActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    cartItems.clear();
                    cartAdapter.notifyDataSetChanged();
                    updateTotalPrice();
                })
                .addOnFailureListener(e -> Toast.makeText(CartActivity.this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private double calculateTotalPrice(List<MenuItem> cartItems) {
        double totalPrice = 0;
        for (MenuItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }
}
