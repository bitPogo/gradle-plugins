/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.JavaVersion

internal interface ConfigurationApiContract {
    interface TestSource {
        val sourceDirectories: Set<String>
        val resourceDirectories: Set<String>
    }

    interface MainSource {
        val manifest: String
        val sourceDirectories: Set<String>
        val resourceDirectories: Set<String>
    }

    interface Compatibility {
        val source: JavaVersion
        val target: JavaVersion
    }

    interface TestRunner {
        val runner: String
        val arguments: Map<String, String>
    }

    interface AndroidBaseConfiguration {
        val compileSdkVersion: Int
        val minSdkVersion: Int
        val targetSdkVersion: Int

        val prefix: String

        val compatibilityTargets: Compatibility
        val fallbacks: Map<String, Set<String>>

        val mainSource: MainSource
        val unitTestSource: TestSource
        val testRunner: TestRunner
    }

    interface AndroidLibraryConfiguration : AndroidBaseConfiguration {
        val publishVariants: Set<String>

        /**
         * TestSource for AndroidTest (UI-Test)
         *
         * This will add an android UI-test folder to the configuration, which is typically not needed for functional libraries and just a thing for apps
         * Note: The established pattern for this is to set value of unitTestSource to `androidUnitTest` and so this may can take over the name of `androidTest` and avoids `androidAndroidTest`
         */
        val androidTest: TestSource?
    }

    companion object {
        const val ANDROID_PREFIX = "antibytes"
        const val ANDROID_PREFIX_SEPARATOR = "_"
        const val TARGET_SDK = 31
        const val MIN_SDK = 23
        val FALLBACKS = mapOf("debug" to setOf("release"))
        val COMPATIBILITY_TARGETS = JavaVersion.VERSION_1_8
        const val TEST_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
        val TEST_RUNNER_ARGUMENTS = mapOf("clearPackageData" to "true")
    }
}
