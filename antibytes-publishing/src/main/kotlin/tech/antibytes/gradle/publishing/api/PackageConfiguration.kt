/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingApiContract.Type
import tech.antibytes.gradle.publishing.PublishingApiContract.PomConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.DeveloperConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.ContributorConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.LicenseConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.SourceControlConfiguration

data class PackageConfiguration(
    override val artifactId: String? = null,
    override val groupId: String? = null,
    override val type: Type = Type.MIXED,
    override val pom: PomConfiguration,
    override val developers: List<DeveloperConfiguration>,
    override val contributors: List<ContributorConfiguration> = emptyList(),
    override val license: LicenseConfiguration,
    override val scm: SourceControlConfiguration,
) : PublishingApiContract.PackageConfiguration
