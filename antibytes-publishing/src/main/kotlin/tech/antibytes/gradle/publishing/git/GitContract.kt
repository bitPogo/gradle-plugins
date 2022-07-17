/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract

internal interface GitContract {
    interface GitActions {
        fun checkout(
            project: Project,
            repository: PublishingApiContract.RepositoryConfiguration,
        )

        fun push(
            project: Project,
            repository: PublishingApiContract.RepositoryConfiguration,
            commitMessage: String,
            dryRun: Boolean,
        ): Boolean
    }
}
