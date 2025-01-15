package com.example.fooddelivery.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Customer.Order;
import com.example.fooddelivery.R;

import java.util.List;

public class CompleteOrderAdapter extends RecyclerView.Adapter<CompleteOrderAdapter.ConfirmOrderViewHolder> {

    private List<Order> deliveredOrdersList;
    private ConfirmOrderListener listener;

    public CompleteOrderAdapter(List<Order> deliveredOrdersList, ConfirmOrderListener listener) {
        this.deliveredOrdersList = deliveredOrdersList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConfirmOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirm_order, parent, false);
        return new ConfirmOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmOrderViewHolder holder, int position) {
        Order order = deliveredOrdersList.get(position);
        holder.bind(order, listener);
    }

    @Override
    public int getItemCount() {
        return deliveredOrdersList.size();
    }

    class ConfirmOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView orderNameTextView;
        private TextView orderPriceTextView;
        private Button confirmOrderButton;

        public ConfirmOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNameTextView = itemView.findViewById(R.id.orderNameTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            confirmOrderButton = itemView.findViewById(R.id.confirmOrderButton);
        }

        public void bind(Order order, ConfirmOrderListener listener) {
            orderNameTextView.setText(order.getOrderId()); // Change this as needed
            orderPriceTextView.setText("Total: Rs. " + order.getTotalPrice());

            confirmOrderButton.setOnClickListener(v -> listener.onConfirmOrder(order));
        }
    }

    public interface ConfirmOrderListener {
        void onConfirmOrder(Order order);
    }
}
