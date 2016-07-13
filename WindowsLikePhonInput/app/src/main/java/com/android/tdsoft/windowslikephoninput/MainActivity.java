package com.android.tdsoft.windowslikephoninput;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editText1, editText2, editText3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);

        editText1.addTextChangedListener(new MyTextWatcher(editText1));
        editText2.addTextChangedListener(new MyTextWatcher(editText2));
        editText3.addTextChangedListener(new MyTextWatcher(editText3));

        editText1.setOnKeyListener(new MyKeyListener());
        editText2.setOnKeyListener(new MyKeyListener());
        editText3.setOnKeyListener(new MyKeyListener());
    }

    private class MyKeyListener implements View.OnKeyListener{

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                switch (v.getId()) {
                    case R.id.editText3:
                        if (TextUtils.isEmpty(editText3.getText()))
                            editText2.requestFocus();
                        break;

                    case R.id.editText2:
                        if (TextUtils.isEmpty(editText2.getText()))
                            editText1.requestFocus();
                        break;

                }
            }

            return false;
        }
    }

    private class MyTextWatcher implements TextWatcher {


        private final EditText mEditText;

        public MyTextWatcher(EditText editText){
            this.mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (mEditText.getId()){
                case R.id.editText1:
                    if (!TextUtils.isEmpty(mEditText.getText()) && mEditText.getText().length() == 2)
                        editText2.requestFocus();
                    break;
                case R.id.editText2:
                    if (!TextUtils.isEmpty(mEditText.getText()) && mEditText.getText().length()  == 2)
                        editText3.requestFocus();
                    break;
                case R.id.editText3:
                    if (!TextUtils.isEmpty(mEditText.getText()) && mEditText.getText().length() == 2)
                        hideKeyboard(MainActivity.this);
                    break;
            }


        }
    };



    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
