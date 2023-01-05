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

class SourceHooksAndroidSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given androidNativeArm is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val androidNativeArmSets: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val androidNativeArm: KotlinSourceSet = mockk(relaxed = true)
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.maybeCreate(any()) } returns androidNativeArm
        invokeGradleAction(androidNativeArmSets, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName(any()) } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.androidNativeArm(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.androidNativeArm32("${prefix}32", configuration) }
        verify(exactly = 1) { extension.androidNativeArm64("${prefix}64", configuration) }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Test") }
        verify(exactly = 1) { sourceSets.getByName("${prefix}32Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}32Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}64Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 4) { androidNativeArmSets.dependsOn(androidNativeArm) }

        verify(exactly = 1) { sourceSets.getByName("commonMain") }
        verify(exactly = 1) { sourceSets.getByName("commonTest") }
        verify(exactly = 2) { androidNativeArm.dependsOn(common) }
    }

    @Test
    fun `Given androidNativeX is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val androidNativeXSets: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val androidNativeX: KotlinSourceSet = mockk(relaxed = true)
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.maybeCreate(any()) } returns androidNativeX
        invokeGradleAction(androidNativeXSets, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName(any()) } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.androidNativeX(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.androidNativeX64("${prefix}64", configuration) }
        verify(exactly = 1) { extension.androidNativeX86("${prefix}86", configuration) }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Test") }
        verify(exactly = 1) { sourceSets.getByName("${prefix}64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}64Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}86Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}86Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 4) { androidNativeXSets.dependsOn(androidNativeX) }

        verify(exactly = 1) { sourceSets.getByName("commonMain") }
        verify(exactly = 1) { sourceSets.getByName("commonTest") }
        verify(exactly = 2) { androidNativeX.dependsOn(common) }
    }

    @Test
    fun `Given androidNative is called it delegates the given parameter to the KMP configuration`() {
        // Given
        val extension: KotlinMultiplatformExtension = mockk(
            relaxed = true,
            moreInterfaces = arrayOf(ExtensionAware::class),
        )
        val extensions: ExtensionContainer = mockk()
        val prefix: String = fixture()
        val configuration: KotlinNativeTarget.() -> Unit = { }
        val androidNativeSets: KotlinSourceSet = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<KotlinSourceSet> = mockk()
        val androidNative: KotlinSourceSet = mockk(relaxed = true)
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.maybeCreate(any()) } returns androidNative
        invokeGradleAction(androidNativeSets, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName(any()) } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.androidNative(prefix, configuration)

        // Then
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Test") }

        verify(exactly = 1) { extension.androidNativeArm32("${prefix}Arm32", configuration) }
        verify(exactly = 1) { extension.androidNativeArm64("${prefix}Arm64", configuration) }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}ArmMain") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}ArmTest") }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm32Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm32Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm64Test", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { extension.androidNativeX64("${prefix}X64", configuration) }
        verify(exactly = 1) { extension.androidNativeX86("${prefix}X86", configuration) }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}XMain") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}XTest") }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X64Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X86Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X86Test", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 12) { androidNativeSets.dependsOn(androidNative) }

        verify(exactly = 3) { sourceSets.getByName("commonMain") }
        verify(exactly = 3) { sourceSets.getByName("commonTest") }
        verify(exactly = 6) { androidNative.dependsOn(common) }
    }
}
