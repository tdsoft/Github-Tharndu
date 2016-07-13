package com.android.tdsoft.assignment.itemdetails;

import com.android.tdsoft.assignment.base.BasePresenter;
import com.android.tdsoft.assignment.base.BaseView;
import com.android.tdsoft.assignment.data.Material;

import java.util.List;

/**
 * Created by Admin on 5/26/2016.
 */
public class ItemDetailsContract {
    interface View extends BaseView<Presenter> {
        void setMaterial(Material material);
    }

    interface Presenter extends BasePresenter {
    }
}
