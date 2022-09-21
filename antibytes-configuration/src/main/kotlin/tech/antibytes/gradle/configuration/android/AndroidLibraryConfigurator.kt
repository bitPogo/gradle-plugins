/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.android

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract
import tech.antibytes.gradle.configuration.ConfigurationContract
import tech.antibytes.gradle.util.isKmp

internal object AndroidLibraryConfigurator : ConfigurationContract.AndroidLibraryConfigurator {
    private fun setupAndroidExtension(
        extension: LibraryExtension,
        configuration: AndroidConfigurationApiContract.AndroidLibraryConfiguration,
    ) {
        extension.compileSdk = configuration.compileSdkVersion
        extension.resourcePrefix = configuration.prefix

        extension.defaultConfig {
            minSdk = configuration.minSdkVersion
            targetSdk = configuration.targetSdkVersion

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

            if (configuration.androidTest is AndroidConfigurationApiContract.TestSource) {
                val androidTest = getByName("androidTest")
                androidTest.java.setSrcDirs(configuration.androidTest!!.sourceDirectories)
                androidTest.res.setSrcDirs(configuration.androidTest!!.resourceDirectories)
            }
        }
    }

    private fun setupKmp(
        project: Project,
        configuration: AndroidConfigurationApiContract.AndroidLibraryConfiguration,
    ) {
        project.extensions.configure(KotlinMultiplatformExtension::class.java) {
            android().publishLibraryVariants(*configuration.publishVariants.toTypedArray())
        }
    }

    override fun configure(
        project: Project,
        configuration: AndroidConfigurationApiContract.AndroidLibraryConfiguration,
    ) {
        project.extensions.configure(LibraryExtension::class.java) {
            setupAndroidExtension(this, configuration)
        }

        if (project.isKmp()) {
            setupKmp(project, configuration)
        }
    }
}
