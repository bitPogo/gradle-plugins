/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract

data class MemorySigningConfiguration(
    override val key: String?,
    override val password: String?,
) : PublishingApiContract.MemorySigning
