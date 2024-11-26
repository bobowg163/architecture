package com.example.architecture

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * @项目 architecture
 * ＠包 com.example.architecture
 * @作者 bobo
 * @日期及日间 2024/11/25 21:00
 **/

@HiltAndroidApp
class ArchiteApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}