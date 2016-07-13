package com.android.tdsoft.assignment.base;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.tdsoft.assignment.R;

/**
 * Created by Admin on 5/26/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public void replace(BaseFragment fragment, boolean addToBackStack) {
        if (fragment != null) {
            String stackName = fragment.getClass().getName();
            BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(stackName);

            if (baseFragment != null && baseFragment.isVisible()) {
                return;

            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (addToBackStack) {
                //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(stackName);
            } else {
                //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.container, fragment, stackName);
            }

            fragmentTransaction.commit();
        }
    }
}
