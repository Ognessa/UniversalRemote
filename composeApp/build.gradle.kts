import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import tasks.GenerateBuildPropsTask
import tasks.GenerateSecretsTask
import tasks.UpdatePlistVersionTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytic)
            implementation(libs.firebase.appdistribution)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.patorika.universalremote"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.patorika.universalremote"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.app.version.get().toInt()
        versionName = libs.versions.app.name.get()
    }

    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/INDEX.LIST",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/DEPENDENCIES",
                "META-INF/io.netty.versions.properties"
            )
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

tasks.named("generateComposeResClass") {
    dependsOn("generateSecretsClass")
    dependsOn("generateBuildPropsClass")
    dependsOn("updatePlistVersion")
}

tasks.register<GenerateSecretsTask>("generateSecretsClass") {
    secretsFile.set(
        layout.projectDirectory.file("../secrets.properties")
    )

    outputFile.set(
        layout.projectDirectory.file(
            "src/commonMain/kotlin/com/patorika/app/Secrets.kt"
        )
    )
}

tasks.register<GenerateBuildPropsTask>("generateBuildPropsClass") {
    versionCode.set(libs.versions.app.version.map { it.toInt() })
    versionName.set(libs.versions.app.name)

    outputFile.set(
        layout.projectDirectory.file(
            "src/commonMain/kotlin/com/patorika/app/BuildProps.kt"
        )
    )
}

tasks.register<UpdatePlistVersionTask>("updatePlistVersion") {
    versionName.set(libs.versions.app.name)
    versionCode.set(libs.versions.app.version)

    plistFile.set(
        layout.projectDirectory.file("../iosApp/iosApp/Info.plist")
    )
}
