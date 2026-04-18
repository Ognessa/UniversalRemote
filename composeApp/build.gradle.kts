import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import tasks.GenerateBuildPropsTask
import tasks.GenerateSecretsTask
import tasks.UpdatePlistVersionTask
import java.util.Properties
import java.io.FileInputStream

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
            implementation(libs.androidx.ui.tooling)
            implementation(libs.androidx.activity.compose)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytic)
            implementation(libs.firebase.appdistribution)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.navigation)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(projects.core)
            implementation(projects.featureController)

            implementation(projects.featureListApi)
            implementation(projects.featureListPresentation)

            implementation(projects.featureEditorApi)
            implementation(projects.featureEditorPresentation)

            implementation(projects.featureLibraryApi)
            implementation(projects.featureLibraryPresentation)

            implementation(projects.featureSignalApi)
            implementation(projects.featureSignalPresentation)

            implementation(projects.featureTitleApi)
            implementation(projects.featureTitlePresentation)

            implementation(projects.featurePlaygroundApi)
            implementation(projects.featurePlaygroundPresentation)
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

    val keystorePropertiesFile = rootProject.file("keystore/keystore.properties")
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs["release"]
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
