/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import com.android.build.api.dsl.AndroidSourceDirectorySet
import com.android.build.api.dsl.AndroidSourceFile
import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.api.dsl.CompileOptions
import com.android.build.api.dsl.LibraryDefaultConfig
import com.android.build.api.dsl.LibraryExtension
import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.junit.Test
import tech.antibytes.gradle.configuration.api.AndroidLibraryConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource
import tech.antibytes.gradle.test.invokeGradleAction
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AndroidLibraryConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils AndroidLibraryConfigurator`() {
        val configurator: Any = AndroidLibraryConfigurator

        assertTrue(configurator is ConfigurationContract.AndroidLibraryConfigurator)
    }

    @Test
    fun `Given configure is called with a Project, it sets the CompileSDK Version and Prefix`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidLibraryConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            prefix = fixture(),
            publishVariants = emptySet(),
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
            androidTest = null,
            testRunner = TestRunner(
                runner = fixture(),
                arguments = fixture()
            )
        )

        val extensions: ExtensionContainer = mockk()
        val libraryExtension: LibraryExtension = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns fixture()

        invokeGradleAction(
            { probe -> extensions.configure(LibraryExtension::class.java, probe) },
            libraryExtension
        )

        // When
        AndroidLibraryConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { libraryExtension.compileSdk = configuration.compileSdkVersion }
        verify(exactly = 1) { libraryExtension.resourcePrefix = configuration.prefix }
    }

    @Test
    fun `Given configure is called with a Project and a AndroidLibraryConfiguration, it setups up the DefaultBuildTypeConfiguration`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidLibraryConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            prefix = fixture(),
            publishVariants = emptySet(),
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
            androidTest = null,
            testRunner = TestRunner(
                runner = fixture(),
                arguments = fixture()
            )
        )

        val extensions: ExtensionContainer = mockk()
        val libraryExtension: LibraryExtension = mockk(relaxed = true)
        val defaultConfiguration: LibraryDefaultConfig = mockk(relaxed = true)
        val runnerArguments: MutableMap<String, String> = mutableMapOf()

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false

        invokeGradleAction(
            { probe -> extensions.configure(LibraryExtension::class.java, probe) },
            libraryExtension
        )

        every { libraryExtension.defaultConfig(captureLambda()) } answers {
            lambda<(LibraryDefaultConfig) -> Unit>().invoke(defaultConfiguration)
        }

        every { defaultConfiguration.testInstrumentationRunnerArguments } returns runnerArguments

        // When
        AndroidLibraryConfigurator.configure(project, configuration)

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
    fun `Given configure is called with a Project and a AndroidLibraryConfiguration, it setups up the CompileOptions`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidLibraryConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            prefix = fixture(),
            publishVariants = emptySet(),
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
            androidTest = null,
            testRunner = TestRunner(
                runner = fixture(),
                arguments = fixture()
            )
        )

        val extensions: ExtensionContainer = mockk()
        val libraryExtension: LibraryExtension = mockk(relaxed = true)
        val compileOptions: CompileOptions = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false

        invokeGradleAction(
            { probe -> extensions.configure(LibraryExtension::class.java, probe) },
            libraryExtension
        )

        every { libraryExtension.compileOptions(captureLambda()) } answers {
            lambda<(CompileOptions) -> Unit>().invoke(compileOptions)
        }

        // When
        AndroidLibraryConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { compileOptions.targetCompatibility = configuration.compatibilityTargets.target }
        verify(exactly = 1) { compileOptions.sourceCompatibility = configuration.compatibilityTargets.source }
    }

    @Test
    fun `Given configure is called with a Project and a AndroidLibraryConfiguration, it setups up the SourceSets`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidLibraryConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            prefix = fixture(),
            publishVariants = emptySet(),
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
            androidTest = null,
            testRunner = TestRunner(
                runner = fixture(),
                arguments = fixture()
            )
        )

        val extensions: ExtensionContainer = mockk()
        val libraryExtension: LibraryExtension = mockk(relaxed = true)
        val sourceSets: NamedDomainObjectContainer<out AndroidSourceSet> = mockk(relaxed = true)
        val mainSource: AndroidSourceSet = mockk(relaxed = true)
        val manifest: AndroidSourceFile = mockk()
        val mainSourceDirectories: AndroidSourceDirectorySet = mockk()
        val mainResourceDirectories: AndroidSourceDirectorySet = mockk()

        val testSource: AndroidSourceSet = mockk(relaxed = true)
        val testSourceDirectories: AndroidSourceDirectorySet = mockk()
        val testResourceDirectories: AndroidSourceDirectorySet = mockk()

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false

        invokeGradleAction(
            { probe -> extensions.configure(LibraryExtension::class.java, probe) },
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

        every { sourceSets.getByName("androidTest") } returns mockk()

        // When
        AndroidLibraryConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { manifest.srcFile(configuration.mainSource.manifest) }
        verify(exactly = 1) { mainSourceDirectories.setSrcDirs(configuration.mainSource.sourceDirectories) }
        verify(exactly = 1) { mainResourceDirectories.setSrcDirs(configuration.mainSource.resourceDirectories) }

        verify(exactly = 1) { testSourceDirectories.setSrcDirs(configuration.unitTestSource.sourceDirectories) }
        verify(exactly = 1) { testResourceDirectories.setSrcDirs(configuration.unitTestSource.resourceDirectories) }

        verify(exactly = 0) { sourceSets.getByName("androidTest") }
    }

    @Test
    fun `Given configure is called with a Project and a AndroidLibraryConfiguration, it setups up the SourceSet for AndroidTest, if given`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidLibraryConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            prefix = fixture(),
            publishVariants = emptySet(),
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
        val libraryExtension: LibraryExtension = mockk(relaxed = true)
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
            { probe -> extensions.configure(LibraryExtension::class.java, probe) },
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
        AndroidLibraryConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { sourceSets.getByName("androidTest") }
        verify(exactly = 1) { androidTestSourceDirectories.setSrcDirs(configuration.androidTest!!.sourceDirectories) }
        verify(exactly = 1) { androidTestResourceDirectories.setSrcDirs(configuration.androidTest!!.resourceDirectories) }
    }

    @Test
    fun `Given configure is called with a Project and a Configuration, will not configure the kotlin extension if it is not KMP`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidLibraryConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            prefix = fixture(),
            publishVariants = emptySet(),
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

        every { project.extensions } returns extensions

        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false

        // When
        AndroidLibraryConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 0) { extensions.configure(KotlinMultiplatformExtension::class.java, any()) }
    }

    @Test
    fun `Given configure is called with a Project and a Configuration, will configure the kotlin extension for the  publishLibraryVariants if it is KMP`() {
        // Given
        val project: Project = mockk()
        val configuration = AndroidLibraryConfiguration(
            compileSdkVersion = fixture(),
            minSdkVersion = fixture(),
            targetSdkVersion = fixture(),
            prefix = fixture(),
            publishVariants = emptySet(),
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
        val kmpExtension: KotlinMultiplatformExtension = mockk()
        val androidTarget: KotlinAndroidTarget = mockk(relaxUnitFun = true)

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.extensions } returns extensions

        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()

        invokeGradleAction(
            { probe -> extensions.configure(KotlinMultiplatformExtension::class.java, probe) },
            kmpExtension
        )

        every { kmpExtension.android() } returns androidTarget

        // When
        AndroidLibraryConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { androidTarget.publishLibraryVariants(*configuration.publishVariants.toTypedArray()) }
    }
}
