/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.android

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract.AndroidLibraryConfiguration
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract.TestSource
import tech.antibytes.gradle.configuration.ConfigurationContract
import tech.antibytes.gradle.util.isKmp

internal object AndroidLibraryConfigurator : ConfigurationContract.Configurator<AndroidLibraryConfiguration> {
    private fun setupAndroidExtension(
        extension: LibraryExtension,
        configuration: AndroidLibraryConfiguration,
    ) {
        extension.compileSdk = configuration.compileSdkVersion
        extension.resourcePrefix = configuration.prefix

        extension.defaultConfig {
            minSdk = configuration.minSdkVersion

            testInstrumentationRunner = configuration.testRunner.runner
            testInstrumentationRunnerArguments.putAll(
                configuration.testRunner.arguments,
            )
        }

        extension.compileOptions {
            targetCompatibility = configuration.compatibilityTargets.target
            sourceCompatibility = configuration.compatibilityTargets.source
        }

        extension.sourceSets {
            val main = getByName("main")
            main.manifest.srcFile(configuration.mainSource.manifest)
            main.java.setSrcDirs(configuration.mainSource.sourceDirectories)
            main.res.setSrcDirs(configuration.mainSource.resourceDirectories)

            val test = getByName("test")
            test.java.setSrcDirs(configuration.unitTestSource.sourceDirectories)
            test.res.setSrcDirs(configuration.unitTestSource.resourceDirectories)

            if (configuration.androidTest is TestSource) {
                val androidTest = getByName("androidTest")
                androidTest.java.setSrcDirs(configuration.androidTest!!.sourceDirectories)
                androidTest.res.setSrcDirs(configuration.androidTest!!.resourceDirectories)
            }
        }
    }

    private fun setupKmp(
        project: Project,
        configuration: AndroidLibraryConfiguration,
    ) {
        project.extensions.configure(KotlinMultiplatformExtension::class.java) {
            androidTarget().publishLibraryVariants(*configuration.publishVariants.toTypedArray())
        }
    }

    override fun configure(
        project: Project,
        configuration: AndroidLibraryConfiguration,
    ) {
        project.extensions.configure(LibraryExtension::class.java) {
            setupAndroidExtension(this, configuration)
        }

        if (project.isKmp()) {
            setupKmp(project, configuration)
        }
    }
}
