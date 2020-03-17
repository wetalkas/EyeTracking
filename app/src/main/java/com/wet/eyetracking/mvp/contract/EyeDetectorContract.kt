package com.wet.eyetracking.mvp.contract

import android.content.Context
import android.content.Intent
import android.net.Uri

class EyeDetectorContract {
    interface View : BaseContract.View {
        fun showProgress()
        fun countProgress(text: String, progress: Int)
        fun hideProgress()
        fun showErrorMessage(error: String?)
        fun showChartFragment(id: Long)
        fun showHistoryFragment()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun createFilePickerIntent(context: Context): Intent
        fun openHistory()
        fun detectEye(files: List<Uri>)
        fun cancel()
    }
}