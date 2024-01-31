/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing

import tech.antibytes.gradle.versioning.api.VersioningConfiguration

abstract class AntibytesPublishingPluginExtension : PublishingContract.PublishingPluginExtension {
    init {
        versioning.convention(VersioningConfiguration())
        dryRun.convention(false)
        standalone.convention(false)

        packaging.convention(null)
        repositories.convention(mutableSetOf())

        documentation.convention(null)

        signing.convention(null)

        excludeProjects.convention(mutableSetOf())

        additionalPublishingTasks.convention(emptyMap())
    }
}
