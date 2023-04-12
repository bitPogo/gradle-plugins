/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.api

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.api.AndroidJacocoConfiguration.Provider
import tech.antibytes.gradle.util.GradleUtilApiContract

class AndroidJacocoCoverageConfigurationProviderAppSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setUp() {
        mockkObject(Provider)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Provider)
    }

    @Test
    fun `It fulfils AndroidJacocoCoverageConfigurationProvider`() {
        val provider: Any = AndroidJacocoConfiguration

        assertTrue(provider is CoverageApiContract.AndroidJacocoCoverageConfigurationProvider)
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project it returns the default settings for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config: AndroidJacocoConfiguration = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config
        every { config.variant = any() } just Runs
        every { config.flavour = any() } just Runs

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(project)

        // Then
        assertEquals(
            actual = result,
            expected = config,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppKmpConfiguration is called with a Project it returns the default settings for ANDROID_APPLICATION in KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(project)

        // Then
        assertEquals(
            actual = result,
            expected = config,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and a Variant it ignores empty Variants for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val variant = ""

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            variant = variant,
        )

        // Then
        assertNotEquals(
            actual = result.variant,
            illegal = variant,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and a Variant it ignores empty Variants for ANDROID_APPLICATION KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val variant = ""

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            variant = variant,
        )

        // Then
        assertNotEquals(
            actual = result.variant,
            illegal = variant,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and a Variant it returns settings for Variant for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val variant = "Variant"

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            variant = variant,
        )

        // Then
        assertSame(
            actual = result.variant,
            expected = variant,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and a Variant it returns settings for Variant for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val variant = "Variant"

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            variant = variant,
        )

        // Then
        assertEquals(
            actual = result.variant,
            expected = variant,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and a Flavour it ignores empty Flavours for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val flavour = ""

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            flavour = flavour,
        )

        // Then
        assertNotEquals(
            actual = result.flavour,
            illegal = flavour,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and a Flavour it ignores empty Flavours for ANDROID_APPLICATION KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val flavour = ""

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            flavour = flavour,
        )

        // Then
        assertNotEquals(
            actual = result.flavour,
            illegal = flavour,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and a Flavour it returns settings for Flavour for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val flavour = "Flavour"

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            flavour = flavour,
        )

        // Then
        assertSame(
            actual = result.flavour,
            expected = flavour,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and a Flavour it returns settings for Flavour for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val flavour = "Flavour"

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            flavour = flavour,
        )

        // Then
        assertEquals(
            actual = result.flavour,
            expected = flavour,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and ReportSettings it returns settings for ReportSettings for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val reporterSettings: CoverageApiContract.JacocoReporterSettings = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            reportSettings = reporterSettings,
        )

        // Then
        assertSame(
            actual = result.reportSettings,
            expected = reporterSettings,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and ReportSettings it returns settings for ReportSettings for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val reporterSettings: CoverageApiContract.JacocoReporterSettings = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            reportSettings = reporterSettings,
        )

        // Then
        assertEquals(
            actual = result.reportSettings,
            expected = reporterSettings,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and TestDependencies it returns the default settings ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val testDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            testDependencies = testDependencies,
        )

        // Then
        assertEquals(
            actual = result.test,
            expected = testDependencies,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and TestDependencies it returns settings for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val testDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            testDependencies = testDependencies,
        )

        // Then
        assertEquals(
            actual = result.test,
            expected = testDependencies,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and InstrumentedTestDependencies it returns the default settings for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val instrumentedTestDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            instrumentedTestDependencies = instrumentedTestDependencies,
        )

        // Then
        assertEquals(
            actual = result.instrumentedTest,
            expected = instrumentedTestDependencies,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and InstrumentedTestDependencies it returns the default settings for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val instrumentedTestDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            instrumentedTestDependencies = instrumentedTestDependencies,
        )

        // Then
        assertEquals(
            actual = result.instrumentedTest,
            expected = instrumentedTestDependencies,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and ClassPattern it returns settings for ClassPattern for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classPattern: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            classPattern = classPattern,
        )

        // Then
        assertEquals(
            actual = result.classPattern,
            expected = classPattern,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and ClassPattern it returns settings for ClassPattern for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classPattern: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            classPattern = classPattern,
        )

        // Then
        assertEquals(
            actual = result.classPattern,
            expected = classPattern,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and ClassFilter it returns settings for ClassFilter for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classFilter: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            classFilter = classFilter,
        )

        // Then
        assertEquals(
            actual = result.classFilter,
            expected = classFilter,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and ClassFilter it returns settings for ClassFilter for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classFilter: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            classFilter = classFilter,
        )

        // Then
        assertEquals(
            actual = result.classFilter,
            expected = classFilter,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and Sources it returns settings for Sources for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val sources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            sources = sources,
        )

        // Then
        assertEquals(
            actual = result.sources,
            expected = sources,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and Sources it returns settings for Sources for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val sources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            sources = sources,
        )

        // Then
        assertEquals(
            actual = result.sources,
            expected = sources,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    @JvmName("AdditionalSources")
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and AdditionalSources it returns settings for AdditionalSources for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalSources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            additionalSources = additionalSources,
        )

        // Then
        assertEquals(
            actual = result.additionalSources,
            expected = additionalSources,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and AdditionalSources it returns the settings with AdditionalSources for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalSources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            additionalSources = additionalSources,
        )

        // Then
        assertEquals(
            actual = result.additionalSources,
            expected = additionalSources,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and AdditionalClasses it returns settings for AdditionalClasses for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalClasses: ConfigurableFileTree = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            additionalClasses = additionalClasses,
        )

        // Then
        assertEquals(
            actual = result.additionalClasses,
            expected = additionalClasses,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and AdditionalClasses it returns settings for AdditionalClasses for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalClasses: ConfigurableFileTree = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            additionalClasses = additionalClasses,
        )

        // Then
        assertEquals(
            actual = result.additionalClasses,
            expected = additionalClasses,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and VerificationRules it returns settings for VerificationRules for ANDROID_APPLICATION only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val verificationRules: Set<JacocoVerificationRule> = setOf(mockk())

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppOnlyConfiguration(
            project,
            verificationRules = verificationRules,
        )

        // Then
        assertEquals(
            actual = result.verificationRules,
            expected = verificationRules,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
            )
        }
    }

    @Test
    fun `Given createAndroidAppOnlyConfiguration is called with a Project and VerificationRules it returns settings for VerificationRules for ANDROID_APPLICATION on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val verificationRules: Set<JacocoVerificationRule> = setOf(mockk())

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidAppKmpConfiguration(
            project,
            verificationRules = verificationRules,
        )

        // Then
        assertEquals(
            actual = result.verificationRules,
            expected = verificationRules,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
            )
        }
    }

    private companion object {
        val defaultConfig = AndroidJacocoConfiguration(
            JacocoReporterSettings(),
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet(),
            mockk(),
            emptySet(),
            emptySet(),
            "xxx",
            "zzz",
        )
    }
}
