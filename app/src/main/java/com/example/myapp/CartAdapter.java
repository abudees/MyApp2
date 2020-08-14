package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<com.example.myapp.CartAdapter.ViewHolder> {

    private LayoutInflater inflater;

    private Context context;

    List<String> url;

    List<String> title;

    List<Integer> price;

    List<Integer> qty;

    List<Integer> ptoductId;

    String currency;




    public CartAdapter(Context mContext, List<String> mUrl,  List<String> mTitle , List<Integer> mQty, List<Integer> mPrice ){

        inflater = LayoutInflater.from(context);

        this.url =  mUrl;

        this.title  =  mTitle;

        this.qty = mQty;

        this.price = mPrice;

        this.context = mContext;
    }


    @NonNull
    @Override
    public com.example.myapp.CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = inflater.inflate(R.layout.single_item_layout, viewGroup, false);
        com.example.myapp.CartAdapter.ViewHolder holder = new com.example.myapp.CartAdapter.ViewHolder(view);


        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_item_layout, viewGroup, false);
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(v);


        return holder;
    }




    @Override
    public void onBindViewHolder(final com.example.myapp.CartAdapter.ViewHolder holder, final int position) {


        String currentURL = url.get(position);


        Picasso.with(context).load(currentURL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.imageView);


        //  String currentPrice = String.valueOf(price.get(position));
        // String currenQty = String.valueOf(qty.get(position));



        String currenTitle = title.get(position);
        int currentPrice = price.get(position);
        int currentQty = price.get(position);




        Picasso.with(context).load(currentURL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.imageView);

        holder.cartTitle.setText(currenTitle);

        holder.cartPrice.setText(Integer.toString(currentPrice));

        holder.cartQty.setText(Integer.toString(currentQty));





        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context c = v.getContext();

            }

        });

    }

    @Override
    public int getItemCount() {


        return price.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView cartPrice , cartQty , cartTitle;



        private ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView8);

            cartPrice = itemView.findViewById(R.id.itemprice);

            cartQty = itemView.findViewById(R.id.qty2);

            cartTitle = itemView.findViewById(R.id.title);


        }

    }
}