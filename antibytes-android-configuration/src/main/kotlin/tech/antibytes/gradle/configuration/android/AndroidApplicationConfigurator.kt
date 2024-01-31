/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.android

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract.AndroidApplicationConfiguration
import tech.antibytes.gradle.configuration.ConfigurationContract

internal object AndroidApplicationConfigurator : ConfigurationContract.Configurator<AndroidApplicationConfiguration> {
    private fun setupAndroidExtension(
        extension: ApplicationExtension,
        configuration: AndroidApplicationConfiguration,
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
            getByName("main").apply {
                manifest.srcFile(configuration.mainSource.manifest)
                java.setSrcDirs(configuration.mainSource.sourceDirectories)
                res.setSrcDirs(configuration.mainSource.resourceDirectories)
            }

            getByName("test").apply {
                java.setSrcDirs(configuration.unitTestSource.sourceDirectories)
                res.setSrcDirs(configuration.unitTestSource.resourceDirectories)
            }

            getByName("androidTest").apply {
                java.setSrcDirs(configuration.androidTest.sourceDirectories)
                res.setSrcDirs(configuration.androidTest.resourceDirectories)
            }
        }
    }

    override fun configure(
        project: Project,
        configuration: AndroidApplicationConfiguration,
    ) {
        project.extensions.configure(ApplicationExtension::class.java) {
            setupAndroidExtension(this, configuration)
        }
    }
}
