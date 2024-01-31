/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.mkdocs

import org.gradle.api.provider.Property
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration

interface MkdocsContract {
    interface Extension {
        val versioning: Property<VersioningConfiguration>
    }

    companion object {
        const val EXTENSION_ID = "antibytesDocumentation"
    }
}
