package com.wet.eyetracking.opencv

import android.content.Context
import android.util.Log
import com.wet.eyetracking.App
import com.wet.eyetracking.R
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class OpenCVLoaderCallback(AppContext: Context?) : BaseLoaderCallback(AppContext) {

    private val TAG = "OpenCVLoader"

    @Inject
    lateinit var eyeDetector: CascadeClassifier


    override fun onManagerConnected(status: Int) {
        super.onManagerConnected(status)

        when (status) {
            LoaderCallbackInterface.SUCCESS -> {
                Log.i(TAG, "OpenCV loaded successfully")

                try {
                    val buffer = ByteArray(4096)
                    var bytesRead = 0

                    // load cascade file from application resources
                    val ise = mAppContext.getResources().openRawResource(R.raw.haarcascade_eye)

                    val cascadeDirEye = mAppContext.getDir("cascade", Context.MODE_PRIVATE)
                    val mCascadeFileEye = File(cascadeDirEye, "haarcascade_eye.xml")
                    val ose = FileOutputStream(mCascadeFileEye)

                    while ({ bytesRead = ise.read(buffer); bytesRead }() != -1) {
                        ose.write(buffer, 0, bytesRead)
                    }
                    ise.close()
                    ose.close()

                    App.appComponent.inject(this)

                    eyeDetector.load(mCascadeFileEye.getAbsolutePath())
                    if (eyeDetector.empty()) {
                        Log.e(TAG, "Failed to load cascade classifier for eye")
                    } else {
                        Log.i(TAG, "Loaded cascade classifier from " + mCascadeFileEye.getAbsolutePath())
                    }

                    cascadeDirEye.delete()

                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e(TAG, "Failed to load cascade. Exception thrown: $e")
                }
            }
            else -> {
                super.onManagerConnected(status)
            }
        }
    }
}