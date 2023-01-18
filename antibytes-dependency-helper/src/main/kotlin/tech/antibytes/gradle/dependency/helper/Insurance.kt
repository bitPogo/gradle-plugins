/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

/* ktlint-disable filename */
package tech.antibytes.gradle.dependency.helper

import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolveDetails
import tech.antibytes.gradle.dependency.config.MainConfig

private val modules = listOf(
    "kotlin-stdlib-jdk7",
    "kotlin-stdlib-jdk8",
    "kotlin-stdlib",
    "kotlin-stdlib-common",
    "kotlin-stdlib-js",
    "kotlin-stdlib-jvm",
    "kotlin-stdlib-wasm",
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
                useVersion(version ?: MainConfig.kotlin)
                because("Avoid resolution conflicts")
            }
        }
    }
}
