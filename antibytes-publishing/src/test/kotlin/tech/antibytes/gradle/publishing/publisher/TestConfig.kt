/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract

data class TestConfig(
    override var registryConfiguration: Set<PublishingApiContract.RegistryConfiguration>,
    override var packageConfiguration: PublishingApiContract.PackageConfiguration?,
    override var dryRun: Boolean,
    override var excludeProjects: Set<String>,
    override var versioning: PublishingApiContract.VersioningConfiguration,
    override var standalone: Boolean
) : PublishingContract.PublishingPluginConfiguration
