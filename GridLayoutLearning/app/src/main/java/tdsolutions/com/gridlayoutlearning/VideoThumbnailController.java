package tdsolutions.com.gridlayoutlearning;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Admin on 1/6/2017.
 */

public class VideoThumbnailController extends BaseVideoController {
    private ImageView thumbnail=null;

    VideoThumbnailController(View cell) {
        super(cell);

        thumbnail=(ImageView)cell.findViewById(R.id.thumbnail);
    }

    @Override
    void bindModel(Cursor row) {
        super.bindModel(row);

        Uri video=
                ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        row.getInt(row.getColumnIndex(MediaStore.Video.Media._ID)));

        Picasso.with(thumbnail.getContext())
                .load(video.toString())
                .fit().centerCrop()
                .placeholder(R.drawable.ic_media_video_poster)
                .into(thumbnail);
    }
}
