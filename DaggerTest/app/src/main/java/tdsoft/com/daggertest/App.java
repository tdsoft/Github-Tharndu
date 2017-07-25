package tdsoft.com.daggertest;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

/**
 * Created by Admin on 7/18/2017.
 */

public class App extends Application {
    @Inject
    Foo foo;

    private static Context instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDagger();
    }

    private ApplicationComponent appComponent;

    private void initDagger() {
        appComponent = DaggerApplicationComponent.builder().build();
        appComponent.inject(this);


        foo.setName("Tharindu");
    }

    public void puuu(){
        System.out.println("pooo");
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }
}
