package com.wet.eyetracking.di

import com.wet.eyetracking.ui.activity.MainActivity
import com.wet.eyetracking.opencv.OpenCVLoaderCallback
import com.wet.eyetracking.mvp.presenter.EyeDetectorPresenter
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class, EyeModule::class))
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(loader: OpenCVLoaderCallback)

    fun inject(presenter: EyeDetectorPresenter)
}