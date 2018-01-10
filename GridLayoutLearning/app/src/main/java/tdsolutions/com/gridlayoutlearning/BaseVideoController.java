package tdsolutions.com.gridlayoutlearning;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Admin on 1/6/2017.
 */

public class BaseVideoController extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Uri videoUri = null;
    private String videoMimeType = null;

    BaseVideoController(View cell) {
        super(cell);

        cell.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW);

        i.setDataAndType(videoUri, videoMimeType);
        itemView.getContext().startActivity(i);
    }

    void bindModel(Cursor row) {
        int mimeTypeColumn =
                row.getColumnIndex(MediaStore.Video.Media.MIME_TYPE);

        videoUri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                row.getInt(row.getColumnIndex(MediaStore.Video.Media._ID)));
        videoMimeType = row.getString(mimeTypeColumn);
    }
}
