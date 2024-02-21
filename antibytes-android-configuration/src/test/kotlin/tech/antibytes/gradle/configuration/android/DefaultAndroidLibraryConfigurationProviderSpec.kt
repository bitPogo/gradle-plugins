/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.android

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.ConfigurationContract
import tech.antibytes.gradle.configuration.api.AndroidLibraryConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource
import tech.antibytes.gradle.util.GradleUtilApiContract
import tech.antibytes.gradle.util.PlatformContextResolver

class DefaultAndroidLibraryConfigurationProviderSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setup() {
        mockkObject(PlatformContextResolver)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(PlatformContextResolver)
    }

    @Test
    fun `It fulfils DefaultConfigurationProvider`() {
        val provider: Any = DefaultAndroidLibraryConfigurationProvider

        assertTrue(provider is ConfigurationContract.DefaultConfigurationProvider<*>)
    }

    @Test
    fun `Given createDefaultConfiguration is called with a Project it returns a AndroidLibraryConfiguration with default settings, if it is an AndroidLibrary`() {
        // Given
        val project: Project = mockk()
        val projectName: String = fixture()

        every { PlatformContextResolver.getType(any()) } returns setOf(GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY)
        every { project.name } returns projectName
        // When
        val result = DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(project)

        // Then
        assertEquals(
            actual = result,
            expected = AndroidLibraryConfiguration(
                compileSdkVersion = 34,
                minSdkVersion = 23,
                prefix = "antibytes_${projectName.replace("-", "_")}_",
                publishVariants = emptySet(),
                compatibilityTargets = Compatibility(
                    target = JavaVersion.VERSION_1_8,
                    source = JavaVersion.VERSION_1_8,
                ),
                fallbacks = mapOf("debug" to setOf("release")),
                mainSource = MainSource(
                    manifest = "src/main/AndroidManifest.xml",
                    sourceDirectories = setOf("src/main/kotlin"),
                    resourceDirectories = setOf(
                        "src/main/res",
                        "src/main/resources",
                    ),
                ),
                unitTestSource = TestSource(
                    sourceDirectories = setOf("src/test/kotlin"),
                    resourceDirectories = setOf(
                        "src/test/res",
                        "src/test/resources",
                    ),
                ),
                androidTest = TestSource(
                    sourceDirectories = setOf("src/androidTest/kotlin"),
                    resourceDirectories = setOf(
                        "src/androidTest/res",
                        "src/androidTest/resources",
                    ),
                ),
                testRunner = TestRunner(
                    runner = "androidx.test.runner.AndroidJUnitRunner",
                    arguments = mapOf("clearPackageData" to "true"),
                ),
            ),
        )
    }

    @Test
    fun `Given createDefaultConfiguration is called with a Project it returns a AndroidLibraryConfiguration with default settings, if it is an AndroidLibrary in KMP Context`() {
        // Given
        val project: Project = mockk()
        val projectName: String = fixture()

        every { PlatformContextResolver.getType(any()) } returns setOf(GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP)
        every { project.name } returns projectName

        // When
        val result = DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(project)

        // Then
        assertEquals(
            actual = result,
            expected = AndroidLibraryConfiguration(
                compileSdkVersion = 34,
                minSdkVersion = 23,
                publishVariants = setOf("release"),
                prefix = "antibytes_${projectName.replace("-", "_")}_",
                compatibilityTargets = Compatibility(
                    target = JavaVersion.VERSION_1_8,
                    source = JavaVersion.VERSION_1_8,
                ),
                fallbacks = mapOf("debug" to setOf("release")),
                mainSource = MainSource(
                    manifest = "src/androidMain/AndroidManifest.xml",
                    sourceDirectories = setOf("src/androidMain/kotlin"),
                    resourceDirectories = setOf(
                        "src/androidMain/res",
                        "src/androidMain/resources",
                    ),
                ),
                unitTestSource = TestSource(
                    sourceDirectories = setOf("src/androidUnitTest/kotlin"),
                    resourceDirectories = setOf(
                        "src/androidUnitTest/res",
                        "src/androidUnitTest/resources",
                    ),
                ),
                androidTest = TestSource(
                    sourceDirectories = setOf("src/androidInstrumentedTest/kotlin"),
                    resourceDirectories = setOf(
                        "src/androidInstrumentedTest/res",
                        "src/androidInstrumentedTest/resources",
                    ),
                ),
                testRunner = TestRunner(
                    runner = "androidx.test.runner.AndroidJUnitRunner",
                    arguments = mapOf("clearPackageData" to "true"),
                ),
            ),
        )
    }
}
