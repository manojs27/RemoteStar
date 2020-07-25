package com.laserfarm.common

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.example.videofeed.network.RestClient
import com.example.videofeed.network.SosService
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


object CommonUtils {

    fun ifConnectionNotAvailable(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert!!")
        builder.setMessage(message)

        builder.setPositiveButton("Settings") { dialog, which ->
            val i = Intent(Settings.ACTION_WIFI_SETTINGS)
            context.startActivity(i)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    fun getSOService(context: Context): SosService {
        return RestClient.getClient(getBaseUrl(), context)!!.create(SosService::class.java)
    }

    fun getBaseUrl(): String {
        return "https://run.mocky.io/"
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                    return true
                }
            }
        }
        return false
    }

}