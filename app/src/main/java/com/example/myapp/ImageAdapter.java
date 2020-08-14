package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends PagerAdapter {


    Context c;

    private List<String> _imagePaths;

    private LayoutInflater inflater;

    ImageView imgDisplay;

    public ImageAdapter (Context c, List<String> imagePaths) {
        this._imagePaths = imagePaths;
        this.c = c;
    }



    @Override
    public int getCount() {
        return this._imagePaths.size();
    }




    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        String currentURL = _imagePaths.get(position);

        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View viewLayout = inflater.inflate(R.layout.pager_layout, container, false);

        imgDisplay = viewLayout.findViewById(R.id.imageView7);


        Glide.with(c).load(currentURL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imgDisplay);

        (container).addView(viewLayout);

        return viewLayout;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        (container).removeView((RelativeLayout) object);

    }
}