/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.config.GradleVersions

/**
 * [Stately](https://github.com/touchlab/Stately)
 */
internal object Stately {
    private const val version = GradleVersions.stately

    const val isolate = version
    const val freeze = version
    const val concurrency = version
    const val collections = version
}
