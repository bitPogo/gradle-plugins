/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.TestNamedProvider
import tech.antibytes.gradle.test.invokeGradleAction

class SourceHooksIosSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given iosx is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(relaxed = true)
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val iosSimulator: KotlinSourceSet = mockk(relaxed = true)
        val ios: KotlinSourceSet = mockk()
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.named(any()) } returns TestNamedProvider(ios)
        invokeGradleAction(iosSimulator, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }

        // When
        extension.iosx(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.ios(prefix, configuration) }
        verify(exactly = 1) { extension.iosSimulatorArm64("${prefix}SimulatorArm64", configuration) }
        verify(exactly = 1) { sourceSets.named("${prefix}SimulatorArm64Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}SimulatorArm64Test", any()) }
        verify(exactly = 2) { iosSimulator.dependsOn(ios) }
    }

    @Test
    fun `Given legacyIos is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(relaxed = true)
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val iosArm32: KotlinSourceSet = mockk(relaxed = true)
        val ios: KotlinSourceSet = mockk()
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.named(any()) } returns TestNamedProvider(ios)
        invokeGradleAction(iosArm32, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }

        // When
        extension.iosLegacy(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.ios(prefix, configuration) }
        verify(exactly = 1) { extension.iosArm32("${prefix}Arm32", configuration) }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm32Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm32Test", any()) }
        verify(exactly = 2) { iosArm32.dependsOn(ios) }
    }

    @Test
    fun `Given iosxLegacy is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(relaxed = true)
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = {}
        val iosMix: KotlinSourceSet = mockk(relaxed = true)
        val ios: KotlinSourceSet = mockk()
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.named(any()) } returns TestNamedProvider(ios)
        invokeGradleAction(iosMix, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }

        // When
        extension.iosxLegacy(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.ios(prefix, configuration) }
        verify(exactly = 1) { extension.iosArm32("${prefix}Arm32", configuration) }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm32Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm32Test", any()) }
        verify(exactly = 1) { extension.iosSimulatorArm64("${prefix}SimulatorArm64", configuration) }
        verify(exactly = 1) { sourceSets.named("${prefix}SimulatorArm64Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}SimulatorArm64Test", any()) }
        verify(exactly = 4) { iosMix.dependsOn(ios) }
    }
}
