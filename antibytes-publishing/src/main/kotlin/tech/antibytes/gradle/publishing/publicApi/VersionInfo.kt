/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publicApi

import com.palantir.gradle.gitversion.VersionDetails

data class VersionInfo(
    val name: String,
    val details: VersionDetails
)
