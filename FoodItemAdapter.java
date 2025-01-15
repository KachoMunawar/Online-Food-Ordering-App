package com.example.fooddelivery.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Customer.MenuItem;
import com.example.fooddelivery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

    private List<MenuItem> foodItemsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MenuItem menuItem);
    }

    public FoodItemAdapter(List<MenuItem> foodItemsList, OnItemClickListener listener) {
        this.foodItemsList = foodItemsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        MenuItem menuItem = foodItemsList.get(position);
        holder.nameTextView.setText(menuItem.getName());
        holder.priceTextView.setText("Price: Rs. " + menuItem.getPrice());
        holder.quantityTextView.setText("Quantity: " + menuItem.getQuantity());
        Picasso.get().load(menuItem.getImageUrl()).into(holder.foodImageView);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(menuItem));
    }

    @Override
    public int getItemCount() {
        return foodItemsList.size();
    }

    public static class FoodItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView quantityTextView;
        public ImageView foodImageView;

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.foodNameTextView);
            priceTextView = itemView.findViewById(R.id.foodPriceTextView);
            quantityTextView = itemView.findViewById(R.id.foodQuantityTextView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
        }
    }
}
