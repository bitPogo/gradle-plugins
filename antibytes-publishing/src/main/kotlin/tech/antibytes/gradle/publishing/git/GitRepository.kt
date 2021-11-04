/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract

internal object GitRepository : GitContract.GitRepository {
    override fun configure(
        project: Project,
        configuration: List<PublishingApiContract.RegistryConfiguration>,
        dryRun: Boolean
    ) {
        /* Do Nothing */
    }
}
