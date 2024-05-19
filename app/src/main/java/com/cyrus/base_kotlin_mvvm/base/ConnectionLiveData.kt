package com.cyrus.base_kotlin_mvvm.base

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

class ConnectionLiveData(private val context: Context) : LiveData<Boolean>() {
    private var connectivityManager: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val networkRequestBuilder: NetworkRequest.Builder = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

    override fun onActive() {
        super.onActive()
        updateConnection()
        context.registerReceiver(
            networkReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(
                getConnectivityMarshmallowManagerCallback()
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> marshmallowNetworkAvailableRequest()
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        } else {
            context.unregisterReceiver(networkReceiver)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkAvailableRequest() {
        connectivityManager.registerNetworkCallback(
            networkRequestBuilder.build(),
            getConnectivityLollipopManagerCallback()
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun marshmallowNetworkAvailableRequest() {
        connectivityManager.registerNetworkCallback(
            networkRequestBuilder.build(),
            getConnectivityMarshmallowManagerCallback()
        )
    }

    private fun getConnectivityLollipopManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    postValue(true)
                }

                override fun onLost(network: Network) {
                    postValue(false)
                }

                override fun onUnavailable() {
                    postValue(false)
                }
            }
            return connectivityManagerCallback
        } else {
            throw IllegalAccessError("Accessing wrong API version")
        }
    }

    private fun getConnectivityMarshmallowManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    networkCapabilities.let { capabilities ->
                        if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(
                                NetworkCapabilities.NET_CAPABILITY_VALIDATED
                            )
                        ) {
                            /*val speedDownload = capabilities.linkDownstreamBandwidthKbps
                            val speedUpload = capabilities.linkUpstreamBandwidthKbps
                            ApplicationContext.getNetworkContext()
                                .updateNetworkType(speedDownload, speedUpload)*/
                        }
                    }
                }

                override fun onLost(network: Network) {
                    postValue(false)
                }

                override fun onUnavailable() {
                    postValue(false)
                }
            }
            return connectivityManagerCallback
        } else {
            throw IllegalAccessError("Accessing wrong API version")
        }
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }

    @Suppress("DEPRECATION")
    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        /*if (ApplicationContext.getNetworkContext().isNetworkConnected != NetworkUtils.isConnected() || !NetworkUtils.isConnected()) {
                LoggerUtils.d("TAG", "POST_INTERNET::updateConnection: ${activeNetwork?.isConnectedOrConnecting}")*/
        postValue(activeNetwork?.isConnectedOrConnecting == true)
        /*ApplicationContext.getNetworkContext().isNetworkConnected = NetworkUtils.isConnected()
    }*/

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
            ) {
                val speedDownload = capabilities.linkDownstreamBandwidthKbps
                val speedUpload = capabilities.linkUpstreamBandwidthKbps
                /*ApplicationContext.getNetworkContext().updateNetworkType(speedDownload, speedUpload)*/
            }
        }
    }
}