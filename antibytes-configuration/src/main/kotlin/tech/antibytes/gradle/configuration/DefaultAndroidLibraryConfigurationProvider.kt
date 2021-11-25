/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import tech.antibytes.gradle.configuration.api.AndroidLibraryConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import tech.antibytes.gradle.util.PlatformContextResolver
import tech.antibytes.gradle.util.isAndroidLibrary

internal object DefaultAndroidLibraryConfigurationProvider : ConfigurationContract.DefaultAndroidLibraryConfigurationProvider {
    private fun hasAndroidLibrary(contexts: Set<PlatformContext>): Boolean {
        return contexts.any { context -> context.isAndroidLibrary() }
    }

    private fun determinePublishingVariants(contexts: Set<PlatformContext>): Set<String> {
        return if (contexts.any { context -> context == PlatformContext.ANDROID_LIBRARY_KMP }) {
            setOf("release", "debug")
        } else {
            emptySet()
        }
    }

    override fun createDefaultConfiguration(
        project: Project
    ): ConfigurationApiContract.AndroidLibraryConfiguration? {
        val contexts = PlatformContextResolver.getType(project)

        return if (hasAndroidLibrary(contexts)) {
            AndroidLibraryConfiguration(
                minSdkVersion = 23,
                targetSdkVersion = 30,
                compileSdkVersion = 30,
                projectInfix = "",
                publishVariants = determinePublishingVariants(contexts),
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
        } else {
            null
        }
    }
}
