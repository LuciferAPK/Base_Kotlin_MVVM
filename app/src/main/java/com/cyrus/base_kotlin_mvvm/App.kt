package com.cyrus.base_kotlin_mvvm

import android.app.Application
import android.content.Context
import com.cyrus.base_kotlin_mvvm.cmp.GoogleMobileAdsConsentManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
        fun getContext(): Context = instance.applicationContext
        fun getGoogleMobileAdsConsent() = instance.googleMobileAdsConsentManager
    }
}
