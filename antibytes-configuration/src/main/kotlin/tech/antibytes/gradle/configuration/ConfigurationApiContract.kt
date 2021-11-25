/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.JavaVersion

interface ConfigurationApiContract {
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
        var minSdkVersion: Int
        var compileSdkVersion: Int
        var targetSdkVersion: Int
        var projectInfix: String

        val prefix: String
            get() {
                return "antibytes_${this.projectInfix}"
            }

        var compatibilityTargets: Compatibility
        var fallbacks: Map<String, Set<String>>

        var mainSource: MainSource
        var unitTestSource: TestSource
        var testRunner: TestRunner
    }

    interface AndroidLibraryConfiguration : AndroidBaseConfiguration {
        var publishVariants: Set<String>

        /**
         * TestSource for AndroidTest (UI-Test)
         *
         * This will add an android UI-test folder to the configuration, which is typically not needed for functional libraries and just a thing for apps
         * Note: The established pattern for this is to set value of unitTestSource to `androidUnitTest` and so this may can take over the name of `androidTest` and avoids `androidAndroidTest`
         */
        var androidTest: TestSource?
    }
}
