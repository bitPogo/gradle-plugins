/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingContract.Companion.DEPENDENCIES
import tech.antibytes.gradle.publishing.PublishingContract.Companion.EXTENSION_ID
import tech.antibytes.gradle.publishing.publisher.PublisherController
import tech.antibytes.gradle.util.applyIfNotExists
import tech.antibytes.gradle.versioning.AntibytesVersioning

class AntibytesPublishing : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create(
            EXTENSION_ID,
            AntibytesPublishingPluginExtension::class.java,
        )

        target.applyIfNotExists(*DEPENDENCIES)
        AntibytesVersioning().apply(target)

        PublisherController.configure(project = target, extension = extension)
    }
}
