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
import io.mockk.unmockkStatic
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

class SourceHooksNativeSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setup() {
        mockkStatic(
            KotlinMultiplatformExtension::androidNative,
            KotlinMultiplatformExtension::apple,
            KotlinMultiplatformExtension::appleWithLegacy,
            KotlinMultiplatformExtension::linux,
            KotlinMultiplatformExtension::mingw,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(
            KotlinMultiplatformExtension::androidNative,
            KotlinMultiplatformExtension::apple,
            KotlinMultiplatformExtension::appleWithLegacy,
            KotlinMultiplatformExtension::linux,
            KotlinMultiplatformExtension::mingw,
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
        val native: KotlinSourceSet = mockk(relaxed = true)
        val nativeSubset: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk(relaxed = true)
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { extension.wasm32(any(), any<KotlinNativeTarget.() -> Unit>()) } returns mockk()
        every { sourceSets.getByName(any()) } returns nativeSubset
        every { sourceSets.maybeCreate(any()) } returns native
        invokeGradleAction(nativeSubset, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName("commonMain") } returns common
        every { sourceSets.getByName("commonTest") } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }
        every { extension.androidNative(any(), any()) } just Runs
        every { extension.apple(any(), any()) } just Runs
        every { extension.linux(any(), any()) } just Runs
        every { extension.mingw(any(), any()) } just Runs

        // When
        extension.native(name, configuration)

        // Then
        verify(exactly = 1) { sourceSets.maybeCreate("${name}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${name}Test") }

        verify(exactly = 1) { extension.androidNative(configuration = configuration) }
        verify(exactly = 1) { extension.apple(configuration = configuration) }
        verify(exactly = 1) { extension.linux(configuration = configuration) }
        // Note here is something off with mockk
        verify(exactly = 1) { extension.wasm32(any<Action<KotlinNativeTarget>>()) } // (configure = configuration) }
        verify(exactly = 1) { extension.mingw(configuration = configuration) }

        verify(exactly = 1) { sourceSets.getByName("androidNativeMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("androidNativeTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("appleMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("appleTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("linuxMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("linuxTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("wasm32Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("wasm32Test", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("mingwMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("mingwTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 10) { nativeSubset.dependsOn(native) }

        verify(exactly = 1) { sourceSets.getByName("commonMain") }
        verify(exactly = 1) { sourceSets.getByName("commonTest") }
        verify(exactly = 2) { native.dependsOn(common) }
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
        val native: KotlinSourceSet = mockk(relaxed = true)
        val nativeSubset: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk(relaxed = true)
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.getByName(any()) } returns nativeSubset
        every { sourceSets.maybeCreate(any()) } returns native
        invokeGradleAction(nativeSubset, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName("commonMain") } returns common
        every { sourceSets.getByName("commonTest") } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }
        every { extension.androidNative(any(), any()) } just Runs
        every { extension.appleWithLegacy(any(), any()) } just Runs
        every { extension.linux(any(), any()) } just Runs
        every { extension.mingw(any(), any()) } just Runs

        // When
        extension.nativeWithLegacy(name, configuration)

        // Then
        verify(exactly = 1) { sourceSets.maybeCreate("${name}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${name}Test") }

        verify(exactly = 1) { extension.androidNative(configuration = configuration) }
        verify(exactly = 1) { extension.appleWithLegacy(configuration = configuration) }
        verify(exactly = 1) { extension.linux(configuration = configuration) }
        // Note here is something off with mockk
        verify(exactly = 1) { extension.wasm32(any<Action<KotlinNativeTarget>>()) }
        verify(exactly = 1) { extension.mingw(configuration = configuration) }

        verify(exactly = 1) { sourceSets.getByName("androidNativeMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("androidNativeTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("appleMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("appleTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("linuxMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("linuxTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("wasm32Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("wasm32Test", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { sourceSets.getByName("mingwMain", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("mingwTest", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 10) { nativeSubset.dependsOn(native) }

        verify(exactly = 1) { sourceSets.getByName("commonMain") }
        verify(exactly = 1) { sourceSets.getByName("commonTest") }
        verify(exactly = 2) { native.dependsOn(common) }
    }
}
