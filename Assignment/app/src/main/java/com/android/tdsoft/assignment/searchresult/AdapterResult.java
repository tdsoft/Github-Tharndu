package com.android.tdsoft.assignment.searchresult;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tdsoft.assignment.R;
import com.android.tdsoft.assignment.data.Material;

import java.util.List;

/**
 * Created by Admin on 5/28/2016.
 */
public class AdapterResult extends RecyclerView.Adapter<AdapterResult.MyHolder> {
    private List<Material> materialList;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Material material);
    }

    public AdapterResult(List<Material> materialList){
        this.materialList = materialList;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtMaterialName;
        public LinearLayout container;

        public MyHolder(View itemView) {
            super(itemView);
            txtMaterialName = (TextView) itemView.findViewById(R.id.txtMaterialName);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(materialList.get(getAdapterPosition()));
            }
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_search_result, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Material material = materialList.get(position);
        holder.txtMaterialName.setText(material.getShortName());
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }
}
