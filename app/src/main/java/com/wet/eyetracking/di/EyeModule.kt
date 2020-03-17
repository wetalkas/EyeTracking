package com.wet.eyetracking.di

import com.wet.eyetracking.opencv.EyeDetector
import com.wet.eyetracking.mvp.presenter.EyeDetectorPresenter
import dagger.Module
import dagger.Provides
import org.opencv.objdetect.CascadeClassifier
import javax.inject.Singleton

@Module
class EyeModule {
    @Provides
    @Singleton
    fun provideCascade(): CascadeClassifier = CascadeClassifier()

    @Provides
    @Singleton
    fun provideEyeDetector(cascade: CascadeClassifier): EyeDetector = EyeDetector(cascade)

    @Provides
    @Singleton
    fun provideEyePresenter(): EyeDetectorPresenter = EyeDetectorPresenter()
}