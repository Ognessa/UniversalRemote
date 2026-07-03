import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.buildkonfig)
}

buildkonfig {
    packageName = "com.patorika.feature_general_menu_presentation"
    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "VERSION_NAME", libs.versions.app.name.get())
        buildConfigField(FieldSpec.Type.STRING, "VERSION_CODE", libs.versions.app.version.get())
    }
}

kotlin {
    android {
        namespace = "com.patorika.feature_general_menu_presentation"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            languageVersion = KotlinVersion.KOTLIN_2_3
            jvmTarget.set(JvmTarget.JVM_11)
        }
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        withHostTest {}
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "feature-general-menu-presentationKit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.compose.navigation)

                implementation(libs.kermit)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(projects.core)
                implementation(projects.featureGeneralMenuApi)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        androidMain {
            dependencies {
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}
