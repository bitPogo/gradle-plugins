/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Project
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.COMPATIBILITY_TARGETS
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.FALLBACKS
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.MIN_SDK
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.TARGET_SDK
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.TEST_RUNNER
import tech.antibytes.gradle.configuration.ConfigurationApiContract.Companion.TEST_RUNNER_ARGUMENTS
import tech.antibytes.gradle.configuration.api.AndroidApplicationConfiguration
import tech.antibytes.gradle.configuration.api.Compatibility
import tech.antibytes.gradle.configuration.api.MainSource
import tech.antibytes.gradle.configuration.api.TestRunner
import tech.antibytes.gradle.configuration.api.TestSource

internal object DefaultAndroidApplicationConfigurationProvider : ConfigurationContract.DefaultAndroidApplicationConfigurationProvider {
    private fun determineTestSource(sourceDir: String): TestSource {
        return TestSource(
            sourceDirectories = setOf("src/$sourceDir/kotlin"),
            resourceDirectories = setOf(
                "src/$sourceDir/res",
                "src/$sourceDir/resources",
            )
        )
    }

    private fun determineMainSource(): MainSource {
        return MainSource(
            manifest = "src/main/AndroidManifest.xml",
            sourceDirectories = setOf("src/main/kotlin"),
            resourceDirectories = setOf(
                "src/main/res",
                "src/main/resources"
            )
        )
    }

    private fun determineTestSource(): TestSource = determineTestSource("test")

    private fun determineInstrumentedTestSource(): TestSource = determineTestSource("androidTest")

    override fun createDefaultConfiguration(
        project: Project
    ): ConfigurationApiContract.AndroidApplicationConfiguration {
        return AndroidApplicationConfiguration(
            compileSdkVersion = TARGET_SDK,
            minSdkVersion = MIN_SDK,
            targetSdkVersion = TARGET_SDK,
            compatibilityTargets = Compatibility(
                target = COMPATIBILITY_TARGETS,
                source = COMPATIBILITY_TARGETS
            ),
            fallbacks = FALLBACKS,
            mainSource = determineMainSource(),
            unitTestSource = determineTestSource(),
            androidTest = determineInstrumentedTestSource(),
            testRunner = TestRunner(
                runner = TEST_RUNNER,
                arguments = TEST_RUNNER_ARGUMENTS
            )
        )
    }
}
