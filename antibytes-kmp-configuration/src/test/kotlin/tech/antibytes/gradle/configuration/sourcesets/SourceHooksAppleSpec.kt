/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.verify
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.invokeGradleAction

class SourceHooksAppleSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setup() {
        mockkStatic(
            KotlinMultiplatformExtension::iosx,
            KotlinMultiplatformExtension::iosxWithLegacy,
            KotlinMultiplatformExtension::macos,
            KotlinMultiplatformExtension::tvosx,
            KotlinMultiplatformExtension::watchosx,
            KotlinMultiplatformExtension::watchosxWithLegacy,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            KotlinMultiplatformExtension::iosx,
            KotlinMultiplatformExtension::iosxWithLegacy,
            KotlinMultiplatformExtension::macos,
            KotlinMultiplatformExtension::tvosx,
            KotlinMultiplatformExtension::watchosx,
            KotlinMultiplatformExtension::watchosxWithLegacy,
        )
    }

    @Test
    fun `Given apple is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val name: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val apple: KotlinSourceSet = mockk(relaxed = true)
        val appleSubset: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk(relaxed = true)
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.getByName(any()) } returns appleSubset
        every { sourceSets.maybeCreate(any()) } returns apple
        invokeGradleAction(appleSubset, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName("commonMain") } returns common
        every { sourceSets.getByName("commonTest") } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }
        every { extension.iosx(any(), any()) } just Runs
        every { extension.macos(any(), any()) } just Runs
        every { extension.tvosx(any(), any()) } just Runs
        every { extension.watchosx(any(), any()) } just Runs

        // When
        extension.apple(name, configuration)

        // Then
        verify(exactly = 1) { sourceSets.maybeCreate("${name}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${name}Test") }

        verify(exactly = 1) { extension.iosx(configuration = configuration) }
        verify(exactly = 1) { extension.macos(configuration = configuration) }
        verify(exactly = 1) { extension.tvosx(configuration = configuration) }
        verify(exactly = 1) { extension.watchosx(configuration = configuration) }

        verify(exactly = 1) { sourceSets.getByName("iosMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("iosTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("macosMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("macosTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("tvosMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("tvosTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("watchosMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("watchosTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 8) { appleSubset.dependsOn(apple) }

        verify(exactly = 1) { sourceSets.getByName("commonMain") }
        verify(exactly = 1) { sourceSets.getByName("commonTest") }
        verify(exactly = 2) { apple.dependsOn(common) }
    }

    @Test
    fun `Given appleLegacy is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val name: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val apple: KotlinSourceSet = mockk(relaxed = true)
        val appleSubset: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk(relaxed = true)
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.getByName(any()) } returns appleSubset
        every { sourceSets.maybeCreate(any()) } returns apple
        invokeGradleAction(appleSubset, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName("commonMain") } returns common
        every { sourceSets.getByName("commonTest") } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }
        every { extension.iosxWithLegacy(any(), any()) } just Runs
        every { extension.macos(any(), any()) } just Runs
        every { extension.tvosx(any(), any()) } just Runs
        every { extension.watchosxWithLegacy(any(), any()) } just Runs

        // When
        extension.appleWithLegacy(name, configuration)

        // Then
        verify(exactly = 1) { sourceSets.maybeCreate("${name}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${name}Test") }

        verify(exactly = 1) { extension.iosxWithLegacy(configuration = configuration) }
        verify(exactly = 1) { extension.macos(configuration = configuration) }
        verify(exactly = 1) { extension.tvosx(configuration = configuration) }
        verify(exactly = 1) { extension.watchosxWithLegacy(configuration = configuration) }

        verify(exactly = 1) { sourceSets.getByName("iosMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("iosTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("macosMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("macosTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("tvosMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("tvosTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("watchosMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("watchosTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 8) { appleSubset.dependsOn(apple) }

        verify(exactly = 1) { sourceSets.getByName("commonMain") }
        verify(exactly = 1) { sourceSets.getByName("commonTest") }
        verify(exactly = 2) { apple.dependsOn(common) }
    }
}
