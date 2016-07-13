package com.android.tdsoft.assignment.searchresult;

import com.android.tdsoft.assignment.data.MaterialProperty;
import com.android.tdsoft.assignment.data.source.AssignmentDataSource;
import com.android.tdsoft.assignment.data.source.AssignmentRepository;

import java.util.List;

/**
 * Created by Admin on 5/27/2016.
 */
public class SearchDialogPresenter implements SearchDialogContract.Presenter {

    private final AssignmentRepository mAssignmentRepository;
    private final SearchDialogContract.View mSearchDialogView;

    public SearchDialogPresenter(AssignmentRepository assignmentRepository, SearchDialogContract.View searchDialogView) {
        mAssignmentRepository = assignmentRepository;
        mSearchDialogView = searchDialogView;
        mSearchDialogView.setPresenter(this);
    }



    @Override
    public void start() {

    }

    @Override
    public void getAllProperties() {
        mAssignmentRepository.getAllProperties(new AssignmentDataSource.LoadMaterialPropertyCallback() {
            @Override
            public void onTasksLoaded(List<MaterialProperty> tasks) {
                mSearchDialogView.onAllPropertiesLoaded(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                mSearchDialogView.onNoDataFound();
            }
        });
    }
}
