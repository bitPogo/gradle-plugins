/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.android

import org.gradle.api.Project
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract.Companion.COMPATIBILITY_TARGETS
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract.Companion.FALLBACKS
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract.Companion.MIN_SDK
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract.Companion.TARGET_SDK
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract.Companion.TEST_RUNNER
import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract.Companion.TEST_RUNNER_ARGUMENTS
import tech.antibytes.gradle.configuration.ConfigurationContract
import tech.antibytes.gradle.configuration.api.AndroidApplicationConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import tech.antibytes.gradle.util.PlatformContextResolver

internal object DefaultAndroidApplicationConfigurationProvider : ConfigurationContract.DefaultAndroidApplicationConfigurationProvider {
    private fun determineTestSource(sourceDir: String): TestSource {
        return TestSource(
            sourceDirectories = setOf("src/$sourceDir/kotlin"),
            resourceDirectories = setOf(
                "src/$sourceDir/res",
                "src/$sourceDir/resources",
            ),
        )
    }

    private fun isAndroidKmpApp(contexts: Set<PlatformContext>): Boolean {
        return contexts.any { context -> context == PlatformContext.ANDROID_APPLICATION_KMP }
    }

    private fun determineMainSource(contexts: Set<PlatformContext>): MainSource {
        return if (isAndroidKmpApp(contexts)) {
            MainSource(
                manifest = "src/androidMain/AndroidManifest.xml",
                sourceDirectories = setOf("src/androidMain/kotlin"),
                resourceDirectories = setOf(
                    "src/androidMain/res",
                    "src/androidMain/resources",
                ),
            )
        } else {
            MainSource(
                manifest = "src/main/AndroidManifest.xml",
                sourceDirectories = setOf("src/main/kotlin"),
                resourceDirectories = setOf(
                    "src/main/res",
                    "src/main/resources",
                ),
            )
        }
    }

    private fun determineTestSource(contexts: Set<PlatformContext>): TestSource {
        return if (isAndroidKmpApp(contexts)) {
            determineTestSource("androidTest")
        } else {
            determineTestSource("test")
        }
    }

    private fun determineInstrumentedTestSource(contexts: Set<PlatformContext>): TestSource {
        return if (isAndroidKmpApp(contexts)) {
            determineTestSource("androidAndroidTest")
        } else {
            determineTestSource("androidTest")
        }
    }

    override fun createDefaultConfiguration(
        project: Project,
    ): AndroidConfigurationApiContract.AndroidApplicationConfiguration {
        val contexts = PlatformContextResolver.getType(project)

        return AndroidApplicationConfiguration(
            compileSdkVersion = TARGET_SDK,
            minSdkVersion = MIN_SDK,
            targetSdkVersion = TARGET_SDK,
            compatibilityTargets = Compatibility(
                target = COMPATIBILITY_TARGETS,
                source = COMPATIBILITY_TARGETS,
            ),
            fallbacks = FALLBACKS,
            mainSource = determineMainSource(contexts),
            unitTestSource = determineTestSource(contexts),
            androidTest = determineInstrumentedTestSource(contexts),
            testRunner = TestRunner(
                runner = TEST_RUNNER,
                arguments = TEST_RUNNER_ARGUMENTS,
            ),
        )
    }
}
