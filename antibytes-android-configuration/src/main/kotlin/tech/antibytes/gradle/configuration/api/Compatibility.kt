/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.api

import org.gradle.api.JavaVersion
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract

internal data class Compatibility(
    override val source: JavaVersion,
    override val target: JavaVersion,
) : AndroidConfigurationApiContract.Compatibility
