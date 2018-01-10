package tdsolutions.com.tempprojectspannable;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ForegroundColorSpan boldColorSpan1;
    ForegroundColorSpan dimColorSpan2;
    ForegroundColorSpan testColorSpan3;
    ForegroundColorSpan testColorSpan4;
    RelativeSizeSpan relativeSmallSpan5;

    ForegroundColorSpan boldColorSpan6;
    ForegroundColorSpan dimColorSpan7;
    ForegroundColorSpan testColorSpan8;
    ForegroundColorSpan testColorSpan9;
    RelativeSizeSpan relativeSmallSpan10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = ((TextView) findViewById(R.id.fragment_title));

//        boldColorSpan1 =
//                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.Green));
//        dimColorSpan2 =
//                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.Blue));
//        testColorSpan3 =
//                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.Black));
//        testColorSpan4 =
//                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.Red));
//        relativeSmallSpan5 = new RelativeSizeSpan(0.8f);
//
//
//        boldColorSpan6 =
//                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.Green));
//        dimColorSpan7 =
//                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.Blue));
//        testColorSpan8 =
//                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.Black));
//        testColorSpan9 =
//                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.Red));
//        relativeSmallSpan10 = new RelativeSizeSpan(0.8f);
//
//        SimpleSpanBuilderTest ssbTest=new SimpleSpanBuilderTest();
//        ssbTest.append("Green",boldColorSpan1);
//        ssbTest.append("Black",dimColorSpan2);
//        ssbTest.append("Blue",testColorSpan3);
//        ssbTest.append("Red",testColorSpan4);
//        ssbTest.append("Green",boldColorSpan6);
//        ssbTest.append("Black",dimColorSpan7);
//        ssbTest.append("Blue",testColorSpan8);
//        ssbTest.append("Red",testColorSpan9);
//        tv.setText(ssbTest.build());

        SimpleSpanBuilder ssbTest=new SimpleSpanBuilder(this);
        ssbTest.append("Green",1);
        ssbTest.append("Black",2);
        ssbTest.append("Blue",3);
        ssbTest.append("Red",4);
        ssbTest.append("Green",1);
        ssbTest.append("Black",2);
        ssbTest.append("Blue",3);
        ssbTest.append("Red",4);
        tv.setText(ssbTest.build());
    }

    public void onNewActivity(View view) {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }
}
