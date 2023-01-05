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
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.invokeGradleAction

class SourceHooksIosSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given iosx is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val iosSimulator: KotlinSourceSet = mockk(relaxed = true)
        val ios: KotlinSourceSet = mockk()
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.getByName(any()) } returns ios
        invokeGradleAction(iosSimulator, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.iosx(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.ios(prefix, configuration) }
        verify(exactly = 1) { extension.iosSimulatorArm64("${prefix}SimulatorArm64", configuration) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}SimulatorArm64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}SimulatorArm64Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 2) { iosSimulator.dependsOn(ios) }
    }

    @Test
    fun `Given legacyIos is called it delegates the given parameter to the KMP configuration`() {
        // Given

        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val iosArm32: KotlinSourceSet = mockk(relaxed = true)
        val ios: KotlinSourceSet = mockk()
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.getByName(any()) } returns ios
        invokeGradleAction(iosArm32, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.iosWithLegacy(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.ios(prefix, configuration) }
        verify(exactly = 1) { extension.iosArm32("${prefix}Arm32", configuration) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm32Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm32Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 2) { iosArm32.dependsOn(ios) }
    }

    @Test
    fun `Given iosxLegacy is called it delegates the given parameter to the KMP configuration`() {
        // Given

        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = {}
        val iosMix: KotlinSourceSet = mockk(relaxed = true)
        val ios: KotlinSourceSet = mockk()
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.getByName(any()) } returns ios
        invokeGradleAction(iosMix, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.iosxWithLegacy(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.ios(prefix, configuration) }
        verify(exactly = 1) { extension.iosArm32("${prefix}Arm32", configuration) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm32Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm32Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { extension.iosSimulatorArm64("${prefix}SimulatorArm64", configuration) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}SimulatorArm64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}SimulatorArm64Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 4) { iosMix.dependsOn(ios) }
    }
}
