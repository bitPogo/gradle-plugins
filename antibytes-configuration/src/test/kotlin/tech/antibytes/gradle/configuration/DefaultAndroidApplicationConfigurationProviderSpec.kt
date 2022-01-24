/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.configuration.api.AndroidApplicationConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource
import tech.antibytes.gradle.util.GradleUtilApiContract
import tech.antibytes.gradle.util.PlatformContextResolver
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultAndroidApplicationConfigurationProviderSpec {
    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        mockkObject(PlatformContextResolver)
    }

    @After
    fun tearDown() {
        unmockkObject(PlatformContextResolver)
    }

    @Test
    fun `It fulfils DefaultAndroidApplicationConfigurationProvider`() {
        val provider: Any = DefaultAndroidApplicationConfigurationProvider

        assertTrue(provider is ConfigurationContract.DefaultAndroidApplicationConfigurationProvider)
    }

    @Test
    fun `Given createDefaultConfiguration is called with a Project it returns a AndroidApplicationConfiguration with default settings, if it is an AndroidLibrary`() {
        // Given
        val project: Project = mockk()
        val projectName: String = fixture()

        every { PlatformContextResolver.getType(any()) } returns setOf(GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY)
        every { project.name } returns projectName
        // When
        val result = DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(project)

        // Then
        assertEquals(
            actual = result,
            expected = AndroidApplicationConfiguration(
                compileSdkVersion = 31,
                minSdkVersion = 23,
                targetSdkVersion = 31,
                compatibilityTargets = Compatibility(
                    target = JavaVersion.VERSION_1_8,
                    source = JavaVersion.VERSION_1_8
                ),
                fallbacks = mapOf("debug" to setOf("release")),
                mainSource = MainSource(
                    manifest = "src/main/AndroidManifest.xml",
                    sourceDirectories = setOf("src/main/kotlin"),
                    resourceDirectories = setOf(
                        "src/main/res",
                        "src/main/resources"
                    )
                ),
                unitTestSource = TestSource(
                    sourceDirectories = setOf("src/test/kotlin"),
                    resourceDirectories = setOf(
                        "src/test/res",
                        "src/test/resources"
                    )
                ),
                androidTest = TestSource(
                    sourceDirectories = setOf("src/androidTest/kotlin"),
                    resourceDirectories = setOf(
                        "src/androidTest/res",
                        "src/androidTest/resources"
                    )
                ),
                testRunner = TestRunner(
                    runner = "androidx.test.runner.AndroidJUnitRunner",
                    arguments = mapOf("clearPackageData" to "true")
                )
            )
        )
    }
}