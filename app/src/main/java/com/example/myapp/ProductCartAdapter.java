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
/*
public class ProductCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private static final int FOOTER_VIEW = 1;
    private ArrayList<String> data; // Take any list that matches your requirement.

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






































    // And now in onCreateViewHolder you have to pass the correct view
    // while populating the list item.



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view;

        if (viewType == FOOTER_VIEW) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(view);
            return vh;
        }

        view = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false);

        NormalViewHolder vh = new NormalViewHolder(view);

        return vh;



       // View view = inflater.inflate(R.layout.cart_list_item, parent, false);

     //   ViewHolder holder = new ProductCartAdapter.ViewHolder(view);


      //  return  holder;
    }






    // Now bind the ViewHolder in onBindViewHolder

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        try {
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder vh = (NormalViewHolder) holder;

                vh.bindView(position);

                String currentURL = mUrl.get(position);
                String currenName = mProductTitle.get(position);
                int currentPrice = mPrice.get(position);
                //  final Products products = listProducts.get(position);
                //  final int currentQty = mQty.get(position);
                int currentID = mPID.get(position);



                // update price
                holder.getAdapterPosition();

                Picasso.with(context).load(mUrl.get(position)).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.productImage);

                holder.productName.setText(currenName);

                holder.price.setText( String.valueOf(currentPrice));

                holder.qty.setText(String.valueOf(mQty.get(position)));

                holder.total.setText(String.valueOf(currentPrice * mQty.get(position)));







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

                            if (mDatabase.listProducts().size() == 1) {

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


            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) holder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }





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

    // Now the critical part. You have return the exact item count of your list
    // I've only one footer. So I returned data.size() + 1
    // If you've multiple headers and footers, you've to return total count
    // like, headers.size() + data.size() + footers.size()


    @Override
    public int getItemCount() {


        if (mListProducts.size() == 0) {
            return 0;
        }



        // Add extra view to show the footer view
        return mListProducts.size() + 1;

       // return mListProducts.size();
    }






    // Now define getItemViewType of your own.

    @Override
    public int getItemViewType(int position) {
        if (position == mListProducts.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }



    // So you're done with adding a footer and its action on onClick.
    // Now set the default ViewHolder for NormalViewHolder


    // Define a ViewHolder for Footer view
    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the item
                }
            });
        }
    }






    static class NormalViewHolder extends RecyclerView.ViewHolder {


        // Define elements of a row here
        TextView productName, price, qty, total;
        ImageView decreaseQty, addQty, productImage;


        // Now define the ViewHolder for Normal list item
        private NormalViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.price);
            decreaseQty = itemView.findViewById(R.id.decreaseQty);
            addQty = itemView.findViewById(R.id.addQty);
            productImage = itemView.findViewById(R.id.productImage);

            total = itemView.findViewById(R.id.total);
            qty = itemView.findViewById(R.id.qty);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the normal items
                }
            });
        }

        public void bindView(int position) {
            // bindView() method to implement actions
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            // Define elements of a row here
            public ViewHolder(View itemView) {
                super(itemView);
                // Find view by ID and initialize here
            }


        }
    }

 */