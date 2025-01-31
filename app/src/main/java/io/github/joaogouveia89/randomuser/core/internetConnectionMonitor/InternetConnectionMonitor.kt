package io.github.joaogouveia89.randomuser.core.internetConnectionMonitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LifecycleObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

enum class InternetConnectionStatus{
    OFFLINE, ONLINE
}

class InternetConnectionMonitor(
    context: Context
): LifecycleObserver {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _status = MutableStateFlow(InternetConnectionStatus.OFFLINE)

    val status: StateFlow<InternetConnectionStatus>
        get() = _status

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _status.update {
                InternetConnectionStatus.ONLINE
            }
        }

        override fun onLost(network: Network) {
            _status.update {
                InternetConnectionStatus.OFFLINE
            }
        }
    }

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }
}