/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import tech.antibytes.gradle.publishing.PublishingApiContract.MemorySigning
import tech.antibytes.gradle.publishing.PublishingApiContract.PackageConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration

data class TestConfig(
    override var repositoryConfiguration: Set<RepositoryConfiguration>,
    override var packageConfiguration: PackageConfiguration?,
    override var dryRun: Boolean,
    override var excludeProjects: Set<String>,
    override var versioning: VersioningConfiguration,
    override var standalone: Boolean,
    override var signingConfiguration: MemorySigning? = null,
) : PublishingContract.PublishingPluginExtension
