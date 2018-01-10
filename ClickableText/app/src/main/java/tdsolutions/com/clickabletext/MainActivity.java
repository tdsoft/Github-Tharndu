package tdsolutions.com.clickabletext;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView) findViewById(R.id.text);
        setClickableText(text);
    }

    private void setClickableText(TextView textView) {
        String text = "this"; // the text you want to mark as a link

        SpannableString ss = new SpannableString(textView.getText());// full text
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#91ca46"));//link text color

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //What ever the code you want when click on text goes here
                Toast.makeText(MainActivity.this,"you clicked",Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int start = textView.getText().toString().indexOf(text);
        int length = start + text.length();

        ss.setSpan(clickableSpan, start, length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ss.setSpan(fcs, start, length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }
}
