package com.patorika.feature_bluetooth_api

import com.patorika.core.navigation.ScreenBuilder

interface BluetoothDevicesScreenBuilder : ScreenBuilder {
    override val routeName: String
        get() = "BluetoothDevices"
}
