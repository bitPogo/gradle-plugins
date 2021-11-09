/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract

data class ContributorConfiguration(
    override val name: String,
    override val email: String,
    override val url: String? = null,
    override val additionalInformation: Map<String, String> = emptyMap()
) : PublishingApiContract.ContributorConfiguration
