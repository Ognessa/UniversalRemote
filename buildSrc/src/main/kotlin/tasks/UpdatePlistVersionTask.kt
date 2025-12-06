package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

abstract class UpdatePlistVersionTask : DefaultTask() {
    @get:Input
    abstract val versionName: Property<String>

    @get:Input
    abstract val versionCode: Property<String>

    @get:InputFile
    abstract val plistFile: RegularFileProperty

    @TaskAction
    fun update() {
        val file = plistFile.get().asFile
        var content = file.readText()

        content =
            content.replace(
                Regex("<key>CFBundleShortVersionString</key>\\s*<string>.*?</string>"),
                "<key>CFBundleShortVersionString</key>\n    <string>${versionName.get()}</string>",
            )

        content =
            content.replace(
                Regex("<key>CFBundleVersion</key>\\s*<string>.*?</string>"),
                "<key>CFBundleVersion</key>\n    <string>${versionCode.get()}</string>",
            )

        file.writeText(content)
    }
}
