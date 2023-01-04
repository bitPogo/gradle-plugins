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
import tech.antibytes.gradle.test.invokeGradleAction

class SourceHooksLinuxSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given linuxArm is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val linuxArmSets: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val linuxArm: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.create(any()) } returns linuxArm
        invokeGradleAction(linuxArmSets, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.linuxArm(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.linuxArm64("${prefix}64", configuration) }
        verify(exactly = 1) { extension.linuxArm32Hfp("${prefix}32Hfp", configuration) }
        verify(exactly = 1) { sourceSets.create("${prefix}Main") }
        verify(exactly = 1) { sourceSets.create("${prefix}Test") }
        verify(exactly = 1) { sourceSets.named("${prefix}64Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}64Test", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}32HfpMain", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}32HfpTest", any()) }
        verify(exactly = 4) { linuxArmSets.dependsOn(linuxArm) }
    }

    @Test
    fun `Given linuxMips is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val linuxMipsSets: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val linuxMips: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.create(any()) } returns linuxMips
        invokeGradleAction(linuxMipsSets, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.linuxMips(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.linuxMips32("${prefix}32", configuration) }
        verify(exactly = 1) { extension.linuxMipsel32("${prefix}el32", configuration) }
        verify(exactly = 1) { sourceSets.create("${prefix}Main") }
        verify(exactly = 1) { sourceSets.create("${prefix}Test") }
        verify(exactly = 1) { sourceSets.named("${prefix}32Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}32Test", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}el32Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}el32Test", any()) }
        verify(exactly = 4) { linuxMipsSets.dependsOn(linuxMips) }
    }

    @Test
    fun `Given linux is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val linuxSets: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val linux: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.create(any()) } returns linux
        invokeGradleAction(linuxSets, mockk()) { sourceSet ->
            sourceSets.named(any(), sourceSet)
        }
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.linux(prefix, configuration)

        // Then
        verify(exactly = 1) { sourceSets.create("${prefix}Main") }
        verify(exactly = 1) { sourceSets.create("${prefix}Test") }
        verify(exactly = 1) { extension.linuxX64("${prefix}X64", configuration) }
        verify(exactly = 1) { sourceSets.named("${prefix}X64Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}X64Test", any()) }

        verify(exactly = 1) { extension.linuxMips32("${prefix}Mips32", configuration) }
        verify(exactly = 1) { extension.linuxMipsel32("${prefix}Mipsel32", configuration) }
        verify(exactly = 1) { sourceSets.create("${prefix}MipsMain") }
        verify(exactly = 1) { sourceSets.create("${prefix}MipsTest") }
        verify(exactly = 1) { sourceSets.named("${prefix}Mips32Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Mips32Test", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Mipsel32Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Mipsel32Test", any()) }

        verify(exactly = 1) { extension.linuxArm64("${prefix}Arm64", configuration) }
        verify(exactly = 1) { extension.linuxArm32Hfp("${prefix}Arm32Hfp", configuration) }
        verify(exactly = 1) { sourceSets.create("${prefix}ArmMain") }
        verify(exactly = 1) { sourceSets.create("${prefix}ArmTest") }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm64Main", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm64Test", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm32HfpMain", any()) }
        verify(exactly = 1) { sourceSets.named("${prefix}Arm32HfpTest", any()) }

        verify(exactly = 14) { linuxSets.dependsOn(linux) }
    }
}
