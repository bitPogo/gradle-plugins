/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract

data class DeveloperConfiguration(
    override val id: String,
    override val name: String,
    override val email: String,
    override val url: String? = null,
    override val additionalInformation: Map<String, String> = emptyMap(),
) : PublishingApiContract.DeveloperConfiguration
