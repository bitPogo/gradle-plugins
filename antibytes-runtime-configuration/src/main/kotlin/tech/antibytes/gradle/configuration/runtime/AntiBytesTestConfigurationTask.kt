/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.runtime

import java.io.File
import tech.antibytes.gradle.util.isKmp

abstract class AntiBytesTestConfigurationTask : AntiBytesRuntimeConfigurationTask() {
    override val fileName: String = "TestConfig"
    override val outputDirectory: File by lazy { createDirectory(determinePath()) }

    private fun determinePath(): String {
        return if (project.isKmp()) {
            "${determineSourceSet()}Test"
        } else {
            "test"
        }
    }
}
