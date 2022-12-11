/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
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
