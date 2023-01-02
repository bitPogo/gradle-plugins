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
import tech.antibytes.gradle.test.invokeGradleAction

class SourceHooksWindowsSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given windows is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(relaxed = true)
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val windowsSets: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val windows: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.create(any()) } returns windows
        invokeGradleAction(windowsSets, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }

        // When
        extension.windows(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.mingwX64("${prefix}X64", configuration) }
        verify(exactly = 1) { extension.mingwX86("${prefix}X86", configuration) }
        verify(exactly = 1) { sourceSets.create("${prefix}Main") }
        verify(exactly = 1) { sourceSets.create("${prefix}Test") }
        verify(exactly = 1) { sourceSets.named("${prefix}X64Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}X64Test", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}X86Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}X86Test", any()) }
        verify(exactly = 4) { windowsSets.dependsOn(windows) }
    }
}
