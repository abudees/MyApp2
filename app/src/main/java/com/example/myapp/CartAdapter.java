package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class CartAdapter extends RecyclerView.Adapter<com.example.myapp.CartAdapter.ViewHolder> {


    Context c;
    ArrayList<Cart> players;




    public CartAdapter(Context ctx, ArrayList<Cart> players){

        //ASSIGN THEM LOCALLY
        this.c=ctx;
        this.players=players;

    }


    @NonNull
    @Override
    public com.example.myapp.CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        //VIEW OBJ FROM XML
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model,null);

        //holder
        MyHolder holder=new MyHolder(v);

        return holder;
    }




    //BIND DATA TO VIEWS
    @Override
    public void onBindViewHolder(final com.example.myapp.CartAdapter.ViewHolder holder, final int position) {

        holder.posTxt.setText(players.get(position).getPosition());
        holder.nameTxt.setText(players.get(position).getName());

        //HANDLE ITEMCLICKS
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //OPEN DETAIL ACTIVITY
                //PASS DATA

                //CREATE INTENT
                Intent i=new Intent(c, DetailActivity.class);

                //LOAD DATA
                i.putExtra("NAME",players.get(pos).getName());
                i.putExtra("POSITION",players.get(pos).getPosition());
                i.putExtra("ID",players.get(pos).getId());

                //START ACTIVITY
                c.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}