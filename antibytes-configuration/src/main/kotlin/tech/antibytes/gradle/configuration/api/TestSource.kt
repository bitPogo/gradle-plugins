/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.api

import tech.antibytes.gradle.configuration.ConfigurationApiContract

internal data class TestSource(
    override val sourceDirectories: Set<String>,
    override val resourceDirectories: Set<String>,
) : ConfigurationApiContract.TestSource
