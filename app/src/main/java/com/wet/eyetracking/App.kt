package com.wet.eyetracking

import android.app.Application
import android.util.Log
import com.wet.eyetracking.di.AppComponent
import com.wet.eyetracking.di.AppModule
import com.wet.eyetracking.di.DaggerAppComponent
import com.wet.eyetracking.di.EyeModule
import com.wet.eyetracking.opencv.OpenCVLoaderCallback

import io.realm.Realm
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader


class App : Application() {

    private val TAG = "EyeTracking"


    companion object {
        lateinit var appComponent: AppComponent
    }


    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        initDagger()

        initOpenCV()
    }


    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .eyeModule(EyeModule()).build()
    }


    private fun initOpenCV() {
        val loaderCallback = OpenCVLoaderCallback(this)

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization.")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, loaderCallback)
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!")
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }
}
