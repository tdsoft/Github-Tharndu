package tdsolutions.com.gridlayoutlearning;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Admin on 1/6/2017.
 */

public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView rv = null;

    public void setAdapter(RecyclerView.Adapter adapter) {
        getRecyclerView().setAdapter(adapter);
    }

    public RecyclerView.Adapter getAdapter() {
        return (getRecyclerView().getAdapter());
    }

    public void setLayoutManager(RecyclerView.LayoutManager mgr) {
        getRecyclerView().setLayoutManager(mgr);
    }

    public RecyclerView getRecyclerView() {
        if (rv == null) {
            rv = new RecyclerView(this);
            setContentView(rv);
        }

        return (rv);
    }
}
