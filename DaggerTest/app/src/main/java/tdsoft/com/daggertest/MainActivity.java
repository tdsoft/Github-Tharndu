package tdsoft.com.daggertest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    Context app;

    @Inject
    Foo foo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAppComponent().inject(this);
        ((App)app).puuu();

        System.out.println(foo.getName());

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public ApplicationComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }
}
