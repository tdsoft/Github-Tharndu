package com.eight25media.insecurefilesender;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 10/17/2016.
 */

public class AdapterDevices extends RecyclerView.Adapter<AdapterDevices.MyHolder> {

    private final List<MyDevice> list;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public AdapterDevices(List<MyDevice> strings) {
        this.list = strings;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_devices, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        MyDevice myDevice = list.get(position);
        holder.textDeviceName.setText(myDevice.deviceName);
        holder.txtDeviceAddress.setText(myDevice.deviceAddress);
        holder.txtPaired.setText(myDevice.isPaired ? "Paired":"None-Paired");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textDeviceName,txtDeviceAddress, txtPaired ;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textDeviceName = (TextView) itemView.findViewById(R.id.txt_name);
            txtDeviceAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtPaired = (TextView) itemView.findViewById(R.id.txt_paired);
        }


        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClicked(getAdapterPosition());
            }
        }
    }
}
