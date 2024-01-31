/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.runtime

import java.io.File
import tech.antibytes.gradle.util.isKmp

abstract class AntiBytesMainConfigurationTask : AntiBytesRuntimeConfigurationTask() {
    override val fileName: String = "MainConfig"
    override val outputDirectory: File = determinePath()

    private fun determinePath(): File {
        val target = if (project.isKmp()) {
            "commonMain"
        } else {
            "main"
        }

        val buildDir = project.layout.buildDirectory.asFile.get()

        if (!buildDir.exists()) {
            buildDir.mkdir()
        }

        return File(
            "${buildDir.absolutePath.trimEnd('/')}/generated/antibytes/$target/kotlin",
        )
    }
}
