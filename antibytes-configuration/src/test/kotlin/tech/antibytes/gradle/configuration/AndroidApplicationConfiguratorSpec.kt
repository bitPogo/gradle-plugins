/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import com.android.build.api.dsl.AndroidSourceDirectorySet
import com.android.build.api.dsl.AndroidSourceFile
import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CompileOptions
import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.api.AndroidApplicationConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource
import tech.antibytes.gradle.test.invokeGradleAction
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AndroidApplicationConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils AndroidApplicationConfigurator`() {
        val configurator: Any = AndroidApplicationConfigurator

        assertTrue(configurator is ConfigurationContract.AndroidApplicationConfigurator)
    }

    @Test
    fun `Given configure is called with a Project, it sets the CompileSDK Version`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidApplicationConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            compatibilityTargets = Compatibility(
                target = JavaVersion.VERSION_1_8,
                source = JavaVersion.VERSION_1_8
            ),
            fallbacks = mapOf(fixture<String>() to fixture()),
            mainSource = MainSource(
                manifest = fixture(),
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            unitTestSource = TestSource(
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            androidTest = TestSource(
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            testRunner = TestRunner(
                runner = fixture(),
                arguments = fixture()
            )
        )

        val extensions: ExtensionContainer = mockk()
        val libraryExtension: ApplicationExtension = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns fixture()

        invokeGradleAction(
            { probe -> extensions.configure(ApplicationExtension::class.java, probe) },
            libraryExtension
        )

        // When
        AndroidApplicationConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { libraryExtension.compileSdk = configuration.compileSdkVersion }
    }

    @Test
    fun `Given configure is called with a Project and a AndroidApplicationConfiguration, it setups up the DefaultBuildTypeConfiguration`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidApplicationConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            compatibilityTargets = Compatibility(
                target = JavaVersion.VERSION_1_8,
                source = JavaVersion.VERSION_1_8
            ),
            fallbacks = mapOf(fixture<String>() to fixture()),
            mainSource = MainSource(
                manifest = fixture(),
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            unitTestSource = TestSource(
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            androidTest = TestSource(
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            testRunner = TestRunner(
                runner = fixture(),
                arguments = fixture()
            )
        )

        val extensions: ExtensionContainer = mockk()
        val libraryExtension: ApplicationExtension = mockk(relaxed = true)
        val defaultConfiguration: ApplicationDefaultConfig = mockk(relaxed = true)
        val runnerArguments: MutableMap<String, String> = mutableMapOf()

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false

        invokeGradleAction(
            { probe -> extensions.configure(ApplicationExtension::class.java, probe) },
            libraryExtension
        )

        every { libraryExtension.defaultConfig(captureLambda()) } answers {
            lambda<(ApplicationDefaultConfig) -> Unit>().invoke(defaultConfiguration)
        }

        every { defaultConfiguration.testInstrumentationRunnerArguments } returns runnerArguments

        // When
        AndroidApplicationConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { defaultConfiguration.minSdk = configuration.minSdkVersion }
        verify(exactly = 1) { defaultConfiguration.targetSdk = configuration.targetSdkVersion }
        verify(exactly = 1) { defaultConfiguration.testInstrumentationRunner = configuration.testRunner.runner }
        assertEquals(
            actual = runnerArguments,
            expected = configuration.testRunner.arguments
        )
    }

    @Test
    fun `Given configure is called with a Project and a AndroidApplicationConfiguration, it setups up the CompileOptions`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidApplicationConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            compatibilityTargets = Compatibility(
                target = JavaVersion.VERSION_1_8,
                source = JavaVersion.VERSION_1_8
            ),
            fallbacks = mapOf(fixture<String>() to fixture()),
            mainSource = MainSource(
                manifest = fixture(),
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            unitTestSource = TestSource(
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            androidTest = TestSource(
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            testRunner = TestRunner(
                runner = fixture(),
                arguments = fixture()
            )
        )

        val extensions: ExtensionContainer = mockk()
        val libraryExtension: ApplicationExtension = mockk(relaxed = true)
        val compileOptions: CompileOptions = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false

        invokeGradleAction(
            { probe -> extensions.configure(ApplicationExtension::class.java, probe) },
            libraryExtension
        )

        every { libraryExtension.compileOptions(captureLambda()) } answers {
            lambda<(CompileOptions) -> Unit>().invoke(compileOptions)
        }

        // When
        AndroidApplicationConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { compileOptions.targetCompatibility = configuration.compatibilityTargets.target }
        verify(exactly = 1) { compileOptions.sourceCompatibility = configuration.compatibilityTargets.source }
    }

    @Test
    fun `Given configure is called with a Project and a AndroidApplicationConfiguration, it setups up the SourceSets`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidApplicationConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            compatibilityTargets = Compatibility(
                target = JavaVersion.VERSION_1_8,
                source = JavaVersion.VERSION_1_8
            ),
            fallbacks = mapOf(fixture<String>() to fixture()),
            mainSource = MainSource(
                manifest = fixture(),
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            unitTestSource = TestSource(
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            androidTest = TestSource(
                sourceDirectories = fixture(),
                resourceDirectories = fixture()
            ),
            testRunner = TestRunner(
                runner = fixture(),
                arguments = fixture()
            )
        )

        val extensions: ExtensionContainer = mockk()
        val libraryExtension: ApplicationExtension = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<out AndroidSourceSet> = mockk(relaxed = true)
        val mainSource: AndroidSourceSet = mockk(relaxed = true)
        val manifest: AndroidSourceFile = mockk()
        val mainSourceDirectories: AndroidSourceDirectorySet = mockk()
        val mainResourceDirectories: AndroidSourceDirectorySet = mockk()

        val testSource: AndroidSourceSet = mockk(relaxed = true)
        val testSourceDirectories: AndroidSourceDirectorySet = mockk()
        val testResourceDirectories: AndroidSourceDirectorySet = mockk()

        val androidTestSource: AndroidSourceSet = mockk(relaxed = true)
        val androidTestSourceDirectories: AndroidSourceDirectorySet = mockk()
        val androidTestResourceDirectories: AndroidSourceDirectorySet = mockk()

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false

        invokeGradleAction(
            { probe -> extensions.configure(ApplicationExtension::class.java, probe) },
            libraryExtension
        )

        every { libraryExtension.sourceSets(captureLambda()) } answers {
            lambda<(NamedDomainObjectContainer<out AndroidSourceSet>) -> Unit>().invoke(sourceSets)
        }

        every { sourceSets.getByName("main") } returns mainSource
        every { mainSource.manifest } returns manifest
        every { manifest.srcFile(any()) } returns mockk()
        every { mainSource.java } returns mainSourceDirectories
        every { mainSourceDirectories.setSrcDirs(any()) } returns mockk()
        every { mainSource.res } returns mainResourceDirectories
        every { mainResourceDirectories.setSrcDirs(any()) } returns mockk()

        every { sourceSets.getByName("test") } returns testSource
        every { testSource.java } returns testSourceDirectories
        every { testSourceDirectories.setSrcDirs(any()) } returns mockk()
        every { testSource.res } returns testResourceDirectories
        every { testResourceDirectories.setSrcDirs(any()) } returns mockk()

        every { sourceSets.getByName("androidTest") } returns androidTestSource
        every { androidTestSource.java } returns androidTestSourceDirectories
        every { androidTestSourceDirectories.setSrcDirs(any()) } returns mockk()
        every { androidTestSource.res } returns androidTestResourceDirectories
        every { androidTestResourceDirectories.setSrcDirs(any()) } returns mockk()

        // When
        AndroidApplicationConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { sourceSets.getByName("androidTest") }
        verify(exactly = 1) { androidTestSourceDirectories.setSrcDirs(configuration.androidTest.sourceDirectories) }
        verify(exactly = 1) { androidTestResourceDirectories.setSrcDirs(configuration.androidTest.resourceDirectories) }
    }
}
