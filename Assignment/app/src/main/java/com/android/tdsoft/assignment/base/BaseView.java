package com.android.tdsoft.assignment.base;

/**
 * Created by Admin on 4/29/2016.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
    void showProgressIndicator();
    void dismissProgressIndicator();
}
