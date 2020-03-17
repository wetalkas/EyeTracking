package com.wet.eyetracking.di

import android.content.Context
import com.wet.eyetracking.App
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    fun provideRealm(): Realm = Realm.getDefaultInstance()
}