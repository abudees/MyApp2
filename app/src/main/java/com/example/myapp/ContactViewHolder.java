package com.example.myapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    TextView tvName, tvPhoneNum;
    ImageView deleteProduct;
    ImageView editProduct;

    ContactViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.contactName);
        tvPhoneNum = itemView.findViewById(R.id.phoneNum);
        deleteProduct = itemView.findViewById(R.id.deleteContact);
        editProduct = itemView.findViewById(R.id.editContact);
    }
}