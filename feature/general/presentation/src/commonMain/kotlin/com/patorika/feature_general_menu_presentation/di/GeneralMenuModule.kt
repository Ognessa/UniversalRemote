package com.patorika.feature_general_menu_presentation.di

import com.patorika.core.navigation.NavDrawerScreenBuilder
import com.patorika.core.provider.navigation.manager.AppNavigationManager
import com.patorika.feature_general_menu_api.GeneralMenuScreenBuilder
import com.patorika.feature_general_menu_presentation.api.GeneralMenuScreenBuilderImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val generalMenuModule =
    module {
        factory<GeneralMenuScreenBuilder> {
            GeneralMenuScreenBuilderImpl(
                appNavigationManager = get<AppNavigationManager>(),
            )
        } bind NavDrawerScreenBuilder::class
    }
