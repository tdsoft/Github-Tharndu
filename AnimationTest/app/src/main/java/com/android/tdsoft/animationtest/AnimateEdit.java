package com.android.tdsoft.animationtest;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

/**
 * Created by Admin on 4/25/2016.
 */
public class AnimateEdit extends EditText {
    private Animation animation;
    public AnimateEdit(Context context) {
        super(context);
        inti(context);
    }

    public AnimateEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        inti(context);
    }

    public AnimateEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inti(context);
    }

    private void inti(Context context){
        animation = AnimationUtils.loadAnimation(context,R.anim.scale);

    }

    public void checkEmpty(){
        if(TextUtils.isEmpty(getText()))
            startAnimation(animation);
    }
}
