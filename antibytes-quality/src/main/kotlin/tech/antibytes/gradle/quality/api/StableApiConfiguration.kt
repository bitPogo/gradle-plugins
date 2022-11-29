/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.api

import tech.antibytes.gradle.quality.QualityApiContract

data class StableApiConfiguration(
    override val excludePackages: Set<String> = emptySet(),
    override val excludeProjects: Set<String> = emptySet(),
    override val excludeClasses: Set<String> = emptySet(),
    override val nonPublicMarkers: Set<String> = emptySet(),
) : QualityApiContract.StableApiConfiguration
