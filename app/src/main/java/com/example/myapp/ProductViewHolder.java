package com.example.myapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    TextView tvPId, tvQty;
    ImageView deleteProduct;
    ImageView editProduct;
    ImageView productImage;

    ProductViewHolder(View itemView) {
        super(itemView);
        tvPId = itemView.findViewById(R.id.contactName);
        tvQty = itemView.findViewById(R.id.phoneNum);
        deleteProduct = itemView.findViewById(R.id.deleteContact);
        editProduct = itemView.findViewById(R.id.editContact);

        productImage = itemView.findViewById(R.id.imageView3);
    }
}
