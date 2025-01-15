package com.example.fooddelivery.Customer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Customer.User;
import com.example.fooddelivery.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private UpdateUserListener updateUserListener;
    private DeleteUserListener deleteUserListener;

    public UserAdapter(List<User> users, UpdateUserListener updateUserListener, DeleteUserListener deleteUserListener) {
        this.users = users;
        this.updateUserListener = updateUserListener;
        this.deleteUserListener = deleteUserListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.nameTextView.setText(user.getName());
        holder.emailTextView.setText(user.getEmail());
        holder.roleTextView.setText(user.getRole());

        holder.updateButton.setOnClickListener(v -> updateUserListener.onUpdate(user));
        holder.deleteButton.setOnClickListener(v -> deleteUserListener.onDelete(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, roleTextView;
        Button updateButton, deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface UpdateUserListener {
        void onUpdate(User user);
    }

    public interface DeleteUserListener {
        void onDelete(User user);
    }
}
