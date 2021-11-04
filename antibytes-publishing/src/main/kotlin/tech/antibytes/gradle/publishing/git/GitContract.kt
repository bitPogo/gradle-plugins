/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract

internal interface GitContract {
    interface GitActions {
        fun checkout(
            project: Project,
            repository: PublishingApiContract.RepositoryConfiguration
        ): Git

        fun push(
            repository: Git,
            credentials: PublishingApiContract.Credentials,
            commitMessage: String,
            dryRun: Boolean
        ): Boolean
    }

    interface GitRepository : PublishingContract.RegistryConfigurator
}
