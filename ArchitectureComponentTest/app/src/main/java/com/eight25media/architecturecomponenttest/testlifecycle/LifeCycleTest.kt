package com.eight25media.architecturecomponenttest.testlifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log

/**
 * Created by Admin on 2/19/2018.
 */
class LifeCycleTest(lifecycle: Lifecycle) : LifecycleObserver {

    private val TAG: String = "LifeCycleTest"

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG, "onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.d(TAG, "onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.d(TAG, "onResume")
    }

    init {
        lifecycle.addObserver(this)
    }
}