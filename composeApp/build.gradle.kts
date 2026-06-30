import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import tasks.GenerateBuildPropsTask
import tasks.GenerateSecretsTask
import tasks.UpdatePlistVersionTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

kotlin {
    android {
        namespace = "com.patorika.universalremote.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            languageVersion = KotlinVersion.KOTLIN_2_3
            jvmTarget.set(JvmTarget.JVM_11)
        }
        withHostTest {}
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
            implementation(libs.compose.navigationevent)

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

            implementation(projects.featureBluetoothApi)
            implementation(projects.featureBluetoothPresentation)
            implementation(projects.featureBluetoothManager)

            implementation(projects.featureGeneralMenuApi)
            implementation(projects.featureGeneralMenuPresentation)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
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
