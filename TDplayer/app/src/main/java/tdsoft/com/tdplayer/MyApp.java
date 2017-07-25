package tdsoft.com.tdplayer;

import android.app.Application;

/**
 * Created by Admin on 6/29/2017.
 */

public class MyApp extends Application {
    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApp getInstance() {
        return instance;
    }
}
