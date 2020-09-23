package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import java.util.List;


public class CategoriesAdapter extends  RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private LayoutInflater inflater;

    List<String> url;

    List<String> title;

    List<Integer> id;

    private Context context;





    public CategoriesAdapter(Context context, List<String> url, List<String> title, List<Integer> id){

        inflater = LayoutInflater.from(context);
        this.url = url;
        this.title = title;
        this.id = id;
        this.context = context;

    }





    @NonNull
    @Override
    public com.example.myapp.CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = inflater.inflate(R.layout.category_card, viewGroup, false);
        com.example.myapp.CategoriesAdapter.ViewHolder holder = new com.example.myapp.CategoriesAdapter.ViewHolder(view);


        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return holder;
    }




    @Override
    public void onBindViewHolder(final com.example.myapp.CategoriesAdapter.ViewHolder holder, final int position) {

        String currentURL = url.get(position);
        final String currenTitle = title.get(position);


        Picasso.with(context).load(currentURL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.imageView);

        holder.categoryTitle.setText(currenTitle);



       holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context c = v.getContext();

                Intent intent = new Intent(c, ProductsActivity.class);

                intent.putExtra("categoryNumber", id.get(position));

                c.startActivity(intent);

            }

        });

    }

    @Override
    public int getItemCount() {
        return url.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView categoryTitle;



        private ViewHolder(View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.categoryImage);
            categoryTitle = itemView.findViewById(R.id.title);
        }

    }
}







