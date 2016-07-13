package com.android.tdsoft.animationtest;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Admin on 4/25/2016.
 */
public class AnimAdapter extends RecyclerView.Adapter<AnimAdapter.MyHolder> {
    ArrayList<Integer> mColors = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int adapterPosition);
    }

    public interface ItemTouchHelperAdapter {

        void onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }

    public AnimAdapter() {
        generateData();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View container;
        public TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            container = itemView;
            textView = (TextView) itemView.findViewById(R.id.textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null)
                onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyHolder(container);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        int color = mColors.get(position);
        holder.container.setBackgroundColor(color);
        holder.textView.setText("#" + Integer.toHexString(color));

    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }

    public void deleteItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            mColors.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            int color = generateColor();
            mColors.add(position, color);
            notifyItemInserted(position);
        }
    }

    public void changeItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            int color = generateColor();
            mColors.set(position, color);
            notifyItemChanged(position);
        }
    }


    private int generateColor() {
        int red = ((int) (Math.random() * 200));
        int green = ((int) (Math.random() * 200));
        int blue = ((int) (Math.random() * 200));
        return Color.rgb(red, green, blue);
    }

    private void generateData() {
        for (int i = 0; i < 100; ++i) {
            mColors.add(generateColor());
        }
    }
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mColors, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mColors, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


}
