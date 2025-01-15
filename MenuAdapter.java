package com.example.fooddelivery.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Customer.MenuItem;
import com.example.fooddelivery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuItems;
    private OnMenuItemClickListener listener;

    public interface OnMenuItemClickListener {
        void onMenuItemClick(MenuItem menuItem);
    }

    public MenuAdapter(List<MenuItem> menuItems, OnMenuItemClickListener listener) {
        this.menuItems = menuItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.bind(menuItem, listener);

        // Handle the availability of the item
        if (!menuItem.isAvailable()) {
            holder.addButton.setEnabled(false);
            holder.addButton.setText("Unavailable");
        } else {
            holder.addButton.setEnabled(true);
            holder.addButton.setText("Available");
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodImageView;
        private TextView foodNameTextView, foodPriceTextView;
        private Button addButton;  // Add the button view

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            foodPriceTextView = itemView.findViewById(R.id.foodPriceTextView);
            addButton = itemView.findViewById(R.id.addButton);  // Initialize the button view
        }

        public void bind(MenuItem menuItem, OnMenuItemClickListener listener) {
            foodNameTextView.setText(menuItem.getName());
            foodPriceTextView.setText("Price: " + menuItem.getPrice());
            Picasso.get().load(menuItem.getImageUrl()).into(foodImageView);

            itemView.setOnClickListener(v -> listener.onMenuItemClick(menuItem));
        }
    }
}
