/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.util.applyIfNotExists

class AntiBytesKmpConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        target.applyIfNotExists("org.jetbrains.kotlin.multiplatform")
    }
}
