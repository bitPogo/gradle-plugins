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

class SourceHooksMacosSpecx {
    private val fixture = kotlinFixture()

    @Test
    fun `Given macos is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(relaxed = true)
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val macosSets: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val macos: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.create(any()) } returns macos
        invokeGradleAction(macosSets, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }

        // When
        extension.macos(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.macosX64("${prefix}X64", configuration) }
        verify(exactly = 1) { extension.macosArm64("${prefix}Arm64", configuration) }
        verify(exactly = 1) { sourceSets.create("${prefix}Main") }
        verify(exactly = 1) { sourceSets.create("${prefix}Test") }
        verify(exactly = 1) { sourceSets.named("${prefix}X64Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}X64Test", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm64Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm64Test", any()) }
        verify(exactly = 4) { macosSets.dependsOn(macos) }
    }
}
