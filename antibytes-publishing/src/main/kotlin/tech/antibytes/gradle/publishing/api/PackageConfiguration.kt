/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingApiContract.ContributorConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.DeveloperConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.LicenseConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.PomConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.SourceControlConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.Type

data class PackageConfiguration(
    override val custom: Any? = null,
    override val artifactId: String? = null,
    override val groupId: String? = null,
    override val type: Type = Type.DEFAULT,
    override val pom: PomConfiguration,
    override val developers: List<DeveloperConfiguration>,
    override val contributors: List<ContributorConfiguration> = emptyList(),
    override val license: LicenseConfiguration,
    override val scm: SourceControlConfiguration,
) : PublishingApiContract.PackageConfiguration
