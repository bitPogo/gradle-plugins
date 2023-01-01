/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun KotlinMultiplatformExtension.depends(
    targets: Set<String>,
    mainDependency: KotlinSourceSet,
    testDependency: KotlinSourceSet,
) {
    targets.forEach { name ->
        sourceSets.named("${name}Main") {
            dependsOn(mainDependency)
        }

        sourceSets.named("${name}Test") {
            dependsOn(testDependency)
        }
    }
}

internal fun KotlinMultiplatformExtension.depends(
    targets: Set<String>,
    dependency: String,
) = depends(
    targets,
    sourceSets.named("${dependency}Main").get(),
    sourceSets.named("${dependency}Test").get(),
)
