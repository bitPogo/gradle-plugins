/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import tech.antibytes.gradle.publishing.PublishingApiContract.DocumentationConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.MemorySigning
import tech.antibytes.gradle.publishing.PublishingApiContract.PackageConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration

data class TestConfig(
    override val excludeProjects: SetProperty<String>,
    override val versioning: Property<VersioningConfiguration>,
    override val repositories: SetProperty<RepositoryConfiguration<out Any>>,
    override val packaging: Property<PackageConfiguration>,
    override val additionalPublishingTasks: MapProperty<String, Set<String>>,
    override val dryRun: Property<Boolean>,
    override val standalone: Property<Boolean>,
    override val documentation: Property<DocumentationConfiguration> = makeProperty(
        DocumentationConfiguration::class.java,
        null,
    ),
    override val signing: Property<MemorySigning> = makeProperty(MemorySigning::class.java, null),
) : PublishingContract.PublishingPluginExtension
