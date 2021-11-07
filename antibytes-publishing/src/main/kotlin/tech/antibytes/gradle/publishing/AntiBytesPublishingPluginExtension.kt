/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import tech.antibytes.gradle.publishing.api.VersioningConfiguration

abstract class AntiBytesPublishingPluginExtension : PublishingContract.PublishingPluginConfiguration {
    // Versioning
    override var versioning: PublishingApiContract.VersioningConfiguration = VersioningConfiguration()

    // Publishing
    override var dryRun: Boolean = false
    override var packageConfiguration: PublishingApiContract.PackageConfiguration? = null
    override var registryConfiguration: Set<PublishingApiContract.RegistryConfiguration> = emptySet()

    // General
    override var excludeProjects: Set<String> = emptySet()
}
