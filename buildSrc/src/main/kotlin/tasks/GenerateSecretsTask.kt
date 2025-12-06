package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.util.Properties

@CacheableTask
abstract class GenerateSecretsTask : DefaultTask() {
    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val secretsFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun generate() {
        val props = Properties()

        val secrets = secretsFile.orNull?.asFile
        if (secrets != null && secrets.exists()) {
            secrets.inputStream().use { props.load(it) }
        }

        val email = props.getProperty("EMAIL", "invalid")
        val phone = props.getProperty("PHONE", "invalid")

        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            package com.patorika.app

            object Secrets {
                const val EMAIL: String = "$email"
                const val PHONE: String = "$phone"
            }
            """.trimIndent() + "\n",
        )
    }
}
