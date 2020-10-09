package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ViewHolder> implements Filterable {

    // define all adapter items and then uncomment the onclick listiner 23/9/2020


    private Context context;
    private ArrayList<Products> listProducts;
    private ArrayList<Products> mArrayList;
    private List<String> url ;
    private List<String> productTitle;
    private List<Integer> price;
    private SqliteDatabase mDatabase;
    private LayoutInflater inflater;



    int maxQty =25;


    ProductCartAdapter(Context context, List<String> url, List<String> productTitle, ArrayList<Products> listProducts, List<Integer> price) {
        this.context = context;
        this.listProducts = listProducts;
        this.mArrayList = listProducts;
        this.url = url;
        this.productTitle = productTitle;
        this.price =price;
        inflater = LayoutInflater.from(context);
        mDatabase = new SqliteDatabase(context);


    }








    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cart_list_item, parent, false);

        ViewHolder holder = new ProductCartAdapter.ViewHolder(view);


        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String currentURL = url.get(position);
        String currenName = productTitle.get(position);
        int currentPrice = price.get(position);
        final Products products = listProducts.get(position);




        Picasso.with(context).load(currentURL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.productImage);

        holder.getAdapterPosition();


        holder.productName.setText(currenName);
        holder.price.setText( String.valueOf(currentPrice));










        holder.qty.setText(String.valueOf(mDatabase.getQty(position)));


        holder.addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.qty.setText(String.valueOf(products.getQty()+1));

                mDatabase.addQty(products.getCartId(),products.getQty()+1);
                Toast.makeText(context, "Qty added successfully", Toast.LENGTH_LONG).show();

            }
        });








        holder.decreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Qty removed successfully", Toast.LENGTH_LONG).show();

               mDatabase.deleteProduct(products.getCartId());
            }
        });
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    ArrayList<Products> filteredList = new ArrayList<>();
                    for (Products products : mArrayList) {
                        if (products.getProductId() > 0 ) {
                            filteredList.add(products);
                        }
                    }
                    listProducts = filteredList;
                } else {
                    listProducts = mArrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listProducts;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listProducts = (ArrayList<Products>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return listProducts.size();
    }











   static class ViewHolder extends RecyclerView.ViewHolder {


       TextView productName, price, qty;
       ImageView decreaseQty, addQty, productImage;


       private ViewHolder(View itemView) {

           super(itemView);


           productName = itemView.findViewById(R.id.productName);
           price = itemView.findViewById(R.id.price);
           decreaseQty = itemView.findViewById(R.id.decreaseQty);
           addQty = itemView.findViewById(R.id.addQty);
           productImage = itemView.findViewById(R.id.productImage);

           qty = itemView.findViewById(R.id.qty);
       }



   }

}