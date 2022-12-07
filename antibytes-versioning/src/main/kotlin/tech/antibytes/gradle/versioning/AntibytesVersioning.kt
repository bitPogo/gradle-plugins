/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
package tech.antibytes.gradle.versioning

import org.gradle.api.Plugin
import org.gradle.api.Project

class AntibytesVersioning : Plugin<Project> {
    override fun apply(target: Project) {
        if (!target.plugins.hasPlugin("com.palantir.git-version")) {
            target.plugins.apply("com.palantir.git-version")
        }
    }
}
