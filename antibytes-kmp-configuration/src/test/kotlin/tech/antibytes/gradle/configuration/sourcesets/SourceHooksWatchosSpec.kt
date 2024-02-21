/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
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

class SourceHooksWatchosSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given watchosx is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val watchosXSet: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val watchosX: KotlinSourceSet = mockk(relaxed = true)
        val common: KotlinSourceSet = mockk(relaxed = true)
        val device: KotlinSourceSet = mockk(relaxed = true)
        val device32: KotlinNativeTarget = mockk(relaxed = true) {
            every { compilations.getByName(any()).defaultSourceSet } returns device
        }
        val device64: KotlinNativeTarget = mockk(relaxed = true) {
            every { compilations.getByName(any()).defaultSourceSet } returns device
        }

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.maybeCreate(any()) } returns watchosX
        invokeGradleAction(watchosXSet, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { extension.watchosArm32(any<String>(), any<KotlinNativeTarget.() -> Unit>()) } returns device32
        every { extension.watchosArm64(any<String>(), any<KotlinNativeTarget.() -> Unit>()) } returns device64
        every { sourceSets.getByName(any()) } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.watchosx(prefix, configuration)

        // Then
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Test") }

        verify(exactly = 1) { extension.watchosArm32("${prefix}Arm32", configuration) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm32Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm32Test", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { extension.watchosArm64("${prefix}Arm64", configuration) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm64Test", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { extension.watchosX64("${prefix}X64", configuration) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X64Test", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { extension.watchosSimulatorArm64("${prefix}SimulatorArm64", configuration) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}SimulatorArm64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}SimulatorArm64Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 8) { watchosXSet.dependsOn(watchosX) }

        verify(exactly = 1) { sourceSets.getByName("commonMain") }
        verify(exactly = 1) { sourceSets.getByName("commonTest") }
        verify(exactly = 2) { watchosX.dependsOn(common) }
    }
}
