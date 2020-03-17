package com.wet.eyetracking.mvp.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.util.Log
import com.nononsenseapps.filepicker.FilePickerActivity
import com.nononsenseapps.filepicker.Utils
import com.wet.eyetracking.*
import com.wet.eyetracking.model.ExperimentItem
import com.wet.eyetracking.model.PointRealmObject
import com.wet.eyetracking.mvp.contract.EyeDetectorContract
import com.wet.eyetracking.opencv.EyeDetector
import com.wet.eyetracking.utils.MyUtils
import com.wet.eyetracking.utils.getImagesSorted
import com.wet.eyetracking.utils.isImage
import com.wet.eyetracking.utils.saveEyeExperiment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.*
import javax.inject.Inject

class EyeDetectorPresenter : EyeDetectorContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: EyeDetectorContract.View

    @Inject
    lateinit var eyeDetector: EyeDetector

    @Inject
    lateinit var mRealm: Realm


    init {
        App.appComponent.inject(this)
    }


    override fun subscribe(subscription: Disposable) {
        subscriptions.add(subscription)
    }

    override fun unsubscribe() {
        view.hideProgress()
        subscriptions.clear()
    }

    override fun attach(view: EyeDetectorContract.View) {
        this.view = view
    }

    override fun detach() {
        unsubscribe()
    }


    override fun createFilePickerIntent(context: Context): Intent {
        val i = Intent(context, FilePickerActivity::class.java)

        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true)
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false)
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE_AND_DIR)
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().path)

        return i
    }


    override fun openHistory() {
        view.showHistoryFragment()
    }


    override fun detectEye(files: List<Uri>) {

        view.showProgress()


        val experiment = ExperimentItem()

        var index = 0
        var size = 0

        subscribe(Observable.fromIterable(files)
                .map { Utils.getFileForUri(it) }
                .flatMap { Observable.fromIterable(it.getImagesSorted()) }
                .toList()
                .toObservable()
                .doOnNext {
                    size += it.size
                }
                .flatMap { Observable.fromIterable(it) }
                .doOnNext {
                    val point = eyeDetector.findEye(it)
                    experiment.points.add(PointRealmObject(point))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    val id = saveExperiment(experiment)

                    view.showChartFragment(id)

                    Handler().postDelayed({
                        view.hideProgress()
                    }, 500)

                }
                .subscribe({
                    index++

                    val text = "$index /" + size
                    val progress = 100 * index / size


                    view.countProgress(text, progress)
                }, {
                    it.printStackTrace()

                    view.showErrorMessage(it.message)
                }))


    }


    override fun cancel() {
        unsubscribe()
    }


    private fun saveExperiment(experiment: ExperimentItem): Long {
        val expNumber = mRealm.where(ExperimentItem::class.java).findAll().size + 1
        experiment.name = "Эксперимент №$expNumber"

        val timeStamp = Date().time

        experiment.date = timeStamp
        experiment.id = timeStamp

        mRealm.saveEyeExperiment(experiment)

        return timeStamp
    }
}