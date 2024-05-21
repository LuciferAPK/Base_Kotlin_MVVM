package com.cyrus.base_kotlin_mvvm.di

import android.content.Context
import com.cyrus.base_kotlin_mvvm.cmp.GoogleMobileAdsConsentManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providerCMP(
        @ApplicationContext context: Context,
    ) = GoogleMobileAdsConsentManager(context)
}
