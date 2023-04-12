/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.configuration.value.AndroidConfigurationProvider
import tech.antibytes.gradle.coverage.configuration.value.JvmConfigurationProvider
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import tech.antibytes.gradle.util.PlatformContextResolver

class DefaultConfigurationProviderSpec {
    @BeforeEach
    fun setup() {
        mockkObject(
            PlatformContextResolver,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            PlatformContextResolver,
        )
    }

    @Test
    fun `It fulfils DefaultConfigurationProvider`() {
        val provider: Any = DefaultConfigurationProvider()

        assertTrue(provider is DefaultConfigurationProvider)
    }

    @Test
    fun `Given configure is called createDefaultCoverageConfiguration with a Project, it returns an empty Map, if nothing maps`() {
        // Given
        val project: Project = mockk()

        every { PlatformContextResolver.getType(project) } returns emptySet()

        // When
        val result = DefaultConfigurationProvider().createDefaultCoverageConfiguration(project)

        // Then
        assertEquals(
            expected = result,
            actual = emptyMap(),
        )
    }

    @Test
    fun `Given configure is called createDefaultCoverageConfiguration with a Project, it returns the default JVM configuration if the project is JVM`() {
        // Given
        val project: Project = mockk()
        val config: CoverageApiContract.JacocoCoverageConfiguration = mockk()
        val jvmConfigurationProvider: JvmConfigurationProvider = mockk {
            every {
                createDefaultCoverageConfiguration(
                    project,
                    PlatformContext.JVM,
                )
            } returns config
        }

        every { PlatformContextResolver.getType(project) } returns setOf(PlatformContext.JVM)

        // When
        val result = DefaultConfigurationProvider(
            jvmConfigurationProvider = jvmConfigurationProvider,
        ).createDefaultCoverageConfiguration(project)

        // Then
        assertEquals(
            expected = 1,
            actual = result.size,
        )

        assertSame(
            expected = config,
            actual = result["jvm"],
        )
    }

    @Test
    fun `Given configure is called createDefaultCoverageConfiguration with a Project, it returns the default Android configuration if the project is Android`() {
        // Given
        val project: Project = mockk()

        val config: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()
        val androidConfigurationProvider: AndroidConfigurationProvider = mockk {
            every {
                createDefaultCoverageConfiguration(
                    project,
                    PlatformContext.ANDROID_APPLICATION,
                )
            } returns config
        }
        every { PlatformContextResolver.getType(project) } returns setOf(PlatformContext.ANDROID_APPLICATION)

        // When
        val result = DefaultConfigurationProvider(
            androidConfigurationProvider = androidConfigurationProvider,
        ).createDefaultCoverageConfiguration(project)

        // Then
        assertEquals(
            expected = 1,
            actual = result.size,
        )

        assertSame(
            expected = config,
            actual = result["android"],
        )
    }

    @Test
    fun `Given configure is called createDefaultCoverageConfiguration with a Project, it returns the default Android and JVM configuration if both are in the context`() {
        // Given
        val project: Project = mockk()

        val configAndroid: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()
        val configJvm: CoverageApiContract.JacocoCoverageConfiguration = mockk()

        val jvmConfigurationProvider: JvmConfigurationProvider = mockk {
            every {
                createDefaultCoverageConfiguration(
                    project,
                    PlatformContext.JVM,
                )
            } returns configJvm
        }
        val androidConfigurationProvider: AndroidConfigurationProvider = mockk {
            every {
                createDefaultCoverageConfiguration(
                    project,
                    PlatformContext.ANDROID_LIBRARY,
                )
            } returns configAndroid
        }
        every { PlatformContextResolver.getType(project) } returns setOf(
            PlatformContext.ANDROID_LIBRARY,
            PlatformContext.JVM,
        )

        // When
        val result = DefaultConfigurationProvider(
            androidConfigurationProvider = androidConfigurationProvider,
            jvmConfigurationProvider = jvmConfigurationProvider,
        ).createDefaultCoverageConfiguration(project)

        // Then
        assertEquals(
            expected = 2,
            actual = result.size,
        )

        assertSame(
            expected = configAndroid,
            actual = result["android"],
        )
        assertSame(
            expected = configJvm,
            actual = result["jvm"],
        )
    }
}
