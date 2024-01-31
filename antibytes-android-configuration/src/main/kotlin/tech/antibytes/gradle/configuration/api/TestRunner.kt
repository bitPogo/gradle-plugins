/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.api

import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract

internal data class TestRunner(
    override val runner: String,
    override val arguments: Map<String, String>,
) : AndroidConfigurationApiContract.TestRunner
