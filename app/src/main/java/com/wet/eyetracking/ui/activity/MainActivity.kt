package com.wet.eyetracking.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.wet.eyetracking.App
import com.wet.eyetracking.R

import com.wet.eyetracking.mvp.contract.EyeDetectorContract
import com.wet.eyetracking.mvp.presenter.EyeDetectorPresenter
import com.wet.eyetracking.ui.fragment.ExperimentFragment
import com.wet.eyetracking.ui.fragment.HistoryFragment
import kotlinx.android.synthetic.main.activity_main.*

import javax.inject.Inject

class MainActivity : AppCompatActivity(), EyeDetectorContract.View {

    private val PHOTO_PICK_SUCCESS = 12345

    @Inject
    lateinit var presenter: EyeDetectorPresenter


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appComponent.inject(this)
        presenter.attach(this)

        btnStart.setOnClickListener {
            startActivityForResult(presenter.createFilePickerIntent(this@MainActivity), PHOTO_PICK_SUCCESS)
        }

        btnCancel.setOnClickListener {
            presenter.cancel()
        }

        btnHistory.setOnClickListener {
            presenter.openHistory()
        }

        btnAbout.setOnClickListener { }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_PICK_SUCCESS && resultCode == Activity.RESULT_OK) {
            val files = com.nononsenseapps.filepicker.Utils.getSelectedFilesFromResult(data!!)
            presenter.detectEye(files)
        }
    }


    override fun showProgress() {
        btnStart.isEnabled = false
        tvStart.text = "Обработка..."
        progressContainer.visibility = View.VISIBLE
        btnHistory.visibility = View.GONE
        btnAbout.visibility = View.INVISIBLE
        btnCancel.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        btnStart.isEnabled = true
        progressContainer.visibility = View.GONE
        tvStart.text = getString(R.string.start)
        btnCancel.visibility = View.GONE
        btnHistory.visibility = View.VISIBLE
        btnAbout.visibility = View.VISIBLE
    }


    override fun countProgress(text: String, progress: Int) {
        tvCount.text = text
        progressBar.progress = progress
    }


    override fun showErrorMessage(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showChartFragment(id: Long) {
        val chartFragment = ExperimentFragment.newInstance(id)

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, chartFragment)
                .addToBackStack(chartFragment.tag)
                .commit()
    }

    override fun showHistoryFragment() {
        val historyFragment = HistoryFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, historyFragment)
                .addToBackStack(historyFragment.tag)
                .commit()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.detach()
    }
}
