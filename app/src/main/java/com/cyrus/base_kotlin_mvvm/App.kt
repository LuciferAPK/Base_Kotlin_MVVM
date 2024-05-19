package com.cyrus.base_kotlin_mvvm

import android.app.Application
import android.content.Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
        fun getContext(): Context = instance.applicationContext
    }
}
