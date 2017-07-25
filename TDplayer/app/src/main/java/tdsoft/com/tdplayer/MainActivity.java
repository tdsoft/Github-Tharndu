package tdsoft.com.tdplayer;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import tdsoft.com.tdplayer.utils.Constants;

import static tdsoft.com.tdplayer.utils.Constants.EXTRA_URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.getClipData() == null || intent.getClipData().getItemCount() == 0) {
            finish();
            return;
        }
        ClipData.Item clipData = intent.getClipData().getItemAt(0);

        if (clipData == null) {
            finish();
            return;
        }

        if(clipData.getText() == null){
            finish();
            return;
        }

        if(!clipData.getText().toString().startsWith(Constants.START_WITH)){
            finish();
            return;
        }
        Intent serviceIntent1 = new Intent(this, TDPlayerService.class);
        serviceIntent1.putExtra(EXTRA_URL, clipData.getText());
        startService(serviceIntent1);
        finish();


    }

}
