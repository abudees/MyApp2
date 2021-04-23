package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;



public class ProductCartAdapter  extends RecyclerView.Adapter<ProductCartAdapter.ViewHolder> implements Filterable {

    // define all adapter items and then uncomment the onclick listiner 23/9/2020


    private Context context;
    private ArrayList<Products> mListProducts;
    private ArrayList<Products> mArrayList;
    private List<String> mUrl ;
    private List<String> mProductTitle;
    private List<Integer> mPrice;
    private List<Integer> mQty;
    private List<Integer> mPID;
    private SqliteDatabase mDatabase;
    private LayoutInflater inflater;

    int maxQty =25;


    ProductCartAdapter(Context context, List<Integer> pIDs, List<String> url, List<String> productTitle, ArrayList<Products> listProducts, List<Integer> price, List<Integer> qty ) {
        this.context = context;
        this.mPID = pIDs;
        this.mListProducts = listProducts;
        this.mArrayList = listProducts;
        this.mUrl = url;
        this.mProductTitle = productTitle;
        this.mPrice =price;
        this.mQty = qty;

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

        String currentURL = mUrl.get(position);
        String currenName = mProductTitle.get(position);
        int currentPrice = mPrice.get(position);
        //  final Products products = listProducts.get(position);
        final int currentQty = mQty.get(position);
        int currentID = mPID.get(position);



        // update price
        holder.getAdapterPosition();

        Picasso.with(context).load(currentURL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.productImage);

        holder.productName.setText(currenName);

        holder.price.setText( String.valueOf(currentPrice));

        holder.qty.setText(String.valueOf(currentQty));

        holder.total.setText(String.valueOf(currentPrice * currentQty));







        holder.addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int newQty = mDatabase.getQty(currentID)+1;

                mDatabase.updateQty(currentID, newQty);

                holder.qty.setText(String.valueOf(newQty));

                holder.total.setText(String.valueOf(currentPrice * newQty));


                Toast.makeText(context, "Qty added successfully", Toast.LENGTH_LONG).show();
            }
        });

        holder.decreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Qty removed successfully", Toast.LENGTH_LONG).show();

                if (mDatabase.getQty(currentID) == 1){

                    if (mDatabase.listAll().size() == 1) {

                        mDatabase.clearCart();
                        notifyDataSetChanged();


                    } else {
                        mDatabase.deleteProduct(currentID);


                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), mListProducts.size());

                        mListProducts.remove(mListProducts.get(position));
                        notifyDataSetChanged();

                    }
                } else if (mDatabase.getQty(currentID) > 1){

                    int newQty = mDatabase.getQty(currentID)-1;

                    mDatabase.updateQty(currentID, newQty);
                    holder.qty.setText(String.valueOf(newQty));
                    holder.total.setText(String.valueOf(currentPrice * newQty));

                }
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
                    mListProducts = filteredList;
                } else {
                    mListProducts = mArrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mListProducts;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListProducts = (ArrayList<Products>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return mListProducts.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView productName, price, qty, total;
        ImageView decreaseQty, addQty, productImage;


        private ViewHolder(View itemView) {

            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.price);
            decreaseQty = itemView.findViewById(R.id.decreaseQty);
            addQty = itemView.findViewById(R.id.addQty);
            productImage = itemView.findViewById(R.id.productImage);

            total = itemView.findViewById(R.id.total);
            qty = itemView.findViewById(R.id.qty);


        }
    }

}

