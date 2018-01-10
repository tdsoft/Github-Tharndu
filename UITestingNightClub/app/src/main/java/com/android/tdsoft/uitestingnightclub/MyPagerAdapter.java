package com.android.tdsoft.uitestingnightclub;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

/**
 * Created by Admin on 1/6/2016.
 */
public class MyPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    public MyPagerAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Declare Variables

        ImageView imgFlag;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listitem_pager, container, false);
        // Locate the ImageView in viewpager_item.xml
        imgFlag = (ImageView) itemView.findViewById(R.id.imageView);
        Glide.with(context).load("http://www.planwallpaper.com/static/images/kartandtinki1_photo-wallpapers_02.jpg").into(imgFlag);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);
    }

}
