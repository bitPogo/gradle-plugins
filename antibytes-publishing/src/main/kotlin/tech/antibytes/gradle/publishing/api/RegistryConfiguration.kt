/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract

data class RegistryConfiguration(
    override val useGit: Boolean,
    override val gitWorkDirectory: String,
    override val name: String,
    override val url: String,
    override val username: String,
    override val password: String,
) : PublishingApiContract.RegistryConfiguration
