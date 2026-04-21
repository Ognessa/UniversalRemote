package com.patorika.feature_bluetooth_presentation.usecase

import com.patorika.feature_bluetooth_presentation.model.BluetoothDeviceModel

class GetBluetoothDevicesUseCase {
    fun execute(): Result<List<BluetoothDeviceModel>> =
        Result.success(
            listOf(
                BluetoothDeviceModel(
                    name = "Device 1",
                    address = "AA:BB:CC:D1",
                ),
                BluetoothDeviceModel(
                    name = "Device 2",
                    address = "AA:BB:CC:D2",
                ),
                BluetoothDeviceModel(
                    name = "Device 3",
                    address = "AA:BB:CC:D3",
                ),
                BluetoothDeviceModel(
                    name = "Device 4",
                    address = "AA:BB:CC:D4",
                ),
            ),
        )
}
