package com.patorika.universalremote

import android.app.Application
import com.patorika.feature_controller.di.androidControllerModule
import com.patorika.universalremote.di.appModule
import org.koin.core.context.startKoin

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            modules(appModule, androidControllerModule)
        }
    }
}
