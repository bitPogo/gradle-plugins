/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget

fun KotlinJsTarget.enableSourceMaps() {
    compilations.forEach { compilation ->
        compilation.compileTaskProvider.get().apply {
            kotlinOptions.sourceMap = true
            kotlinOptions.metaInfo = true
        }
    }
}
