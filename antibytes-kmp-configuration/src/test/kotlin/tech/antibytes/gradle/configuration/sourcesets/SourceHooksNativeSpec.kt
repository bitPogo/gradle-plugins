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

class SourceHooksNativeSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setup() {
        mockkStatic(
            KotlinMultiplatformExtension::androidNative,
            KotlinMultiplatformExtension::apple,
            KotlinMultiplatformExtension::appleWithLegacy,
            KotlinMultiplatformExtension::linux,
            KotlinMultiplatformExtension::windows,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            KotlinMultiplatformExtension::androidNative,
            KotlinMultiplatformExtension::apple,
            KotlinMultiplatformExtension::appleWithLegacy,
            KotlinMultiplatformExtension::linux,
            KotlinMultiplatformExtension::windows,
        )
    }

    @Test
    fun `Given native is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val name: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val native: KotlinSourceSet = mockk()
        val nativeSubset: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { extension.wasm32(any(), any<KotlinNativeTarget.() -> Unit>()) } returns mockk()
        every { sourceSets.named(any()) } returns TestNamedProvider(nativeSubset)
        every { sourceSets.create(any()) } returns native
        invokeGradleAction(nativeSubset, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }
        every { extension.androidNative(any(), any()) } just Runs
        every { extension.apple(any(), any()) } just Runs
        every { extension.linux(any(), any()) } just Runs
        every { extension.windows(any(), any()) } just Runs

        // When
        extension.native(name, configuration)

        // Then
        verify(exactly = 1) { sourceSets.create("${name}Main") }
        verify(exactly = 1) { sourceSets.create("${name}Test") }

        verify(exactly = 1) { extension.androidNative(configuration = configuration) }
        verify(exactly = 1) { extension.apple(configuration = configuration) }
        verify(exactly = 1) { extension.linux(configuration = configuration) }
        verify(exactly = 1) { extension.wasm32(configure = configuration) }
        verify(exactly = 1) { extension.windows(configuration = configuration) }

        verify(exactly = 1) { sourceSets.named("androidNativeMain", any()) }
        verify(exactly = 1) { sourceSets.named("androidNativeTest", any()) }

        verify(exactly = 1) { sourceSets.named("appleMain", any()) }
        verify(exactly = 1) { sourceSets.named("appleTest", any()) }

        verify(exactly = 1) { sourceSets.named("linuxMain", any()) }
        verify(exactly = 1) { sourceSets.named("linuxTest", any()) }

        verify(exactly = 1) { sourceSets.named("wasm32Main", any()) }
        verify(exactly = 1) { sourceSets.named("wasm32Test", any()) }

        verify(exactly = 1) { sourceSets.named("windowsMain", any()) }
        verify(exactly = 1) { sourceSets.named("windowsTest", any()) }

        verify(exactly = 10) { nativeSubset.dependsOn(native) }
    }

    @Test
    fun `Given nativeLegacy is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val name: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val native: KotlinSourceSet = mockk()
        val nativeSubset: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.named(any()) } returns TestNamedProvider(nativeSubset)
        every { sourceSets.create(any()) } returns native
        invokeGradleAction(nativeSubset, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }
        every { extension.androidNative(any(), any()) } just Runs
        every { extension.appleWithLegacy(any(), any()) } just Runs
        every { extension.linux(any(), any()) } just Runs
        every { extension.windows(any(), any()) } just Runs

        // When
        extension.nativeWithLegacy(name, configuration)

        // Then
        verify(exactly = 1) { sourceSets.create("${name}Main") }
        verify(exactly = 1) { sourceSets.create("${name}Test") }

        verify(exactly = 1) { extension.androidNative(configuration = configuration) }
        verify(exactly = 1) { extension.appleWithLegacy(configuration = configuration) }
        verify(exactly = 1) { extension.linux(configuration = configuration) }
        verify(exactly = 1) { extension.wasm32(configure = configuration) }
        verify(exactly = 1) { extension.windows(configuration = configuration) }

        verify(exactly = 1) { sourceSets.named("androidNativeMain", any()) }
        verify(exactly = 1) { sourceSets.named("androidNativeTest", any()) }

        verify(exactly = 1) { sourceSets.named("appleMain", any()) }
        verify(exactly = 1) { sourceSets.named("appleTest", any()) }

        verify(exactly = 1) { sourceSets.named("linuxMain", any()) }
        verify(exactly = 1) { sourceSets.named("linuxTest", any()) }

        verify(exactly = 1) { sourceSets.named("wasm32Main", any()) }
        verify(exactly = 1) { sourceSets.named("wasm32Test", any()) }

        verify(exactly = 1) { sourceSets.named("windowsMain", any()) }
        verify(exactly = 1) { sourceSets.named("windowsTest", any()) }

        verify(exactly = 10) { nativeSubset.dependsOn(native) }
    }
}
