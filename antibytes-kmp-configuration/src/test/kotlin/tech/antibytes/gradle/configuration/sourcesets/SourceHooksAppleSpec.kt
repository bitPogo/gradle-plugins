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
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.TestNamedProvider
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
        val apple: KotlinSourceSet = mockk()
        val appleSubset: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.named(any()) } returns TestNamedProvider(appleSubset)
        every { sourceSets.create(any()) } returns apple
        invokeGradleAction(appleSubset, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }
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
        verify(exactly = 1) { sourceSets.create("${name}Main") }
        verify(exactly = 1) { sourceSets.create("${name}Test") }

        verify(exactly = 1) { extension.iosx(configuration = configuration) }
        verify(exactly = 1) { extension.macos(configuration = configuration) }
        verify(exactly = 1) { extension.tvosx(configuration = configuration) }
        verify(exactly = 1) { extension.watchosx(configuration = configuration) }

        verify(exactly = 1) { sourceSets.named("iosMain", any()) }
        verify(exactly = 1) { sourceSets.named("iosTest", any()) }

        verify(exactly = 1) { sourceSets.named("macosMain", any()) }
        verify(exactly = 1) { sourceSets.named("macosTest", any()) }

        verify(exactly = 1) { sourceSets.named("tvosMain", any()) }
        verify(exactly = 1) { sourceSets.named("tvosTest", any()) }

        verify(exactly = 1) { sourceSets.named("watchosMain", any()) }
        verify(exactly = 1) { sourceSets.named("watchosTest", any()) }

        verify(exactly = 8) { appleSubset.dependsOn(apple) }
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
        val apple: KotlinSourceSet = mockk()
        val appleSubset: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.named(any()) } returns TestNamedProvider(appleSubset)
        every { sourceSets.create(any()) } returns apple
        invokeGradleAction(appleSubset, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }
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
        verify(exactly = 1) { sourceSets.create("${name}Main") }
        verify(exactly = 1) { sourceSets.create("${name}Test") }

        verify(exactly = 1) { extension.iosxWithLegacy(configuration = configuration) }
        verify(exactly = 1) { extension.macos(configuration = configuration) }
        verify(exactly = 1) { extension.tvosx(configuration = configuration) }
        verify(exactly = 1) { extension.watchosxWithLegacy(configuration = configuration) }

        verify(exactly = 1) { sourceSets.named("iosMain", any()) }
        verify(exactly = 1) { sourceSets.named("iosTest", any()) }

        verify(exactly = 1) { sourceSets.named("macosMain", any()) }
        verify(exactly = 1) { sourceSets.named("macosTest", any()) }

        verify(exactly = 1) { sourceSets.named("tvosMain", any()) }
        verify(exactly = 1) { sourceSets.named("tvosTest", any()) }

        verify(exactly = 1) { sourceSets.named("watchosMain", any()) }
        verify(exactly = 1) { sourceSets.named("watchosTest", any()) }

        verify(exactly = 8) { appleSubset.dependsOn(apple) }
    }
}
