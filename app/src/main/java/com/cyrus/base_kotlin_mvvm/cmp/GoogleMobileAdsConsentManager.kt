package com.cyrus.base_kotlin_mvvm.cmp

import android.app.Activity
import android.content.Context
import com.cyrus.base_kotlin_mvvm.utils.LoggerUtils
import com.google.android.ump.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class GoogleMobileAdsConsentManager @Inject constructor(context: Context) {

    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    /** Interface definition for a callback to be invoked when consent gathering is complete. */
    fun interface OnConsentGatheringCompleteListener {
        fun consentGatheringComplete(error: FormError?)
    }

    /** Helper variable to determine if the app can request ads. */
    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    private val isShownConsentForm = AtomicBoolean(false)

    /** Helper variable to determine if the privacy options form is required. */
    val isPrivacyOptionsRequired: Boolean
        get() = consentInformation.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    /**
     * Helper method to call the UMP SDK methods to request consent information and load/show a
     * consent form if necessary.
     */
    fun gatherConsent(
        activity: Activity,
        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener,
    ) {
        val startTimeRequest = System.currentTimeMillis()

        if (isShownConsentForm.get()) {
            LoggerUtils.d(TAG, "##### BAILS shown one time")
            onConsentGatheringCompleteListener.consentGatheringComplete(null)
            return
        }
        // For testing purposes, you can force a DebugGeography of EEA or NOT_EEA.
        val debugSettings =
            ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                // Check your logcat output for the hashed device ID e.g.
                // "Use new ConsentDebugSettings.Builder().addTestDeviceHashedId("ABCDEF012345")" to use
                // the debug functionality.
                .addTestDeviceHashedId("1EB061C0F8CCF91E7C5895D67249D652")
                .addTestDeviceHashedId("76C5E339518E00036C77F39FD6CE88F1")
                .build()

//		val params =
//			ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).build()

        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()
        // Requesting an update to consent information should be called on every app launch.
        LoggerUtils.d(TAG, "##### START requestConsentInfoUpdate")

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                LoggerUtils.d(TAG, "##### FINISH requestConsentInfoUpdate")
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    LoggerUtils.d(TAG, "##### FINISH show Consent Form")
                    isShownConsentForm.set(true)
                    // Consent has been gathered.
                    onConsentGatheringCompleteListener.consentGatheringComplete(formError)
                }
            },
            { requestConsentError ->
                LoggerUtils.d(TAG, "##### ERROR RequestConsentInfo")
                onConsentGatheringCompleteListener.consentGatheringComplete(requestConsentError)
            }
        )
    }

    /** Helper method to call the UMP SDK method to show the privacy options form. */
    fun showPrivacyOptionsForm(
        activity: Activity,
        onConsentFormDismissedListener: ConsentForm.OnConsentFormDismissedListener,
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
    }

    companion object {
        private val TAG = "GoogleMobileAdsConsentManager"
    }
}
