/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import tech.antibytes.gradle.publishing.api.VersioningConfiguration

abstract class AntiBytesPublishingPluginExtension : PublishingContract.PublishingPluginExtension {
    // Versioning
    override var versioning: PublishingApiContract.VersioningConfiguration = VersioningConfiguration()

    // Publishing
    override var dryRun: Boolean = false
    override var standalone: Boolean = false
    override var packageConfiguration: PublishingApiContract.PackageConfiguration? = null
    override var repositoryConfiguration: Set<PublishingApiContract.RepositoryConfiguration> = emptySet()

    // Signing
    override var signingConfiguration: PublishingApiContract.MemorySigning? = null

    // General
    override var excludeProjects: Set<String> = emptySet()
}
