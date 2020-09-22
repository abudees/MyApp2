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

public class ProductCartAdapter extends RecyclerView.Adapter<ProductViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Products> listProducts;
    private ArrayList<Products> mArrayList;
    private SqliteDatabase mDatabase;
    private List<String> url ;


    ProductCartAdapter(Context context, List<String> url, ArrayList<Products> listProducts) {
        this.context = context;
        this.url = url;
        this.listProducts = listProducts;
        this.mArrayList = listProducts;
        mDatabase = new SqliteDatabase(context);

    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_layout, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {

        String currentURL = url.get(position);



        Picasso.with(context).load(currentURL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.imageView);

        final Products products = listProducts.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context c = v.getContext();



            }

        });
       // holder.tvPId.setText(products.getCartId());
       // holder.tvQty.setText(products.getProductId());
       /* holder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(Products);
            }
        });
        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.deleteProduct(products.getCartId());
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });*/
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
   /* private void editTaskDialog(final Products products) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_contacts, null);
        final EditText nameField = subView.findViewById(R.id.enterName);
        final EditText contactField = subView.findViewById(R.id.enterPhoneNum);

        if (products != null) {
            nameField.setText(products.getProductId());
            contactField.setText(String.valueOf(products.getQty()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit contact");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("EDIT CONTACT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String ph_no = contactField.getText().toString();
                  if (TextUtils.isEmpty(name)) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    //    mDatabase.updateProduct(new Products(Objects.requireNonNull(listProducts.get(), name, ph_no));
                    ((Activity) context).finish();
                    context.startActivity(((Activity)
                            context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled",Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }*/
}