/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.junit.jupiter.api.Test

class ConfigureAndroidFixtureSpec {
    @Test
    fun `Given setupAndroidTest is called it wires the AndroidTest dependency tree`() {
        // Given
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val androidSourceSets: List<KotlinSourceSet> = listOf(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
        )

        every { sourceSets.getByName(any()) } returnsMany androidSourceSets

        // When
        sourceSets.setupAndroidTest()

        // Then
        verifyOrder {
            sourceSets.getByName("androidAndroidTestRelease")
            sourceSets.getByName("androidAndroidTest")
            androidSourceSets[1].dependsOn(androidSourceSets[0])

            sourceSets.getByName("androidTestFixturesDebug")
            sourceSets.getByName("androidTestFixturesRelease")
            sourceSets.getByName("androidTestFixtures")
            androidSourceSets[4].dependsOn(androidSourceSets[2])
            androidSourceSets[4].dependsOn(androidSourceSets[3])

            sourceSets.getByName("androidTest")
            androidSourceSets[5].dependsOn(androidSourceSets[4])
        }
    }
}
