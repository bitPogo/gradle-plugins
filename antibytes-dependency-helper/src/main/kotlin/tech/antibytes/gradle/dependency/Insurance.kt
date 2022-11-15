/* ktlint-disable filename */
/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolveDetails
import tech.antibytes.gradle.dependency.config.DependencyConfig

private val modules = listOf(
    "kotlin-stdlib-jdk7",
    "kotlin-stdlib-jdk8",
    "kotlin-stdlib",
    "kotlin-stdlib-common",
    "kotlin-reflect",
)

private fun DependencyResolveDetails.overrideVersion(excludes: List<String>): Boolean {
    return requested.group == "org.jetbrains.kotlin" &&
        requested.name in modules &&
        excludes.none { excluded -> target.name.startsWith(excluded) }
}

fun Project.ensureKotlinVersion(
    version: String? = null,
    excludes: List<String> = emptyList(),
) {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (overrideVersion(excludes)) {
                useVersion(version ?: DependencyConfig.kotlin)
                because("Avoid resolution conflicts")
            }
        }
    }
}
