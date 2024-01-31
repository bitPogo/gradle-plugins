/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.catalog.GradleBundleVersion
import tech.antibytes.gradle.dependency.config.GradleVersions

internal object Jetbrains {
    /**
     * [Multiplatform Compose](https://github.com/JetBrains/compose-jb/blob/master/CHANGELOG.md)
     */
    val compose = GradleBundleVersion(GradleVersions.composeMultiplaform)
}
