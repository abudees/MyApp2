package com.example.myapp;

import android.app.Activity;
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

public class RecyclerViewWithFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements Filterable {

    private static final int FOOTER_VIEW = 1;
  //  private ArrayList<String> data; // Take any list that matches your requirement.

    // define all adapter items and then uncomment the onclick listiner 23/9/2020
    private static Context context;
    private ArrayList<Products> mListProducts;
    private ArrayList<Products> mArrayList;
    private List<String> mUrl ;
    private List<String> mProductTitle;
    private List<Integer> mPrice;
    private List<Integer> mQty;
    private List<Integer> mPID;
    private SqliteDatabase mDatabase;
    CheckoutActivity car;

   // private double total = 0;
    int maxQty =25;


    RecyclerViewWithFooterAdapter(Context context, List<Integer> pIDs, List<String> url, List<String> productTitle, ArrayList<Products> listProducts, List<Integer> price, List<Integer> qty ) {
        this.context = context;
        this.mPID = pIDs;
        this.mListProducts = listProducts;
        this.mArrayList = listProducts;
        this.mUrl = url;
        this.mProductTitle = productTitle;
        this.mPrice =price;
        this.mQty = qty;

      //  LayoutInflater inflater = LayoutInflater.from(context);
        mDatabase = new SqliteDatabase(context);
        car = new CheckoutActivity();


    }





    // Define a ViewHolder for Footer view
    public static class FooterViewHolder extends ViewHolder {

        static TextView totalTextView ;

      //  public  static int n =7;

        FooterViewHolder(View itemView) {
            super(itemView);

            totalTextView = itemView.findViewById(R.id.footerText);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the item
                }
            });
        }
    }

    // Now define the ViewHolder for Normal list item
    public static class NormalViewHolder extends ViewHolder {

        TextView productName, price, qty, total;
        ImageView decreaseQty, addQty, productImage;

       // TextView txtView ;



        NormalViewHolder(View itemView) {
            super(itemView);

           // txtView = ((Activity)context).findViewById(R.id.footerText);



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
    }

    // And now in onCreateViewHolder you have to pass the correct view
    // while populating the list item.

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if (viewType == FOOTER_VIEW) {
            v = LayoutInflater.from(context).inflate(R.layout.list_item_footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(v);
            return vh;
        }

        v = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false);

        NormalViewHolder vh = new NormalViewHolder(v);

        return vh;
    }

    // Now bind the ViewHolder in onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder,  int position) {

        try {


            if (holder instanceof NormalViewHolder) {
                NormalViewHolder vh = (NormalViewHolder) holder;

                vh.bindView(position);


                String currentURL = mUrl.get(position);
                String currenName = mProductTitle.get(position);
                int currentPrice = mPrice.get(position);
                //  final Products products = listProducts.get(position);
                final int currentQty = mQty.get(position);
                int currentID = mPID.get(position);


                holder.getAdapterPosition();

                Picasso.with(context).load(mUrl.get(position)).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.productImage);

                vh.productName.setText(currenName);

                vh.price.setText(String.valueOf(currentPrice));

                vh.qty.setText(String.valueOf(currentQty));


                vh.total.setText(String.valueOf(currentPrice * mQty.get(position)));


             //   TextView txtView = ((Activity)context).findViewById(R.id.footerText);
             //   vh.txtView.setText("Hello");

              //  FooterViewHolder.totalTextView.setText(String.valueOf(car.countTotal(mPrice,mQty)));


                vh.addQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int newQty = mDatabase.getQty(currentID) + 1;

                        mDatabase.updateQty(currentID, newQty);

                        vh.qty.setText(String.valueOf(newQty));

                        vh.total.setText(String.valueOf(currentPrice * newQty));

                        Toast.makeText(context, "Qty added successfully", Toast.LENGTH_LONG).show();

                       // Toast.makeText(context, String.valueOf(FooterViewHolder.n), Toast.LENGTH_LONG).show();





                        notifyDataSetChanged();
                    }
                });

                vh.decreaseQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(context, "Qty removed successfully", Toast.LENGTH_LONG).show();

                        if (mDatabase.getQty(currentID) == 1) {

                            if (mDatabase.listProducts().size() == 1) {

                                mDatabase.clearCart();



                            } else {
                                mDatabase.deleteProduct(currentID);


                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), mListProducts.size());

                                mListProducts.remove(mListProducts.get(position));

                                notifyItemChanged(mQty.get(position));
                            }
                        } else if (mDatabase.getQty(currentID) > 1) {

                            int newQty = mDatabase.getQty(currentID) - 1;

                            mDatabase.updateQty(currentID, newQty);
                            vh.qty.setText(String.valueOf(newQty));
                            vh.total.setText(String.valueOf(currentPrice * newQty));

                            notifyItemChanged(mQty.get(position));
                        }
                    }
                });


            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) holder;


               // notifyItemChanged(mQty.get(position));


             //   double total =0;
               // for(int i = 0; i < mPrice.size(); i++){

                 //   total += (mPrice.get(i)*mQty.get(i));
               // }



                vh.totalTextView.setText(String.valueOf(car.countTotal(mPrice,mQty)));
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
        if (mListProducts == null) {
            return 0;
        }

        if (mListProducts.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return mListProducts.size() + 1;
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        // Define elements of a row here
        ViewHolder(View itemView) {
            super(itemView);
            // Find view by ID and initialize here
        }

        void bindView(int position) {
            // bindView() method to implement actions
        }
    }
}
