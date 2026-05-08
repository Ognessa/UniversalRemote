package com.patorika.universalremote

import android.app.Application
import com.patorika.feature_bluetooth_manager.di.androidBluetoothModule
import com.patorika.feature_controller.di.androidControllerModule
import com.patorika.universalremote.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@AndroidApplication)
            modules(androidControllerModule, androidBluetoothModule, appModule)
        }
    }
}
