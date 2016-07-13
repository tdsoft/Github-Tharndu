package com.android.tdsoft.assignment.searchresult;

import com.android.tdsoft.assignment.base.BasePresenter;
import com.android.tdsoft.assignment.base.BaseView;
import com.android.tdsoft.assignment.data.MaterialProperty;

import java.util.List;

/**
 * Created by Admin on 5/27/2016.
 */
public class SearchDialogContract {
    interface Presenter extends BasePresenter{
        void getAllProperties();
    }

    interface View extends BaseView<Presenter>{
        void onAllPropertiesLoaded(List<MaterialProperty> materialProperties);
        void onNoDataFound();
    }
}
