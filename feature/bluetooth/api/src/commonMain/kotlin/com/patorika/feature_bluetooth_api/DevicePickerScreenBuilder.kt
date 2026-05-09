package com.patorika.feature_bluetooth_api

import com.patorika.core.navigation.ScreenBuilder

interface DevicePickerScreenBuilder : ScreenBuilder {
    override val routeName: String
        get() = "DevicePicker"
}
