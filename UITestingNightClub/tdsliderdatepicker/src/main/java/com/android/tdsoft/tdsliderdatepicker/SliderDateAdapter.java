package com.android.tdsoft.tdsliderdatepicker;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 1/8/2016.
 */
public class SliderDateAdapter extends RecyclerView.Adapter<SliderDateAdapter.SliderDateHolder>{

    private final List<SliderDateItem> mDateStringList;
    private int currentSelectedItem;

    public SliderDateAdapter(List<SliderDateItem> sliderDateItems) {
        mDateStringList = sliderDateItems;
    }

    @Override
    public SliderDateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.date_item, parent,false);
        return new SliderDateHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderDateHolder holder, int position) {
        holder.txtDate.setText(mDateStringList.get(position).dateString);
        if(currentSelectedItem==position){
            holder.txtDate.setTextColor(Color.RED);
        }else{
            holder.txtDate.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return mDateStringList.size();
    }



    public class SliderDateHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public SliderDateHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            int left = (int) Utils.getSizeAccordingToDevice(txtDate.getContext(), 20);
            int right= (int) Utils.getSizeAccordingToDevice(txtDate.getContext(), 20);
            txtDate.setPadding(left,txtDate.getPaddingTop(),right,txtDate.getBottom());
        }
    }

    public void markCurrentSelectedItem(int position){
        currentSelectedItem = position;
        notifyDataSetChanged();
    }
}
