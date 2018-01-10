package com.android.tdsoft.uitestingnightclub;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

public class MyParallax extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parallax);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.wrap);
        ViewTreeObserver vto = linearLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > 15)
                    linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int width = linearLayout.getMeasuredWidth();
                int height = linearLayout.getMeasuredHeight();
                int itemCount = getResources().getInteger(R.integer.item_count);
                recyclerView.setAdapter(new MyAdapter(height, itemCount));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
