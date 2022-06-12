/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import tech.antibytes.gradle.versioning.VersioningContract
import tech.antibytes.gradle.versioning.api.VersioningConfiguration

abstract class AntiBytesPublishingPluginExtension : PublishingContract.PublishingPluginExtension {
    // Versioning
    override var versioning: VersioningContract.VersioningConfiguration = VersioningConfiguration()

    // Publishing
    override var dryRun: Boolean = false
    override var standalone: Boolean = false
    override var packageConfiguration: PublishingApiContract.PackageConfiguration? = null
    override var repositoryConfiguration: Set<PublishingApiContract.RepositoryConfiguration> = emptySet()

    // Documentation
    override var documentation: PublishingApiContract.Documentation? = null

    // Signing
    override var signingConfiguration: PublishingApiContract.MemorySigning? = null

    // General
    override var excludeProjects: Set<String> = emptySet()
}
