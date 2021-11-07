/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.plugins

class AntiBytesCoverage : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.forEach {
            println(it)
        }
    }
}
