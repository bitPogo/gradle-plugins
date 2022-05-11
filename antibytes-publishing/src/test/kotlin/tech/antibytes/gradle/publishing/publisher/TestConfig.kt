/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.SigningApiContract

data class TestConfig(
    override var repositoryConfiguration: Set<PublishingApiContract.RepositoryConfiguration>,
    override var packageConfiguration: PublishingApiContract.PackageConfiguration?,
    override var dryRun: Boolean,
    override var excludeProjects: Set<String>,
    override var versioning: PublishingApiContract.VersioningConfiguration,
    override var standalone: Boolean,
    override var signingConfiguration: SigningApiContract.MemorySigning? = null,
) : PublishingContract.PublishingPluginExtension
