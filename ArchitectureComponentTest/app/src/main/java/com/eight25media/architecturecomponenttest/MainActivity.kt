package com.eight25media.architecturecomponenttest

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.eight25media.architecturecomponenttest.testlifecycle.LiveDataTest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LiveDataTest().observe(this, Observer { t ->
            run {
                text.text = t
            }
        })
    }
}
