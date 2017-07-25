package tdsoft.com.tdplayer;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;

import java.io.File;

/**
 * Created by Admin on 4/15/2017.
 */

public class AppUtils {
    public static File getTempDirectoryPath(Context ctx) {
        File cache = null;

        // SD Card Mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cache = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Android/data/" + ctx.getPackageName() + "/cache/");
        }
        // Use internal storage
        else {
            cache = ctx.getCacheDir();
        }

        // Create the cache directory if it doesn't exist
        if (!cache.exists()) {
            cache.mkdirs();
        }

        return cache;
    }

    public static float getSizeCorrectly(int unit, float sizeText) {
        return TypedValue.applyDimension(unit, sizeText, MyApp.getInstance().getResources().getDisplayMetrics());
    }

    public static Typeface getTypeFaceFromAsset(String font) {
        return Typeface.createFromAsset(MyApp.getInstance().getAssets(), "fonts/" + font);
    }

    public static Typeface getTypeface(Context context, String font) {
        AssetManager am = context.getResources().getAssets();
        return Typeface.createFromAsset(am,
                String.format("fonts/%s", font));
    }


    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    public static AlertDialog showYesNoDialog(Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener onClickPositiveListener, DialogInterface.OnClickListener onClickNegativeListener) {
        AlertDialog alertDialog = getAlertDialog(context, R.string.btn_text_yes, R.string.btn_text_no, onClickPositiveListener, onClickNegativeListener);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog getAlertDialog(Context context, int positiveButtonText, int negativeButtonText,
                                             DialogInterface.OnClickListener onPositiveClickListener, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
        if (positiveButtonText > -1) {
            alertDialog.setPositiveButton(positiveButtonText, onPositiveClickListener);
        }

        if (negativeButtonText > -1) {
            alertDialog.setNegativeButton(negativeButtonText, onNegativeClickListener);
        }
        return alertDialog.create();
    }
}
