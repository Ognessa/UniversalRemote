package com.patorika.feature_controller.presentation.ext

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun generateControllerId(): String = Uuid.random().toString()
