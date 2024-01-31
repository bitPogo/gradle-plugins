/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.GradleException

sealed class CoverageError(
    override val message: String,
) : GradleException(message) {
    class UnknownPlatformConfiguration(message: String = "Unknown Configuration Type detected.") : CoverageError(message)
}
