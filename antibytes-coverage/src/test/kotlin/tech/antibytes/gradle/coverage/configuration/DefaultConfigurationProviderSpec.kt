/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.gradle.api.Project
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageContract
import tech.antibytes.gradle.coverage.configuration.value.AndroidConfigurationProvider
import tech.antibytes.gradle.coverage.configuration.value.JvmConfigurationProvider
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DefaultConfigurationProviderSpec {
    @Before
    fun setup() {
        mockkObject(PlatformContextResolver)
        mockkObject(JvmConfigurationProvider)
        mockkObject(AndroidConfigurationProvider)
    }

    @After
    fun tearDown() {
        unmockkObject(PlatformContextResolver)
        unmockkObject(JvmConfigurationProvider)
        unmockkObject(AndroidConfigurationProvider)
    }

    @Test
    fun `It fulfils DefaultConfigurationProvider`() {
        val provider: Any = DefaultConfigurationProvider

        assertTrue(provider is CoverageContract.DefaultConfigurationProvider)
    }

    @Test
    fun `Given configure is called createDefaultCoverageConfiguration with a Project, it returns an empty Map, if nothing maps`() {
        // Given
        val project: Project = mockk()

        every { PlatformContextResolver.getType(project) } returns emptySet()

        // When
        val result = DefaultConfigurationProvider.createDefaultCoverageConfiguration(project)

        // Then
        assertEquals(
            expected = result,
            actual = emptyMap()
        )
    }

    @Test
    fun `Given configure is called createDefaultCoverageConfiguration with a Project, it returns the default JVM configuration if the project is JVM`() {
        // Given
        val project: Project = mockk()

        val config: CoverageApiContract.JacocoCoverageConfiguration = mockk()

        every { PlatformContextResolver.getType(project) } returns setOf(ConfigurationContract.PlatformContext.JVM)
        every {
            JvmConfigurationProvider.createDefaultCoverageConfiguration(
                project,
                ConfigurationContract.PlatformContext.JVM
            )
        } returns config

        // When
        val result = DefaultConfigurationProvider.createDefaultCoverageConfiguration(project)

        // Then
        assertEquals(
            expected = 1,
            actual = result.size
        )

        assertSame(
            expected = config,
            actual = result["jvm"]
        )
    }

    @Test
    fun `Given configure is called createDefaultCoverageConfiguration with a Project, it returns the default Android configuration if the project is Android`() {
        // Given
        val project: Project = mockk()

        val config: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()

        every { PlatformContextResolver.getType(project) } returns setOf(ConfigurationContract.PlatformContext.ANDROID_APPLICATION)
        every {
            AndroidConfigurationProvider.createDefaultCoverageConfiguration(
                project,
                ConfigurationContract.PlatformContext.ANDROID_APPLICATION
            )
        } returns config

        // When
        val result = DefaultConfigurationProvider.createDefaultCoverageConfiguration(project)

        // Then
        assertEquals(
            expected = 1,
            actual = result.size
        )

        assertSame(
            expected = config,
            actual = result["android"]
        )
    }

    @Test
    fun `Given configure is called createDefaultCoverageConfiguration with a Project, it returns the default Android and JVM configuration if both are in the context`() {
        // Given
        val project: Project = mockk()

        val configAndroid: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()
        val configJvm: CoverageApiContract.JacocoCoverageConfiguration = mockk()

        every { PlatformContextResolver.getType(project) } returns setOf(
            ConfigurationContract.PlatformContext.ANDROID_APPLICATION,
            ConfigurationContract.PlatformContext.JVM
        )
        every {
            AndroidConfigurationProvider.createDefaultCoverageConfiguration(
                project,
                ConfigurationContract.PlatformContext.ANDROID_APPLICATION
            )
        } returns configAndroid
        every {
            JvmConfigurationProvider.createDefaultCoverageConfiguration(
                project,
                ConfigurationContract.PlatformContext.JVM
            )
        } returns configJvm

        // When
        val result = DefaultConfigurationProvider.createDefaultCoverageConfiguration(project)

        // Then
        assertEquals(
            expected = 2,
            actual = result.size
        )

        assertSame(
            expected = configAndroid,
            actual = result["android"]
        )
        assertSame(
            expected = configJvm,
            actual = result["jvm"]
        )
    }
}
