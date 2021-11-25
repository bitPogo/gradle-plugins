/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.configuration.api.AndroidLibraryConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource
import tech.antibytes.gradle.util.GradleUtilApiContract
import tech.antibytes.gradle.util.PlatformContextResolver
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultAndroidLibraryConfigurationProviderSpec {
    @Before
    fun setup() {
        mockkObject(PlatformContextResolver)
    }

    @After
    fun tearDown() {
        unmockkObject(PlatformContextResolver)
    }

    @Test
    fun `It fulfils DefaultAndroidLibraryConfigurationProvider`() {
        val provider: Any = DefaultAndroidLibraryConfigurationProvider

        assertTrue(provider is ConfigurationContract.DefaultAndroidLibraryConfigurationProvider)
    }

    @Test
    fun `Given createDefaultConfiguration is called with a Project it returns null if the project contains no AndroidLibrary`() {
        // Given
        val project: Project = mockk()

        every { PlatformContextResolver.getType(any()) } returns setOf(GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION)
        // When
        val result = DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(project)

        // Then
        assertNull(result)
    }

    @Test
    fun `Given createDefaultConfiguration is called with a Project it returns a AndroidLibraryConfiguration with default settings, if it is an AndroidLibrary`() {
        // Given
        val project: Project = mockk()

        every { PlatformContextResolver.getType(any()) } returns setOf(GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY)
        // When
        val result = DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(project)

        // Then
        assertTrue(result is ConfigurationApiContract.AndroidLibraryConfiguration)
        assertEquals(
            actual = result,
            expected = AndroidLibraryConfiguration(
                minSdkVersion = 23,
                targetSdkVersion = 30,
                compileSdkVersion = 30,
                projectInfix = "",
                publishVariants = emptySet(),
                compatibilityTargets = Compatibility(
                    target = JavaVersion.VERSION_1_8,
                    source = JavaVersion.VERSION_1_8
                ),
                fallbacks = mapOf("debug" to setOf("release")),
                mainSource = MainSource(
                    manifest = "src/androidMain/AndroidManifest.xml",
                    sourceDirectories = setOf("src/androidMain/kotlin"),
                    resourceDirectories = setOf("src/androidMain/res")
                ),
                unitTestSource = TestSource(
                    sourceDirectories = setOf("src/androidTest/kotlin"),
                    resourceDirectories = setOf("src/androidTest/res")
                ),
                androidTest = null,
                testRunner = TestRunner(
                    runner = "androidx.test.runner.AndroidJUnitRunner",
                    arguments = mapOf("clearPackageData" to "true")
                )
            )
        )
    }

    @Test
    fun `Given createDefaultConfiguration is called with a Project it returns a AndroidLibraryConfiguration with default settings, if it is an AndroidLibrary in KMP Context`() {
        // Given
        val project: Project = mockk()

        every { PlatformContextResolver.getType(any()) } returns setOf(GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP)
        // When
        val result = DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(project)

        // Then
        assertTrue(result is ConfigurationApiContract.AndroidLibraryConfiguration)
        assertEquals(
            actual = result,
            expected = AndroidLibraryConfiguration(
                minSdkVersion = 23,
                targetSdkVersion = 30,
                compileSdkVersion = 30,
                projectInfix = "",
                publishVariants = setOf("release", "debug"),
                compatibilityTargets = Compatibility(
                    target = JavaVersion.VERSION_1_8,
                    source = JavaVersion.VERSION_1_8
                ),
                fallbacks = mapOf("debug" to setOf("release")),
                mainSource = MainSource(
                    manifest = "src/androidMain/AndroidManifest.xml",
                    sourceDirectories = setOf("src/androidMain/kotlin"),
                    resourceDirectories = setOf("src/androidMain/res")
                ),
                unitTestSource = TestSource(
                    sourceDirectories = setOf("src/androidTest/kotlin"),
                    resourceDirectories = setOf("src/androidTest/res")
                ),
                androidTest = null,
                testRunner = TestRunner(
                    runner = "androidx.test.runner.AndroidJUnitRunner",
                    arguments = mapOf("clearPackageData" to "true")
                )
            )
        )
    }
}
