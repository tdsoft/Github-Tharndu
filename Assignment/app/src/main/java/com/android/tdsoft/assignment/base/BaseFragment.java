package com.android.tdsoft.assignment.base;

import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Admin on 5/26/2016.
 */
public class BaseFragment extends Fragment {

    public  void replace(BaseFragment fragment, boolean addToBackStack){
        ((BaseActivity)getActivity()).replace(fragment,addToBackStack);
    }
}
