/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.signing

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.SigningApiContract

internal interface SigningContract {
    interface CommonSigning {
        fun configure(project: Project)
    }

    interface MemorySigning {
        fun configure(
            project: Project,
            configuration: SigningApiContract.MemorySigning,
        )
    }

    interface SigningController {
        fun configure(
            project: Project,
            extension: PublishingContract.PublishingPluginExtension
        )
    }
}
