/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.script

/**
 * Dependency update check task using [DependencyUpdates](https://github.com/ben-manes/gradle-versions-plugin)
 *
 * Install:
 *
 * You need to add following dependencies to the buildSrc/build.gradle.kts
 *
 * dependencies {
 *     implementation("com.github.ben-manes:gradle-versions-plugin:0.39.0")
 * }
 *
 * Now just add id("tech.antibytes.gradle.plugin.script.dependency-updates") to your project module build.gradle.kts plugins section
 *
 * plugins {
 *     id("tech.antibytes.gradle.plugin.script.dependency-updates")
 * }
 *
 * Usage:
 * - ./gradlew dependencyUpdates
 */
plugins {
    id("com.github.ben-manes.versions")
}

tasks.named<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>("dependencyUpdates")
    .configure {
        // reject all non stable versions and disallow release candidates as upgradable versions from stable versions
        resolutionStrategy {
            componentSelection {
                all {
                    if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                        reject("Release candidate")
                    }
                }
            }
        }
    }

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
