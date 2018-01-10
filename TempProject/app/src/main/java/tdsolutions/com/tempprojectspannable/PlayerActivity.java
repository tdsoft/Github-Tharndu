package tdsolutions.com.tempprojectspannable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

public class PlayerActivity extends AppCompatActivity {

    private boolean toggle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


    }

    public void onFullscreen(View view) {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (toggle) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            toggle = false;
        } else {
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            toggle = true;
        }
        getWindow().setAttributes(attrs);
    }

}
