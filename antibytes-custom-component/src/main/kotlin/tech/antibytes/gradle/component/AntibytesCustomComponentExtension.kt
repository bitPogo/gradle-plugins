/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.component

abstract class AntibytesCustomComponentExtension : CustomComponentContract.Extension {
    init {
        customArtifacts.convention(emptySet())
        attributes.convention(emptyMap())
        scope.convention("compile")
    }
}
