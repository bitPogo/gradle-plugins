/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
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

class AndroidJacocoCoverageConfigurationProviderLibrarySpec {
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
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project it returns the default settings for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config: AndroidJacocoConfiguration = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config
        every { config.variant = any() } just Runs
        every { config.flavour = any() } just Runs

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(project)

        // Then
        assertEquals(
            actual = result,
            expected = config,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryKmpConfiguration is called with a Project it returns the default settings for ANDROID_LIBRARY in KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(project)

        // Then
        assertEquals(
            actual = result,
            expected = config,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and a Variant it ignores empty Variants for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val variant = ""

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and a Variant it ignores empty Variants for ANDROID_LIBRARY KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val variant = ""

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and a Variant it returns settings withVariant for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val variant = "Variant"

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and a Variant it returns settings withVariant for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val variant = "Variant"

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and a Variant it ignores empty Flavours for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val flavour = ""

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and a Variant it ignores empty Flavours for ANDROID_LIBRARY KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val flavour = ""

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and a Flavour it returns settings withFlavour for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val flavour = "Flavour"

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and a Flavour it returns settings withFlavour for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val flavour = "Flavour"

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and ReportSettings it returns settings withReportSettings for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val reporterSettings: CoverageApiContract.JacocoReporterSettings = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and ReportSettings it returns settings withReportSettings for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val reporterSettings: CoverageApiContract.JacocoReporterSettings = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project it returns the settings with TestDependencies for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val testDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project it returns the settings with TestDependencies for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val testDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and InstrumentedTestDependencies it returns the default settings for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val instrumentedTestDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and InstrumentedTestDependencies it returns the default settings for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val instrumentedTestDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and ClassPattern it returns settings with the ClassPattern for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classPattern: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and ClassPattern it returns settings with the ClassPattern for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classPattern: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and ClassFilter it returns settings withClassFilter for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classFilter: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and ClassFilter it returns settings withClassFilter for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classFilter: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and Sources it returns settings withSources for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val sources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and Sources it returns settings withSources for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val sources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    @JvmName("AdditionalSources")
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and AdditionalSources it returns the settings with the AdditionalSources for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalSources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and AdditionalSources it returns the settings with the AdditionalSources for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalSources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and AdditionalClasses it returns settings withAdditionalClasses for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalClasses: ConfigurableFileTree = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and AdditionalClasses it returns settings withAdditionalClasses for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalClasses: ConfigurableFileTree = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and VerificationRules it returns settings withVerificationRules for ANDROID_LIBRARY only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val verificationRules: Set<JacocoVerificationRule> = setOf(mockk())

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
            )
        }
    }

    @Test
    fun `Given createAndroidLibraryOnlyConfiguration is called with a Project and VerificationRules it returns settings withVerificationRules for ANDROID_LIBRARY on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val verificationRules: Set<JacocoVerificationRule> = setOf(mockk())

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
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
