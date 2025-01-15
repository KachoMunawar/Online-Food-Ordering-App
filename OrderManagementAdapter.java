package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class OrderManagementAdapter extends RecyclerView.Adapter<OrderManagementAdapter.OrderManagementViewHolder> {

    private List<Order> orderList;
    private final com.example.adminapp.OrderManagementAdapter.OnOrderStatusChangeListener onOrderStatusChangeListener;

    public interface OnOrderStatusChangeListener {
        void onOrderStatusChange(Order order);
    }

    public OrderManagementAdapter(List<Order> orderList, OrderManagementAdapter.OnOrderStatusChangeListener onOrderStatusChangeListener) {
        this.orderList = orderList;
        this.onOrderStatusChangeListener = onOrderStatusChangeListener;
    }

    @NonNull
    @Override
    public OrderManagementAdapter.OrderManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderManagementAdapter.OrderManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderManagementAdapter.OrderManagementViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderManagementViewHolder extends RecyclerView.ViewHolder {

        private TextView orderIdTextView;
        private TextView orderStatusTextView;
        private TextView orderTotalPriceTextView;
        private TextView orderAddressTextView;
        private Button changeStatusButton;

        public OrderManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            orderTotalPriceTextView = itemView.findViewById(R.id.orderTotalPriceTextView);
            orderAddressTextView = itemView.findViewById(R.id.orderAddressTextView);
            changeStatusButton = itemView.findViewById(R.id.changeStatusButton);

            changeStatusButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Order order = orderList.get(position);
                    onOrderStatusChangeListener.onOrderStatusChange(order);
                }
            });
        }

        public void bind(Order order) {
            orderIdTextView.setText("Order ID: " + order.getOrderId());
            orderStatusTextView.setText("Status: " + order.getStatus());
            orderTotalPriceTextView.setText("Total: Rs. " + order.getTotalPrice());
            orderAddressTextView.setText("Address: " + order.getAddress());

            if (order.getStatus().equals("Placed")) {
                changeStatusButton.setText("Mark as Completed");
            } else {
                changeStatusButton.setText("Completed");
                changeStatusButton.setEnabled(false);
            }
        }
    }
}
