/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
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

internal fun KotlinMultiplatformExtension.dependsOnCommon(
    main: KotlinSourceSet,
    test: KotlinSourceSet,
) {
    sourceSets {
        main.dependsOn(this@sourceSets.getByName("commonMain"))
        test.dependsOn(this@sourceSets.getByName("commonTest"))
    }
}

internal fun KotlinMultiplatformExtension.depends(
    targets: Set<String>,
    mainDependency: KotlinSourceSet,
    testDependency: KotlinSourceSet,
) {
    sourceSets {
        targets.forEach { target ->
            getByName("${target}Main") {
                println(this)
                dependsOn(mainDependency)
            }

            getByName("${target}Test") {
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
            getByName("${target}Main") {
                dependsOn(this@sourceSets.getByName("${dependency}Main"))
            }

            getByName("${target}Test") {
                dependsOn(this@sourceSets.getByName("${dependency}Test"))
            }
        }
    }
}

internal fun KotlinMultiplatformExtension.wireDependencies(
    main: KotlinSourceSet,
    test: KotlinSourceSet,
    targets: Set<String>,
) {
    dependsOnCommon(
        main = main,
        test = test,
    )
    depends(
        targets = targets,
        mainDependency = main,
        testDependency = test,
    )
}
