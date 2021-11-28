/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.ANDROID_PREFIX
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.ANDROID_PREFIX_SEPARATOR
import tech.antibytes.gradle.configuration.api.AndroidLibraryConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import tech.antibytes.gradle.util.PlatformContextResolver

internal object DefaultAndroidLibraryConfigurationProvider : ConfigurationContract.DefaultAndroidLibraryConfigurationProvider {
    private fun isAndroidKmpLibrary(contexts: Set<PlatformContext>): Boolean {
        return contexts.any { context -> context == PlatformContext.ANDROID_LIBRARY_KMP }
    }

    private fun determinePublishingVariants(contexts: Set<PlatformContext>): Set<String> {
        return if (isAndroidKmpLibrary(contexts)) {
            setOf("release")
        } else {
            emptySet()
        }
    }

    private fun determineMainSource(contexts: Set<PlatformContext>): MainSource {
        return if (isAndroidKmpLibrary(contexts)) {
            MainSource(
                manifest = "src/androidMain/AndroidManifest.xml",
                sourceDirectories = setOf("src/androidMain/kotlin"),
                resourceDirectories = setOf("src/androidMain/res")
            )
        } else {
            MainSource(
                manifest = "src/main/AndroidManifest.xml",
                sourceDirectories = setOf("src/main/kotlin"),
                resourceDirectories = setOf("src/main/res")
            )
        }
    }

    private fun determineTestSource(contexts: Set<PlatformContext>): TestSource {
        return if (isAndroidKmpLibrary(contexts)) {
            TestSource(
                sourceDirectories = setOf("src/androidTest/kotlin"),
                resourceDirectories = setOf("src/androidTest/res")
            )
        } else {
            TestSource(
                sourceDirectories = setOf("src/test/kotlin"),
                resourceDirectories = setOf("src/test/res")
            )
        }
    }

    private fun determinePrefix(project: Project): String {
        val projectInfix = project.name.replace("-", ANDROID_PREFIX_SEPARATOR)

        return "${ANDROID_PREFIX}${ANDROID_PREFIX_SEPARATOR}${projectInfix}$ANDROID_PREFIX_SEPARATOR"
    }

    override fun createDefaultConfiguration(
        project: Project
    ): ConfigurationApiContract.AndroidLibraryConfiguration {
        val contexts = PlatformContextResolver.getType(project)

        return AndroidLibraryConfiguration(
            compileSdkVersion = 30,
            minSdkVersion = 23,
            targetSdkVersion = 30,
            prefix = determinePrefix(project),
            publishVariants = determinePublishingVariants(contexts),
            compatibilityTargets = Compatibility(
                target = JavaVersion.VERSION_1_8,
                source = JavaVersion.VERSION_1_8
            ),
            fallbacks = mapOf("debug" to setOf("release")),
            mainSource = determineMainSource(contexts),
            unitTestSource = determineTestSource(contexts),
            androidTest = null,
            testRunner = TestRunner(
                runner = "androidx.test.runner.AndroidJUnitRunner",
                arguments = mapOf("clearPackageData" to "true")
            )
        )
    }
}
