package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

public class ProductCartAdapter extends RecyclerView.Adapter<com.example.myapp.ProductCartAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<Products> listProducts;
    private ArrayList<Products> mArrayList;
    private SqliteDatabase mDatabase;

    private LayoutInflater inflater;

    List<String> url;

    public ProductCartAdapter(Context context, ArrayList<Products> listProducts, List<String> url) {
        this.context = context;
        this.listProducts = listProducts;
        this.mArrayList = listProducts;
        mDatabase = new SqliteDatabase(context);
        this.url = url;
    }

    @NonNull
    @Override
    public com.example.myapp.ProductCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view  = inflater.inflate(R.layout.single_item_layout, viewGroup, false);

        com.example.myapp.ProductCartAdapter.ViewHolder holder = new com.example.myapp.ProductCartAdapter.ViewHolder(view);



        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Products products = listProducts.get(position);
       // holder.tvPId.setText(products.getCartId());
       // holder.tvQty.setText(products.getProductId());

        String currentURL = url.get(position);
        Picasso.with(context).load(currentURL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.productImage);
        holder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   editTaskDialog(Products);
            }
        });
        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* mDatabase.deleteProduct(products.getCartId());
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            */}
        });
    }
    /*
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listProducts = mArrayList;
                }

                else {
                    ArrayList<Products> filteredList = new ArrayList<>();
                    for (Products products : mArrayList) {
                        if (products.getProductId() > 0 ) {
                            filteredList.add(products);
                        }
                    }
                    listProducts = filteredList;
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
    }*/
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
        builder.show();*/



    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPId, tvQty;

        ImageView imageView;
        ImageView deleteProduct;
        ImageView editProduct;
        ImageView productImage;

        ViewHolder(View itemView) {
            super(itemView);
            //   tvPId = itemView.findViewById(R.id.contactName);
            //   tvQty = itemView.findViewById(R.id.phoneNum);
            deleteProduct = itemView.findViewById(R.id.deleteContact);
            editProduct = itemView.findViewById(R.id.editContact);

            productImage = itemView.findViewById(R.id.imageView3);
        }
    }
}