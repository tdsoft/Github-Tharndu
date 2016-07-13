package com.android.tdsoft.androidneurophtest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Admin on 4/8/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private final File[] files;
    private OnMyItemClickListener onItemClickListener;

    public MyAdapter(File[] files){
        this.files = files;
    }

    public interface OnMyItemClickListener{
        void onItemClick(File file);
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_file,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.txtFileName.setText(files[position].getName());
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtFileName;
        public MyHolder(View itemView) {
            super(itemView);
            txtFileName = (TextView) itemView.findViewById(R.id.txtFileName);
            txtFileName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null) {
                onItemClickListener.onItemClick(files[getAdapterPosition()]);
            }
        }
    }
}
