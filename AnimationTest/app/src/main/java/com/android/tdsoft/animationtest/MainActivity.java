package com.android.tdsoft.animationtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    AnimateEdit animateEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animateEdit = (AnimateEdit) findViewById(R.id.editText);
    }


    public void onAnimListClick(View view) {
        Intent intent = new Intent(this, AnimateList.class);
        startActivity(intent);
    }

    public void onScaleClick(View view) {
        Button button = (Button) view;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        button.startAnimation(animation);

    }

    public void onCheckEmptyClick(View view) {
        animateEdit.checkEmpty();


    }

}
