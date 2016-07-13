package com.android.tdsoft.assignment.searchresult;

import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.data.source.AssignmentDataSource;
import com.android.tdsoft.assignment.data.source.AssignmentRepository;

import java.util.List;

/**
 * Created by Admin on 5/26/2016.
 */
public class SearchResultPresenter implements SearchResultContract.Presenter {
    private final AssignmentRepository mAssignmentRepository;
    private final SearchResultContract.View mSearchResultView;

    public SearchResultPresenter(AssignmentRepository assignmentRepository, SearchResultContract.View searchResultView) {
        mAssignmentRepository = assignmentRepository;
        mSearchResultView = searchResultView;
        mSearchResultView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getMaterialsForSql(String sql) {
        mAssignmentRepository.getMaterialsForSql(sql, new AssignmentDataSource.LoadMaterialCallback() {
            @Override
            public void onTasksLoaded(List<Material> tasks) {
                mSearchResultView.onMaterialsLoaded(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                mSearchResultView.onNoDataFound();
            }
        });
    }
}
