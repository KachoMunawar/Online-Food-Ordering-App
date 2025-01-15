package com.example.fooddelivery.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Customer.Order;
import com.example.fooddelivery.R;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<Order> orderHistoryList;

    public OrderHistoryAdapter(List<Order> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderHistoryList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView orderIdTextView;
        private TextView orderStatusTextView;
        private TextView orderTotalTextView;
        private TextView orderAddressTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            orderTotalTextView = itemView.findViewById(R.id.orderTotalTextView);
            orderAddressTextView = itemView.findViewById(R.id.orderAddressTextView);
        }

        public void bind(Order order) {
            orderIdTextView.setText("Order ID: " + order.getOrderId());
            orderStatusTextView.setText("Status: " + order.getStatus());
            orderTotalTextView.setText("Total: Rs. " + order.getTotalPrice());
            orderAddressTextView.setText("Address: " + order.getAddress());
        }
    }
}
