package com.android.tdsoft.paralexviewpagertest;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Admin on 1/5/2016.
 */
public class MyParallaxPagerTransformer implements ViewPager.PageTransformer {


    private final ParallaxItem[] parallaxItems;
    private float speed = 0.5f;

    public MyParallaxPagerTransformer(ParallaxItem[] parallaxItems) {
        this.parallaxItems = parallaxItems;
    }

    @Override
    public void transformPage(View page, float position) {

        for (int i = 0; i < parallaxItems.length; i++) {
            ParallaxItem parallaxItem = parallaxItems[i];
            View parallaxView = page.findViewById(parallaxItem.id);

                if (parallaxView != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                    if (position > -1 && position < 1) {
                        float width = parallaxView.getWidth();
                        parallaxView.setTranslationX((position * width * parallaxItem.speed));
                        float sc = ((float) page.getWidth()) / page.getWidth();
                        if (position == 0) {
                            page.setScaleX(1);
                            page.setScaleY(1);
                        } else {
                            page.setScaleX(sc);
                            page.setScaleY(sc);
                        }
                    }
                }


        }
    }
}
