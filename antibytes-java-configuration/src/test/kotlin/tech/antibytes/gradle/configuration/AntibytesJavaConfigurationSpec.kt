/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.config.MainConfig

class AntibytesJavaConfigurationSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntibytesJavaConfiguration()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called it does nothing if no Java is active`() {
        // Given
        val project: Project = mockk {
            every { extensions.findByType(JavaPluginExtension::class.java) } returns null
        }

        // When
        AntibytesJavaConfiguration().apply(project)

        // Then
        verify(exactly = 1) { project.extensions.findByType(JavaPluginExtension::class.java) }
        confirmVerified(project)
    }

    @Test
    fun `Given apply is called it sets up the compatibility and the toolchain`() {
        // Given
        val version = JavaVersion.toVersion(MainConfig.javaVersion)
        val jvmToolchain: JavaToolchainSpec = mockk(relaxed = true)
        val javaPluginExtension: JavaPluginExtension = mockk(relaxed = true) {
            every { toolchain } returns jvmToolchain
        }
        val project: Project = mockk {
            every { extensions.findByType(JavaPluginExtension::class.java) } returns javaPluginExtension
        }

        // When
        AntibytesJavaConfiguration().apply(project)

        // Then
        verify(exactly = 1) { jvmToolchain.languageVersion.set(JavaLanguageVersion.of(MainConfig.javaVersion)) }
        verify(exactly = 1) { javaPluginExtension.sourceCompatibility = version }
        verify(exactly = 1) { javaPluginExtension.targetCompatibility = version }
    }
}
