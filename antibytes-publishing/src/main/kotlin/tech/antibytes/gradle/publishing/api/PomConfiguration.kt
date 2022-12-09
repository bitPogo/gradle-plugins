/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract

data class PomConfiguration(
    override val name: String,
    override val description: String,
    override val year: Int,
    override val url: String,
    override val packageing: String? = null,
    override val additionalInformation: Map<String, String> = emptyMap(),
) : PublishingApiContract.PomConfiguration
