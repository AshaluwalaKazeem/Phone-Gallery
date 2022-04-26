package com.androidassessment.phonegallery.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class Utils {

    companion object {
        const val baseUrl = "https://my-json-server.typicode.com/Reyst/exhibit_db/"

        fun isInternetConnected(context: Context): Boolean {
            return getConnectionType(context) != 0
        }

        /**
         * This method can be used to get the connection of a device
         * @param context Application context
         * @return It returns an int
        0: No Internet available (maybe on airplane mode, or in the process of joining an wi-fi).

        1: Cellular (mobile data, 3G/4G/LTE whatever).

        2: Wi-fi.

        3: VPN
         */
        private fun getConnectionType(context: Context): Int {
            var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                                result = 2
                            }
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                                result = 1
                            }
                            hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                                result = 3
                            }
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = 2
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = 1
                        } else if(type == ConnectivityManager.TYPE_VPN) {
                            result = 3
                        }
                    }
                }
            }
            return result
        }
    }
}