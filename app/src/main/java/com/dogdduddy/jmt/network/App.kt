package com.dogdduddy.jmt.network

import android.app.Application
import android.content.Context

class App:Application() {
    companion object {
        var instance: App? = null
        var appContext : Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }
    fun getAppContext(): App {
        checkNotNull(instance) {
            "This Application does not inherit com.dogdduddy.JMT"
        }
        return instance!!
    }
}