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
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.maybeCreate(any()) } returns linuxArm
        invokeGradleAction(linuxArmSets, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName(any()) } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.linuxArm(prefix, configuration)

        // Then
        verify(exactly = 1) { extension.linuxArm64("${prefix}64", configuration) }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Test") }
        verify(exactly = 1) { sourceSets.getByName("${prefix}64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}64Test", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 2) { linuxArmSets.dependsOn(linuxArm) }

        verify(exactly = 1) { sourceSets.getByName("commonMain") }
        verify(exactly = 1) { sourceSets.getByName("commonTest") }
        verify(exactly = 2) { linuxArm.dependsOn(common) }
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
        val common: KotlinSourceSet = mockk(relaxed = true)

        every { extension.sourceSets } returns sourceSets
        every { sourceSets.maybeCreate(any()) } returns linux
        invokeGradleAction(linuxSets, mockk()) { sourceSet ->
            sourceSets.getByName(any(), sourceSet)
        }
        every { sourceSets.getByName(any()) } returns common
        every { (extension as ExtensionAware).extensions } returns extensions
        invokeGradleAction(sourceSets) { sources ->
            extensions.configure("sourceSets", sources)
        }

        // When
        extension.linux(prefix, configuration)

        // Then
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Main") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}Test") }
        verify(exactly = 1) { extension.linuxX64("${prefix}X64", configuration) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}X64Test", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 1) { extension.linuxArm64("${prefix}Arm64", configuration) }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}ArmMain") }
        verify(exactly = 1) { sourceSets.maybeCreate("${prefix}ArmTest") }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm64Main", any<Action<KotlinSourceSet>>()) }
        verify(exactly = 1) { sourceSets.getByName("${prefix}Arm64Test", any<Action<KotlinSourceSet>>()) }

        verify(exactly = 8) { linuxSets.dependsOn(linux) }

        verify(exactly = 2) { sourceSets.getByName("commonMain") }
        verify(exactly = 2) { sourceSets.getByName("commonTest") }
        verify(exactly = 4) { linux.dependsOn(common) }
    }
}
