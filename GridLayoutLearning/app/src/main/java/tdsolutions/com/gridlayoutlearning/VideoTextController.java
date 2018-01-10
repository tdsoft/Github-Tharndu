package tdsolutions.com.gridlayoutlearning;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Admin on 1/6/2017.
 */

public class VideoTextController extends BaseVideoController {
    private TextView label=null;
    private int cursorColumn;

    VideoTextController(View cell, int labelId, int cursorColumn) {
        super(cell);
        this.cursorColumn=cursorColumn;

        label=(TextView)cell.findViewById(labelId);
    }

    @Override
    void bindModel(Cursor row) {
        super.bindModel(row);

        label.setText(row.getString(cursorColumn));
    }
}