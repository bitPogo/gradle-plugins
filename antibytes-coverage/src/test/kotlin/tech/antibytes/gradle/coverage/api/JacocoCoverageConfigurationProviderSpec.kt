/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage.api

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration.Provider
import tech.antibytes.gradle.util.GradleUtilApiContract

class JacocoCoverageConfigurationProviderSpec {
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
    fun `It fulfils JacocoCoverageConfigurationProvider`() {
        val provider: Any = JvmJacocoConfiguration

        assertTrue(provider is CoverageApiContract.JacocoCoverageConfigurationProvider)
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project it returns the default settings for JVM only`() {
        // Given
        val project: Project = mockk()
        val config: JvmJacocoConfiguration = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmOnlyConfiguration(project)

        // Then
        assertEquals(
            actual = result,
            expected = config,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.JVM,
            )
        }
    }

    @Test
    fun `Given createJvmKmpConfiguration is called with a Project it returns the default settings for JVM in KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmKmpConfiguration(project)

        // Then
        assertEquals(
            actual = result,
            expected = config,
        )

        verify(exactly = 1) {
            Provider.createDefaultCoverageConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.JVM_KMP,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and ReportSettings it returns settings withReportSettings for JVM only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val reporterSettings: CoverageApiContract.JacocoReporterSettings = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and ReportSettings it returns settings withReportSettings for JVM on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val reporterSettings: CoverageApiContract.JacocoReporterSettings = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM_KMP,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and TestDependencies it returns settings withTestDependencies for JVM only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val testDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and TestDependencies it returns settings withTestDependencies for JVM on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val testDependencies: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM_KMP,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and ClassPattern it returns settings with the ClassPattern for JVM only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classPattern: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and ClassPattern it returns settings with the ClassPattern for JVM on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classPattern: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM_KMP,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and ClassFilter it returns settings withClassFilter for JVM only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classFilter: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and ClassFilter it returns settings withClassFilter for JVM on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val classFilter: Set<String> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM_KMP,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and Sources it returns settings withSources for JVM only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val sources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and Sources it returns settings withSources for JVM on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val sources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM_KMP,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and AdditionalSources it returns settings withAdditionalSources for JVM only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalSources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and AdditionalSources it returns settings withAdditionalSources for JVM on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalSources: Set<File> = fixture()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM_KMP,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and AdditionalClasses it returns settings withAdditionalClasses for JVM only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalClasses: ConfigurableFileTree = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and AdditionalClasses it returns settings withAdditionalClasses for JVM on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val additionalClasses: ConfigurableFileTree = mockk()

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM_KMP,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and VerificationRules it returns settings withVerificationRules for JVM only`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val verificationRules: Set<JacocoVerificationRule> = setOf(mockk())

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmOnlyConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM,
            )
        }
    }

    @Test
    fun `Given createJvmOnlyConfiguration is called with a Project and VerificationRules it returns settings withVerificationRules for JVM on KMP`() {
        // Given
        val project: Project = mockk()
        val config = defaultConfig.copy()
        val verificationRules: Set<JacocoVerificationRule> = setOf(mockk())

        every { Provider.createDefaultCoverageConfiguration(any(), any()) } returns config

        // When
        val result = JvmJacocoConfiguration.createJvmKmpConfiguration(
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
                GradleUtilApiContract.PlatformContext.JVM_KMP,
            )
        }
    }

    private companion object {
        val defaultConfig = JvmJacocoConfiguration(
            JacocoReporterSettings(),
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet(),
            emptySet(),
            mockk(),
            emptySet(),
        )
    }
}
