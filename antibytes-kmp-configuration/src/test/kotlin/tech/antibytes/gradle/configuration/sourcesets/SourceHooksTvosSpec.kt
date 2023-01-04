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
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.TestNamedProvider
import tech.antibytes.gradle.test.invokeGradleAction

class SourceHooksTvosSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given tvosx is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val tvosSimulator: KotlinSourceSet = mockk(relaxed = true)
        val tvos: KotlinSourceSet = mockk()
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.named(any()) } returns TestNamedProvider(tvos)
        invokeGradleAction(tvosSimulator, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.tvosx(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.tvos(prefix, configuration) }
        verify(exactly = 1) { extension.tvosSimulatorArm64("${prefix}SimulatorArm64", configuration) }
        verify(exactly = 1) { sourceSets.named("${prefix}SimulatorArm64Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}SimulatorArm64Test", any()) }
        verify(exactly = 2) { tvosSimulator.dependsOn(tvos) }
    }
}
