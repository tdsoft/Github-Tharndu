package com.android.tdsoft.ioslikelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i =0; i < 200; i ++){
            arrayList.add("Item name: " + i);
        }

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, arrayList.toArray(new String[arrayList.size()]));
        IosLikeListView listView = (IosLikeListView) findViewById(R.id.list);
        listView.setAdapter(stringArrayAdapter);
    }
}
