/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.quality.api

import tech.antibytes.gradle.quality.QualityApiContract

data class StableApiConfiguration(
    override val excludePackages: Set<String> = emptySet(),
    override val excludeProjects: Set<String> = setOf("docs"),
    override val excludeClasses: Set<String> = emptySet(),
    override val nonPublicMarkers: Set<String> = emptySet(),
) : QualityApiContract.StableApiConfiguration
