/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.git

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration

internal interface GitContract {
    interface GitActions {
        fun checkout(
            project: Project,
            repository: RepositoryConfiguration<String>,
        )

        fun push(
            project: Project,
            repository: RepositoryConfiguration<String>,
            commitMessage: String,
            dryRun: Boolean,
        ): Boolean
    }
}
