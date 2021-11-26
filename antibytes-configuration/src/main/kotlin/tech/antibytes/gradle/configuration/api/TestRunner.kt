/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.api

import tech.antibytes.gradle.configuration.ConfigurationApiContract

data class TestRunner(
    override val runner: String,
    override val arguments: Map<String, String>
) : ConfigurationApiContract.TestRunner
