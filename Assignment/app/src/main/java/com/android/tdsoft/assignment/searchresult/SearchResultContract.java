package com.android.tdsoft.assignment.searchresult;

import com.android.tdsoft.assignment.base.BasePresenter;
import com.android.tdsoft.assignment.base.BaseView;
import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.data.MaterialProperty;

import java.util.List;

/**
 * Created by Admin on 5/26/2016.
 */
public class SearchResultContract {

    interface View extends BaseView<Presenter> {
        void onSearchSqlReceived(String sql);
        void onMaterialsLoaded(List<Material> materials);
        void onNoDataFound();
    }

    interface Presenter extends BasePresenter {
        void getMaterialsForSql(String sql);
    }

}
