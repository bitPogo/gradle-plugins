/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.versioning

internal interface VersioningInternalContract {
    interface Extension {
        var configuration: VersioningContract.VersioningConfiguration?
    }

    companion object {
        const val EXTENSION_ID = "antibytesVersioning"
    }
}
