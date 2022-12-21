/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.api

import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract

internal data class MainSource(
    override val manifest: String,
    override val sourceDirectories: Set<String>,
    override val resourceDirectories: Set<String>,
) : AndroidConfigurationApiContract.MainSource
