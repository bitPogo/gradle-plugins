/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning

import org.gradle.api.provider.Property

internal interface VersioningInternalContract {
    interface Extension {
        val configuration: Property<VersioningContract.VersioningConfiguration?>
    }

    companion object {
        const val EXTENSION_ID = "antibytesVersioning"
    }
}
