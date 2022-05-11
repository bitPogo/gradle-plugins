/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.signing

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension
import tech.antibytes.gradle.publishing.SigningApiContract

internal object MemorySigning : SigningContract.MemorySigning {
    override fun configure(
        project: Project,
        configuration: SigningApiContract.MemorySigning,
    ) {
        project.extensions.configure(SigningExtension::class.java) {
            when (configuration) {
                is SigningApiContract.CompleteMemorySigning -> useInMemoryPgpKeys(
                    configuration.keyId,
                    configuration.keyPath,
                    configuration.password,
                )
                else -> useInMemoryPgpKeys(
                    configuration.keyPath,
                    configuration.password,
                )
            }

            sign(project.extensions.getByType<PublishingExtension>().publications)
        }
    }
}
