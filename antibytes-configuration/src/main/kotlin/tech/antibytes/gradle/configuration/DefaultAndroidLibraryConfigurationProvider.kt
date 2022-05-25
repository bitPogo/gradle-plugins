/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Project
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.ANDROID_PREFIX
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.ANDROID_PREFIX_SEPARATOR
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.COMPATIBILITY_TARGETS
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.FALLBACKS
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.MIN_SDK
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.TARGET_SDK
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.TEST_RUNNER
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.TEST_RUNNER_ARGUMENTS
import tech.antibytes.gradle.configuration.api.AndroidLibraryConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import tech.antibytes.gradle.util.PlatformContextResolver

internal object DefaultAndroidLibraryConfigurationProvider : ConfigurationContract.DefaultAndroidLibraryConfigurationProvider {
    private fun determineTestSource(sourceDir: String): TestSource {
        return TestSource(
            sourceDirectories = setOf("src/$sourceDir/kotlin"),
            resourceDirectories = setOf(
                "src/$sourceDir/res",
                "src/$sourceDir/resources",
            )
        )
    }

    private fun determineInstrumentedTestSource(contexts: Set<PlatformContext>): TestSource {
        return if (isAndroidKmpLibrary(contexts)) {
            determineTestSource("androidAndroidTest")
        } else {
            determineTestSource("androidTest")
        }
    }

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
                resourceDirectories = setOf(
                    "src/androidMain/res",
                    "src/androidMain/resources"
                )
            )
        } else {
            MainSource(
                manifest = "src/main/AndroidManifest.xml",
                sourceDirectories = setOf("src/main/kotlin"),
                resourceDirectories = setOf(
                    "src/main/res",
                    "src/main/resources"
                )
            )
        }
    }

    private fun determineTestSource(contexts: Set<PlatformContext>): TestSource {
        return if (isAndroidKmpLibrary(contexts)) {
            determineTestSource("androidTest")
        } else {
            determineTestSource("test")
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
            compileSdkVersion = TARGET_SDK,
            minSdkVersion = MIN_SDK,
            targetSdkVersion = TARGET_SDK,
            prefix = determinePrefix(project),
            publishVariants = determinePublishingVariants(contexts),
            compatibilityTargets = Compatibility(
                target = COMPATIBILITY_TARGETS,
                source = COMPATIBILITY_TARGETS
            ),
            fallbacks = FALLBACKS,
            mainSource = determineMainSource(contexts),
            unitTestSource = determineTestSource(contexts),
            androidTest = determineInstrumentedTestSource(contexts),
            testRunner = TestRunner(
                runner = TEST_RUNNER,
                arguments = TEST_RUNNER_ARGUMENTS
            )
        )
    }
}
