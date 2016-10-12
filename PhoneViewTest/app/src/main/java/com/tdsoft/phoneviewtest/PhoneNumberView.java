package com.tdsoft.phoneviewtest;

import android.content.Context;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Admin on 7/29/2016.
 */
public class PhoneNumberView extends EditText {

    private boolean isDeleting;

    public PhoneNumberView(Context context) {
        super(context);
        init();
    }

    public PhoneNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhoneNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        isDeleting = false;
//        addTextChangedListener(txtMyTextWatcher);
        addTextChangedListener(new PhoneNumberFormattingTextWatcher());

    }

    private TextWatcher txtMyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!isDeleting && s!=null ){
                switch ( s.length()){
                    case 3:
                        s.insert(0,"(");
                        s.insert(s.length(),")");
                        break;
                    case 6:
                        s.insert(5," ");
                        break;
                    case 9:
                        s.append(" ");
                        break;
                }
            }

            if(isDeleting){
                //deleting has completed
                isDeleting = false;
            }
        }
    };


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_DEL) {
//            System.out.println("onKeyUp " + getText().length());
//            if(getText().length() == 10){
//                int cursorPosition = getSelectionStart();
//                if (cursorPosition > 0) {
//                    setText(getText().delete(8, cursorPosition - 1));
//                    setSelection(cursorPosition-1);
//                }
//            }
//        }
//
//        return super.onKeyDown(keyCode, event);
//
//    }
//
//    //(000) 000 0
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_DEL) {
//
//            if(getText().length() == 10){
//                System.out.println("onKeyUp " + getText().length());
//                int cursorPosition = getSelectionStart();
//                if (cursorPosition > 0) {
//                    setText(getText().replace(cursorPosition - 1, cursorPosition, ""));
//                    setSelection(cursorPosition-1);
//                }
//            }
//        }
//        return super.onKeyUp(keyCode, event);
//    }
}
