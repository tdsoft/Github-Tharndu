package com.android.tdsoft.assignment.itemdetails;

import com.android.tdsoft.assignment.data.source.AssignmentRepository;
import com.android.tdsoft.assignment.searchresult.SearchResultContract;

/**
 * Created by Admin on 5/26/2016.
 */
public class ItemDetailsPresenter implements ItemDetailsContract.Presenter{

    private final AssignmentRepository mAssignmentRepository;
    private final ItemDetailsContract.View mItemDetailsView;

    public ItemDetailsPresenter(AssignmentRepository assignmentRepository, ItemDetailsContract.View itemDetailsView) {
        mAssignmentRepository = assignmentRepository;
        mItemDetailsView = itemDetailsView;
        mItemDetailsView.setPresenter(this);
    }
    @Override
    public void start() {

    }
}
