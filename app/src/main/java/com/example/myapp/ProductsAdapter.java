package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private LayoutInflater inflater;



    List<String> url;

    List<String> title;

    List<Integer> price;

    List<Integer> productId;

    String currency , area;

    private Context context;



    public ProductsAdapter(Context context, List<String> mUrl, List<String> mTitle, List<Integer> mPrice, List<Integer> mProductId, String mCurrency, String area){

        inflater = LayoutInflater.from(context);
        this.url = mUrl;
        this.title = mTitle;
        this.price = mPrice;
        this.productId = mProductId;
        this.currency = mCurrency;
        this.area = area;
        this.context = context;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.product_card, parent, false);
        ProductsAdapter.ViewHolder holder= new ProductsAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.ViewHolder holder, final int position) {

        String currentURL = url.get(position);
        String currenTitle = title.get(position);
        int currentPrice = price.get(position);




        Picasso.with(context).load(currentURL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.imageView);

        holder.productTitle.setText(currenTitle);

        holder.productPrice.setText(Integer.toString(currentPrice));

        holder.productCurrency.setText(currency);



        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context c = v.getContext();

                Intent intent = new Intent(c, ProductDetailsActivity.class);

                intent.putExtra("productId", productId.get(position));

                intent.putExtra("area", area);

                c.startActivity(intent);

            }

        });

    }

    @Override
    public int getItemCount() {
        return url.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView productTitle;
        TextView productPrice;
        TextView productCurrency;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.categoryImage);
            productTitle = itemView.findViewById(R.id.title);
            productPrice = itemView.findViewById(R.id.price);
            productCurrency = itemView.findViewById(R.id.currency);

        }
    }
}
