/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.versioning.api

import com.palantir.gradle.gitversion.VersionDetails

data class VersionInfo(
    val name: String,
    val details: VersionDetails,
)
