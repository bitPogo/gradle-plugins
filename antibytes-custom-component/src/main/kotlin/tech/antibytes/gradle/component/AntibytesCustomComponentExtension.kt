/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.component

abstract class AntibytesCustomComponentExtension : CustomComponentContract.Extension {
    init {
        customArtifacts.convention(emptySet())
        attributes.convention(emptyMap())
        scope.convention("compile")
    }
}
