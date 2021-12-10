/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration.value

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.gradle.api.Project
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.coverage.api.JacocoReporterSettings
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract
import tech.antibytes.gradle.coverage.source.SourceHelper
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AndroidConfigurationProviderSpec {
    @Before
    fun setup() {
        mockkObject(SourceHelper)
    }

    @After
    fun tearDown() {
        unmockkObject(SourceHelper)
    }

    @Test
    fun `It fulfils DefaultPlatformConfigurationProvider`() {
        val provider: Any = AndroidConfigurationProvider

        assertTrue(provider is ConfigurationContract.DefaultPlatformConfigurationProvider)
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration, which contains default ReportSettings`() {
        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(
            mockk(),
            PlatformContext.ANDROID_LIBRARY
        )

        // Then
        assertEquals(
            actual = config.reportSettings,
            expected = JacocoReporterSettings(),
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration, which contains the dependencies for TestTask`() {
        // Given
        val context = PlatformContext.ANDROID_LIBRARY

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.testDependencies,
            expected = setOf("testDebugUnitTest"),
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, which indicates a Android Library, it returns a CoverageConfiguration, which has no InstrumentedTestTask`() {
        // Given
        val context = PlatformContext.ANDROID_LIBRARY

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.instrumentedTestDependencies,
            expected = setOf(),
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, which indicates a Android Library for KMP, it returns a CoverageConfiguration, which has no InstrumentedTestTask`() {
        // Given
        val context = PlatformContext.ANDROID_LIBRARY_KMP

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.instrumentedTestDependencies,
            expected = setOf(),
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration, which has a InstrumentedTestTask`() {
        // Given
        val context = PlatformContext.ANDROID_APPLICATION

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.instrumentedTestDependencies,
            expected = setOf("connectedDebugAndroidTest"),
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration, which contains the default class pattern`() {
        // Given
        val context = PlatformContext.ANDROID_LIBRARY

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.classPattern,
            expected = setOf(
                "build/intermediates/javac/debug/**/*.class",
                "build/tmp/kotlin-classes/debug/**/*.class",
            ),
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration, which contains the default class filter`() {
        // Given
        val context = PlatformContext.ANDROID_LIBRARY

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.classFilter,
            expected = setOf(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*",
                "android/**/*.*",
                "**/*\$Lambda$*.*",
                "**/*\$inlined$*.*"
            ),
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration, which contains the sources`() {
        // Given
        val context = PlatformContext.ANDROID_LIBRARY

        val project: Project = mockk()
        val sources: Set<File> = mockk()

        every { SourceHelper.resolveSources(project, context) } returns sources

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(project, context)

        // Then
        assertSame(
            actual = config.sources,
            expected = sources
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration, which contains no additional classes`() {
        // Given
        val context = PlatformContext.ANDROID_LIBRARY

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertNull(config.additionalClasses)
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration, which contains no additional sources`() {
        // Given
        val context = PlatformContext.ANDROID_LIBRARY

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.additionalSources,
            expected = emptySet(),
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration, which contains no violation rules`() {
        // Given
        val context = PlatformContext.ANDROID_LIBRARY

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.verificationRules,
            expected = emptySet(),
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration which contains the default variant`() {
        val context = PlatformContext.ANDROID_LIBRARY

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.variant,
            expected = "debug"
        )
    }

    @Test
    fun `Given createDefaultCoverageConfiguration is called with a Project and PlatformContext, it returns a CoverageConfiguration which contains the default flavour`() {
        val context = PlatformContext.ANDROID_LIBRARY

        every { SourceHelper.resolveSources(any(), any()) } returns mockk()

        // When
        val config = AndroidConfigurationProvider.createDefaultCoverageConfiguration(mockk(), context)

        // Then
        assertEquals(
            actual = config.flavour,
            expected = ""
        )
    }
}
