/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.maven

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract

internal interface MavenContract {
    fun interface MavenPublisher {
        fun configure(
            project: Project,
            configuration: PublishingApiContract.PackageConfiguration
        )
    }

    fun interface MavenRegistry : PublishingContract.RegistryConfigurator
}
