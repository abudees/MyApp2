package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DataBeanAdapter extends RecyclerView.Adapter<DataBeanAdapter.ViewHolder> {

    private List<DataBean> items;
    private int itemLayout;

    public DataBeanAdapter(List<DataBean> items, int itemLayout){
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataBean item = items.get(position);
        holder.name.setText(item.getName());
        holder.card.setText(item.getCard());
        //All the thing you gonna show in the item
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView card;

        public ViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.name);
            card =  itemView.findViewById(R.id.card);
        }
    }
}
