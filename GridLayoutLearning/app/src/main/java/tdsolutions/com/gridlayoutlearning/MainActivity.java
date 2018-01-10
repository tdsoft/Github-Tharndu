package tdsolutions.com.gridlayoutlearning;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class MainActivity extends RecyclerViewActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int[] COLUMN_WEIGHTS={1, 4, 1};
    private static final String STATE_IN_PERMISSION="inPermission";
    private static final int REQUEST_PERMS=137;
    private boolean isInPermission=false;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        ColumnWeightSpanSizeLookup spanSizer=new ColumnWeightSpanSizeLookup(COLUMN_WEIGHTS);
        GridLayoutManager mgr=new GridLayoutManager(this, spanSizer.getTotalSpans());

        mgr.setSpanSizeLookup(spanSizer);
        setLayoutManager(mgr);
        setAdapter(new VideoAdapter());

        if (icicle!=null) {
            isInPermission=
                    icicle.getBoolean(STATE_IN_PERMISSION, false);
        }

        if (hasFilesPermission()) {
            loadVideos();
        }
        else if (!isInPermission) {
            isInPermission=true;

            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMS);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE_IN_PERMISSION, isInPermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        isInPermission=false;

        if (requestCode==REQUEST_PERMS) {
            if (hasFilesPermission()) {
                loadVideos();
            }
            else {
                finish(); // denied permission, so we're done
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return(new CursorLoader(this,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Video.Media.TITLE));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        ((VideoAdapter)getAdapter()).setVideos(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((VideoAdapter)getAdapter()).setVideos(null);
    }

    private boolean hasFilesPermission() {
        return(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED);
    }

    private void loadVideos() {
        getLoaderManager().initLoader(0, null, this);
    }

    class VideoAdapter extends RecyclerView.Adapter<BaseVideoController> {
        Cursor videos=null;

        @Override
        public BaseVideoController onCreateViewHolder(ViewGroup parent, int viewType) {
            BaseVideoController result=null;

            switch(viewType) {
                case 0:
                    result=new VideoThumbnailController(getLayoutInflater()
                            .inflate(R.layout.thumbnail,
                                    parent, false));
                    break;

                case 1:
                    int cursorColumn=videos.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME);

                    result=new VideoTextController(getLayoutInflater()
                            .inflate(R.layout.label,
                                    parent, false),
                            android.R.id.text1,
                            cursorColumn);
                    break;

                case 2:
                    cursorColumn=videos.getColumnIndex(MediaStore.Video.VideoColumns.DURATION);

                    result=new VideoTextController(getLayoutInflater()
                            .inflate(R.layout.label,
                                    parent, false),
                            android.R.id.text1,
                            cursorColumn);
                    break;
            }

            return(result);
        }

        @Override
        public void onBindViewHolder(BaseVideoController holder, int position) {
            videos.moveToPosition(position/3);
            holder.bindModel(videos);
        }

        void setVideos(Cursor videos) {
            this.videos=videos;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if (videos==null) {
                return(0);
            }

            return(videos.getCount()*3);
        }

        @Override
        public int getItemViewType(int position) {
            return(position % 3);
        }
    }
}