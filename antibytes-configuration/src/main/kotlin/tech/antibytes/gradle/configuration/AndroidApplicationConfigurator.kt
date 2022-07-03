/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

internal object AndroidApplicationConfigurator : ConfigurationContract.AndroidApplicationConfigurator {
    private fun setupAndroidExtension(
        extension: ApplicationExtension,
        configuration: ConfigurationApiContract.AndroidApplicationConfiguration,
    ) {
        extension.compileSdk = configuration.compileSdkVersion

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

            val androidTest = getByName("androidTest")
            androidTest.java.setSrcDirs(configuration.androidTest.sourceDirectories)
            androidTest.res.setSrcDirs(configuration.androidTest.resourceDirectories)
        }
    }

    override fun configure(
        project: Project,
        configuration: ConfigurationApiContract.AndroidApplicationConfiguration,
    ) {
        project.extensions.configure(ApplicationExtension::class.java) {
            setupAndroidExtension(this, configuration)
        }
    }
}
