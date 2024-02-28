package com.example.hiretop.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Utility class for checking network connectivity.
 */
class NetworkHelper @Inject constructor(@ApplicationContext private val context: Context) {

    /**
     * Checks if the device is connected to the internet.
     *
     * @return true if the device is connected to the internet, false otherwise.
     */
    fun isConnectedToInternet(): Boolean {
        // Get system service for managing connectivity
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get the active network
        val network = connectivityManager.activeNetwork ?: return false

        // Get network capabilities
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        // Check if the network has internet capability
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}