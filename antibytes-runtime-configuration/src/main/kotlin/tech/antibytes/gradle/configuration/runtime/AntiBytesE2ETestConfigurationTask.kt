/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.runtime

import java.io.File
import tech.antibytes.gradle.util.isKmp

abstract class AntiBytesE2ETestConfigurationTask : AntiBytesRuntimeConfigurationTask() {
    override val fileName: String = "E2ETestConfig"
    override val outputDirectory: File by lazy { createDirectory(determinePath()) }

    private fun determinePath(): String {
        return if (project.isKmp()) {
            "${determineSourceSet()}E2ETest"
        } else {
            "e2eTest"
        }
    }
}
