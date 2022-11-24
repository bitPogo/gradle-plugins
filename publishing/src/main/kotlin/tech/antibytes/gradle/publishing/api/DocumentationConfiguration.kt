/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.api

import java.io.File
import tech.antibytes.gradle.publishing.PublishingApiContract

data class DocumentationConfiguration(
    override val tasks: Set<String>,
    override val outputDir: File,
) : PublishingApiContract.DocumentationConfiguration
