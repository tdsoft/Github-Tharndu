package com.android.tdsoft.fileobservertest;

import android.content.Context;
import android.os.FileObserver;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Admin on 4/20/2016.
 */
public class MyDirObserver extends FileObserver {
    String superPath;
    boolean isCreate,isModified,isOpen,isAgainModified;
    Context context;
    public MyDirObserver(Context context, String path) {
        super(path);
        this.context = context;
        superPath = path;
    }

    @Override
    public void onEvent(int event, String path) {
        Log.e("onEvent of Directory", "=== onEvent ===");
        try {
            _Dump("dir", event, path, superPath);
        } catch (NullPointerException ex) {
            Log.e("ERROR", "I am getting error");
        }
    }

    private void _Dump(final String tag, int event, String path, String superPath) {
        Log.d(tag, "=== dump begin ===");
        Log.d(tag, "path=" + path);
        Log.d(tag, "super path=" + superPath);
        Log.d(tag, "event list:");
        if ((event & FileObserver.OPEN) != 0) {
            Log.d(tag, "  OPEN");
        }
        if ((event & FileObserver.CLOSE_NOWRITE) != 0) {
            Log.d(tag, "  CLOSE_NOWRITE");
        }
        if ((event & FileObserver.CLOSE_WRITE) != 0) {


            Log.d(tag, "  CLOSE_WRITE");
            Log.i("NEWFILEOBSERVER", "File is Modified");
            if (path != null) {
                Log.d("---------FilePath", superPath + path);
            }


        }
        if ((event & FileObserver.CREATE) != 0) {
            isCreate = true;
            Log.i("NEWFILEOBSERVER", "File is Created ");
            if (path != null) {
                Log.d("---------FilePath", superPath + path);
            }
            Log.d(tag, "  CREATE");


        }
        if ((event & FileObserver.DELETE) != 0) {
            Log.i("NEWFILEOBSERVER", "File is deleted");
            if (path != null) {
                Log.d("---------FilePath", superPath + path);
            }
            //  startMyActivity("A new file is deleted thats="+superPath);


            Log.d(tag, "  DELETE");


        }

        if ((event & FileObserver.DELETE_SELF) != 0) {
            Log.d(tag, "  DELETE_SELF");
        }

        if ((event & FileObserver.ACCESS) != 0) {
            Log.d(tag, "  ACCESS");
        }

        if ((event & FileObserver.MODIFY) != 0) {
            if (!isModified)
                isModified = true;

            if (isModified && isOpen)
                isAgainModified = true;
            Log.d(tag, "  MODIFY");
        }

        if ((event & FileObserver.MOVED_FROM) != 0) {
            Log.d(tag, "  MOVED_FROM");
            if (path != null) {
                Log.d("---------FilePath", superPath + path);
            }
        }

        if ((event & FileObserver.MOVED_TO) != 0) {
            Log.d(tag, "  MOVED_TO");
            if (path != null) {
                Log.d("---------FilePath", superPath + path);
            }
        }

        if ((event & FileObserver.MOVE_SELF) != 0) {
            Log.d(tag, "  MOVE_SELF");
        }

        if ((event & FileObserver.ATTRIB) != 0) {
            Log.d(tag, "  ATTRIB");
        }

        Log.d(tag, "=== dump end ===");
    }
}
