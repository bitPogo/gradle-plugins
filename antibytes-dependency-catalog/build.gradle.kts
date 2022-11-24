/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
import tech.antibytes.gradle.dependency.catalog.addSharedAntibytesConfiguration
import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration
import tech.antibytes.gradle.publishing.api.DeveloperConfiguration
import tech.antibytes.gradle.publishing.api.LicenseConfiguration
import tech.antibytes.gradle.publishing.api.SourceControlConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.Type

plugins {
    `version-catalog`

    id("tech.antibytes.gradle.publishing.local")
}

antiBytesPublishing {
    packageConfiguration = PackageConfiguration(
        type = Type.VERSION_CATALOG,
        pom = PomConfiguration(
            name = "antibytes-dependency-catalog",
            description = "General dependencies for Antibytes projects.",
            year = 2022,
            url = LibraryConfig.publishing.url,
        ),
        developers = listOf(
            DeveloperConfiguration(
                id = LibraryConfig.publishing.developerId,
                name = LibraryConfig.publishing.developerName,
                url = LibraryConfig.publishing.developerUrl,
                email = LibraryConfig.publishing.developerEmail,
            )
        ),
        license = LicenseConfiguration(
            name = LibraryConfig.publishing.licenseName,
            url = LibraryConfig.publishing.licenseUrl,
            distribution = LibraryConfig.publishing.licenseDistribution,
        ),
        scm = SourceControlConfiguration(
            url = LibraryConfig.publishing.scmUrl,
            connection = LibraryConfig.publishing.scmConnection,
            developerConnection = LibraryConfig.publishing.scmDeveloperConnection,
        ),
    )
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

catalog {
    addSharedAntibytesConfiguration()
}
