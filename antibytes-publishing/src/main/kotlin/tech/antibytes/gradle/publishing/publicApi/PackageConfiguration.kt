/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publicApi

import tech.antibytes.gradle.publishing.PublishingApiContract

data class PackageConfiguration(
    override val artifactId: String?,
    override val groupId: String?,
    override val pom: PublishingApiContract.PomConfiguration,
    override val developers: List<PublishingApiContract.DeveloperConfiguration>,
    override val contributors: List<PublishingApiContract.ContributorConfiguration>,
    override val license: PublishingApiContract.LicenseConfiguration,
    override val scm: PublishingApiContract.SourceControlConfiguration
) : PublishingApiContract.PackageConfiguration
