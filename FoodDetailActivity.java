package com.example.fooddelivery.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.squareup.picasso.Picasso;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView foodImageView;
    private TextView foodNameTextView, foodPriceTextView, foodDescriptionTextView;
    private EditText quantityEditText;
    private Button addToCartButton;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        foodImageView = findViewById(R.id.foodImageView);
        foodNameTextView = findViewById(R.id.foodNameTextView);
        foodPriceTextView = findViewById(R.id.foodPriceTextView);
        foodDescriptionTextView = findViewById(R.id.foodDescriptionTextView);
        quantityEditText = findViewById(R.id.quantityEditText);
        addToCartButton = findViewById(R.id.addToCartButton);

        menuItem = (MenuItem) getIntent().getSerializableExtra("menuItem");
        if (menuItem != null) {
            Picasso.get().load(menuItem.getImageUrl()).into(foodImageView);
            foodNameTextView.setText(menuItem.getName());
            foodPriceTextView.setText("Price: " + menuItem.getPrice());
            foodDescriptionTextView.setText(menuItem.getDescription());
        }

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityStr = quantityEditText.getText().toString();
                if (quantityStr.isEmpty() || Integer.parseInt(quantityStr) <= 0) {
                    Toast.makeText(FoodDetailActivity.this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(quantityStr);
                menuItem.setQuantity(quantity);

                    // Handle the availability of the item
                    if (menuItem.isAvailable()) {
                        Intent intent = new Intent();
                        intent.putExtra("menuItem", menuItem);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

            }
        });
    }
}
