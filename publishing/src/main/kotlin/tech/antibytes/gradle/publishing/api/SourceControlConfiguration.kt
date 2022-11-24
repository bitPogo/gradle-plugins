/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract

data class SourceControlConfiguration(
    override val connection: String,
    override val developerConnection: String,
    override val url: String,
) : PublishingApiContract.SourceControlConfiguration
