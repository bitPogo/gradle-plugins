/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

private fun KotlinMultiplatformExtension.sourceSets(
    configuration: Action<NamedDomainObjectContainer<KotlinSourceSet>>,
): Unit = (this as ExtensionAware).extensions.configure("sourceSets", configuration)

internal fun KotlinMultiplatformExtension.depends(
    targets: Set<String>,
    mainDependency: KotlinSourceSet,
    testDependency: KotlinSourceSet,
) {
    sourceSets {
        targets.forEach { target ->
            named("${target}Main") {
                dependsOn(mainDependency)
            }

            named("${target}Test") {
                dependsOn(testDependency)
            }
        }
    }
}

internal fun KotlinMultiplatformExtension.depends(
    targets: Set<String>,
    dependency: String,
) {
    sourceSets {
        targets.forEach { target ->
            named("${target}Main") {
                dependsOn(this@sourceSets.named("${dependency}Main").get())
            }

            named("${target}Test") {
                dependsOn(this@sourceSets.named("${dependency}Test").get())
            }
        }
    }
}
