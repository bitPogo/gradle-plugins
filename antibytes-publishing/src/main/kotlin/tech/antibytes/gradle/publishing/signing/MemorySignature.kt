/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.signing

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension
import tech.antibytes.gradle.publishing.PublishingApiContract

internal object MemorySignature : SigningContract.MemorySignature {
    override fun configure(
        project: Project,
        configuration: PublishingApiContract.MemorySigning,
    ) {
        project.extensions.configure(SigningExtension::class.java) {
            when (configuration) {
                is PublishingApiContract.CompleteMemorySigning -> useInMemoryPgpKeys(
                    configuration.keyId,
                    configuration.key,
                    configuration.password,
                )
                else -> useInMemoryPgpKeys(
                    configuration.key,
                    configuration.password,
                )
            }

            sign(project.extensions.getByType<PublishingExtension>().publications)
        }
    }
}
