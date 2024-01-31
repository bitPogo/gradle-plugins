/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.component

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

internal interface CustomComponentContract {
    interface Extension {
        val scope: Property<String>
        val customArtifacts: SetProperty<CustomComponentApiContract.Artifact>
        val attributes: MapProperty<Any, Any>
    }

    companion object {
        const val EXTENSION_ID = "antibytesCustomComponent"
    }
}
