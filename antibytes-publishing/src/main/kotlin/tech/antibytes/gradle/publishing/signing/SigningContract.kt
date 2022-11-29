/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.signing

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract

internal interface SigningContract {
    interface CommonSignature {
        fun configure(project: Project)
    }

    interface MemorySignature {
        fun configure(
            project: Project,
            configuration: PublishingApiContract.MemorySigning,
        )
    }
}
