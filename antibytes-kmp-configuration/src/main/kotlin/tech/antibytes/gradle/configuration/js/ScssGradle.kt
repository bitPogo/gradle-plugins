/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.js

import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

fun KotlinJsTargetDsl.enableScss() {
    compilations.getByName("main").run {
        packageJson {
            customField(
                "exports",
                mapOf(
                    "./kotlin/scss/*.scss" to mapOf(
                        "import" to "./*.scss",
                        "require" to "./*.scss",
                    ),
                ),
            )
        }
    }
}
