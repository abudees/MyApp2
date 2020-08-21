package com.example.myapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView img;
    TextView nameTxt,posTxt;
    ItemClickListener itemClickListener;


    public MyHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View v) {

    }
}
