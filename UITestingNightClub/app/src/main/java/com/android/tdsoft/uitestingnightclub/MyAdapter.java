package com.android.tdsoft.uitestingnightclub;

import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Admin on 12/29/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private int height;
    private int itemCount;
    public MyAdapter(int height,int itemCount){
        this.height = height;
        this.itemCount = itemCount;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext()).load("http://www.planwallpaper.com/static/images/wallpapers-7020-7277-hd-wallpapers.jpg")
                .transform(new CircleTransform(holder.imageView.getContext())).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            int itemHeight = height / itemCount;
            imageView.getLayoutParams().height = (int) Utils.dpToPx(imageView.getContext(), itemHeight - 20);
            itemView.getLayoutParams().height = itemHeight;

            itemView.requestLayout();
        }
    }
}