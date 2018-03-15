package com.eight25media.architecturecomponenttest.testlifecycle

import android.arch.lifecycle.LiveData
import android.os.Handler
import android.util.Log


/**
 * Created by Admin on 2/19/2018.
 */
class LiveDataTest : LiveData<String>(), Runnable {
    var i = 0;
    private var isActive = false
    override fun run() {
        var s = "item " + i.toString()
        Log.d("LiveDataTest", s)
        value = s
        i++
        if (isActive)
            Handler().postDelayed(this, 2000)
    }


    override fun onActive() {
        super.onActive()
        isActive = true
        Handler().postDelayed(this, 2000)
    }

    override fun onInactive() {
        super.onInactive()
        isActive = false
    }
}