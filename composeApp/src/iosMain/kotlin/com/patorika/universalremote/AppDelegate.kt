package com.patorika.universalremote

import com.patorika.feature_bluetooth_manager.di.iosBluetoothModule
import com.patorika.feature_controller.di.iosControllerModule
import com.patorika.universalremote.di.appModule
import org.koin.core.context.startKoin

fun initKoinIos() {
    startKoin {
        modules(listOf(iosControllerModule, iosBluetoothModule, appModule))
    }
}
