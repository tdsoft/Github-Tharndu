package com.android.tdsoft.assignment.itemdetails;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tdsoft.assignment.R;
import com.android.tdsoft.assignment.base.BaseFragment;
import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.searchresult.SearchResultPresenter;
import com.android.tdsoft.assignment.util.Injection;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemDetailsFragment extends BaseFragment implements ItemDetailsContract.View {


    private Material mMaterial;
    private TextView txtShortName;
    private TextView txtDescription;
    private TextView txtType;

    public ItemDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);
        this.txtShortName = (TextView) view.findViewById(R.id.txtShortName);
        this.txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        this.txtType = (TextView) view.findViewById(R.id.txtType);
        init();
        return view;
    }

    private void init(){
        if(mMaterial == null) {
            return;
        }

        txtShortName.setText(mMaterial.getShortName());
        txtDescription.setText(mMaterial.getDescription());
        txtType.setText(mMaterial.getType());
    }

    public static ItemDetailsFragment getInstance(Context context){
        ItemDetailsFragment itemDetailsFragment = new ItemDetailsFragment();
        itemDetailsFragment.setPresenter(new ItemDetailsPresenter(Injection.provideChambaRepository(context),itemDetailsFragment));
        return itemDetailsFragment;
    }

    @Override
    public void setMaterial(Material material) {
        mMaterial = material;
    }

    @Override
    public void setPresenter(ItemDetailsContract.Presenter presenter) {

    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void dismissProgressIndicator() {

    }
}
