/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import tech.antibytes.gradle.publishing.publicApi.VersioningConfiguration

abstract class AntiBytesPublishingPluginExtension : PublishingContract.PublishingPluginConfiguration {
    // Versioning
    abstract override val versioning: Property<PublishingApiContract.VersioningConfiguration>

    // Publishing
    abstract override val dryRun: Property<Boolean>
    abstract override val packageConfiguration: Property<PublishingApiContract.PackageConfiguration>
    abstract override val registryConfiguration: SetProperty<PublishingApiContract.RegistryConfiguration>

    // General
    abstract override val excludeProjects: SetProperty<String>

    init {
        versioning.convention(VersioningConfiguration())

        dryRun.convention(false)
        packageConfiguration.convention(null)
        registryConfiguration.convention(emptySet())

        excludeProjects.convention(emptySet())
    }
}
