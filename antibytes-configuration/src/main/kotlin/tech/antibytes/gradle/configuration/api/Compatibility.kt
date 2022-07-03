/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.api

import org.gradle.api.JavaVersion
import tech.antibytes.gradle.configuration.ConfigurationApiContract

internal data class Compatibility(
    override val source: JavaVersion,
    override val target: JavaVersion,
) : ConfigurationApiContract.Compatibility
