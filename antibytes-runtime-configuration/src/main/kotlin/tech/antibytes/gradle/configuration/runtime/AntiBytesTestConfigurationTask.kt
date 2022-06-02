/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.runtime

import tech.antibytes.gradle.util.isKmp
import java.io.File

abstract class AntiBytesTestConfigurationTask : AntiBytesRuntimeConfigurationTask() {
    override val fileName: String = "TestConfig"
    override val outputDirectory: File = determinePath()

    private fun determinePath(): File {
        val target = if (project.isKmp()) {
            "commonTest"
        } else {
            "test"
        }

        if (!project.buildDir.exists()) {
            project.buildDir.mkdir()
        }

        return File(
            "${project.buildDir.absolutePath.trimEnd('/')}/generated/antibytes/$target/kotlin"
        )
    }
}
