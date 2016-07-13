package com.android.tdsoft.assignment.searchresult;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.tdsoft.assignment.R;
import com.android.tdsoft.assignment.base.BaseFragment;
import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.itemdetails.ItemDetailsFragment;
import com.android.tdsoft.assignment.util.Injection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends BaseFragment implements SearchResultContract.View, AdapterResult.OnItemClickListener {


    private RecyclerView listMaterials;
    private SearchResultContract.Presenter mPresenter;
    private AdapterResult adapterResult;

    public SearchResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        listMaterials = (RecyclerView) view.findViewById(R.id.list);
        listMaterials.setLayoutManager(new LinearLayoutManager(getContext()));
        if (adapterResult != null) {
            listMaterials.setAdapter(adapterResult);
        }

        return view;
    }


    public static SearchResultFragment getInstance(Context context) {
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        searchResultFragment.setPresenter(new SearchResultPresenter(Injection.provideChambaRepository(context), searchResultFragment));
        return searchResultFragment;
    }

    @Override
    public void setPresenter(SearchResultContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void dismissProgressIndicator() {

    }

    @Override
    @Subscribe
    public void onSearchSqlReceived(String sql) {
        if(sql==null){
            return;
        }
        System.out.println(sql);
        mPresenter.getMaterialsForSql(sql);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onMaterialsLoaded(List<Material> materials) {
        if(materials== null || materials.size() == 0){
            Toast.makeText(getContext(), "No result found", Toast.LENGTH_LONG).show();
            return;
        }
        adapterResult = new AdapterResult(materials);
        listMaterials.setLayoutManager(new LinearLayoutManager(getContext()));
        listMaterials.setAdapter(adapterResult);
        adapterResult.setOnItemClickListener(this);
    }

    @Override
    public void onNoDataFound() {

    }

    @Override
    public void onItemClick(Material material) {
        ItemDetailsFragment itemDetailsFragment = ItemDetailsFragment.getInstance(getContext());
        itemDetailsFragment.setMaterial(material);
        replace(itemDetailsFragment, true);
    }
}
