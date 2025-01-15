package com.example.fooddelivery.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Customer.MenuItem;
import com.example.fooddelivery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<MenuItem> cartItems;
    private OnCartItemChangeListener onCartItemChangeListener;

    public CartAdapter(List<MenuItem> cartItems, OnCartItemChangeListener onCartItemChangeListener) {
        this.cartItems = cartItems;
        this.onCartItemChangeListener = onCartItemChangeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        MenuItem menuItem = cartItems.get(position);
        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCartItems(List<MenuItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        private ImageView foodImageView;
        private TextView foodNameTextView, foodPriceTextView, quantityTextView;
        private ImageButton increaseQuantityButton, decreaseQuantityButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            foodPriceTextView = itemView.findViewById(R.id.foodPriceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
        }

        public void bind(MenuItem menuItem) {
            Picasso.get().load(menuItem.getImageUrl()).into(foodImageView);
            foodNameTextView.setText(menuItem.getName());
            foodPriceTextView.setText("Price: Rs. " + menuItem.getPrice());
            quantityTextView.setText(String.valueOf(menuItem.getQuantity()));

            increaseQuantityButton.setOnClickListener(v -> {
                menuItem.setQuantity(menuItem.getQuantity() + 1);
                quantityTextView.setText(String.valueOf(menuItem.getQuantity()));
                onCartItemChangeListener.onCartItemChange();
            });

            decreaseQuantityButton.setOnClickListener(v -> {
                if (menuItem.getQuantity() > 1) {
                    menuItem.setQuantity(menuItem.getQuantity() - 1);
                    quantityTextView.setText(String.valueOf(menuItem.getQuantity()));
                    onCartItemChangeListener.onCartItemChange();
                } else {
                    Toast.makeText(itemView.getContext(), "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface OnCartItemChangeListener {
        void onCartItemChange();
    }
}
