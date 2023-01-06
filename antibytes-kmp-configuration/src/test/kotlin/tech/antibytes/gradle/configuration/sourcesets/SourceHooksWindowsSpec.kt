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

class SourceHooksWindowsSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given windows is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val windowsSets: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val windows: KotlinSourceSet = mockk(relaxed = true)
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.maybeCreate(any()) } returns windows
        invokeGradleAction(windowsSets, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName(any()) } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.mingw(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.mingwX64("${prefix}X64", configuration) }
        verify(exactly = 1) { extension.mingwX86("${prefix}X86", configuration) }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Test") }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X64Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X86Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X86Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 4) { windowsSets.dependsOn(windows) }

        verify(exactly = 1) { sourceSets.getByName("commonMain") }
        verify(exactly = 1) { sourceSets.getByName("commonTest") }
        verify(exactly = 2) { windows.dependsOn(common) }
    }
}
