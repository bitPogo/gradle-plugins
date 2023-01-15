/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

fun NamedDomainObjectContainer<KotlinSourceSet>.setupAndroidTest() {
    val androidAndroidTestRelease = getByName("androidAndroidTestRelease")

    getByName("androidAndroidTest").apply {
        dependsOn(androidAndroidTestRelease)
    }

    val androidTestFixturesDebug = getByName("androidTestFixturesDebug")
    val androidTestFixturesRelease = getByName("androidTestFixturesRelease")
    val androidTestFixtures = getByName("androidTestFixtures").apply {
        dependsOn(androidTestFixturesDebug)
        dependsOn(androidTestFixturesRelease)
    }
    getByName("androidTest").apply {
        dependsOn(androidTestFixtures)
    }
}
