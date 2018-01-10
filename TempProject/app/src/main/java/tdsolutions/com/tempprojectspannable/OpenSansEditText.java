package tdsolutions.com.tempprojectspannable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Admin on 3/30/2017.
 */

public class OpenSansEditText extends LinearLayout {
    private TextView txtPrefix;
    private EditText etValue;
    private String prefix = "$";
    private int prefixColor = Color.BLACK;

    public OpenSansEditText(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public OpenSansEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public OpenSansEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.opensansedittext_view, this,true);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.OpenSansEditText);
            prefix = a.getString(R.styleable.OpenSansEditText_prefix);
            prefixColor = a.getColor(R.styleable.OpenSansEditText_prefixColor, Color.BLACK);
        }
    }

    public  CharSequence getValue(){
        return etValue.getText();
    }

    public CharSequence egtPrefix(){
        return txtPrefix.getText();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        txtPrefix = (TextView) findViewById(R.id.txt_prefix);
        etValue  = (EditText) findViewById(R.id.et_value);

        txtPrefix.setText(prefix);
        txtPrefix.setTextColor(prefixColor);
    }
}
