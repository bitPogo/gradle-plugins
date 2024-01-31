/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract

data class GitRepositoryConfiguration(
    override val gitWorkDirectory: String,
    override val name: String,
    override val url: String,
    override val username: String? = null,
    override val password: String? = null,
) : PublishingApiContract.GitRepositoryConfiguration
