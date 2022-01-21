/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.api

import tech.antibytes.gradle.publishing.PublishingApiContract

data class PackageConfiguration(
    override val artifactId: String? = null,
    override val groupId: String? = null,
    override val pom: PublishingApiContract.PomConfiguration,
    override val developers: List<PublishingApiContract.DeveloperConfiguration>,
    override val contributors: List<PublishingApiContract.ContributorConfiguration> = emptyList(),
    override val license: PublishingApiContract.LicenseConfiguration,
    override val scm: PublishingApiContract.SourceControlConfiguration
) : PublishingApiContract.PackageConfiguration
