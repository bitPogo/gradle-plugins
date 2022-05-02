/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

object Version {

    val gradle = Gradle

    object Gradle {
        /**
         * [KTLint](https://github.com/pinterest/ktlint/releases)
         */
        const val ktLint = "0.44.0"
    }

    // Kotlin
    val kotlin = Kotlin

    object Kotlin {
        /**
         * [Kotlin](https://github.com/JetBrains/kotlin)
         */
        const val language = "1.6.10"

        const val stdlib = language

        /**
         * [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
         */
        const val coroutines = "1.6.0-native-mt"

        /**
         * [Koin](https://github.com/InsertKoinIO/koin)
         */
        const val koin = "3.1.6"

        /**
         * [Ktor](https://github.com/ktorio/ktor)
         */
        const val ktor = "1.6.8"

        /**
         * [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
         */
        const val serialization = "1.3.2"

        /**
         * [Kotlin DateTime](https://github.com/Kotlin/kotlinx-datetime)
         */
        const val dateTime = "0.3.2"

        /**
         * [UUID](https://github.com/benasher44/uuid)
         */
        const val uuid = "0.4.0"

        /**
         * [Stately](https://github.com/touchlab/Stately)
         */
        const val stately = "1.2.1"

        /**
         * (AtomicFu)(https://github.com/Kotlin/kotlinx.atomicfu)
         */
        const val atomicFu = "0.17.1"

        val test = Test

        object Test {
            /**
             * [mockk](http://mockk.io)
             */
            const val mockk = "1.12.3"
        }
    }

    val android = Android

    object Android {
        /**
         * [Android Desugaring explained](https://developer.android.com/studio/write/java8-support)
         * [Android Desugaring](https://github.com/google/desugar_jdk_libs)
         */
        const val desugar = "1.1.5"

        // AndroidX
        /**
         * [AndroidX](https://developer.android.com/jetpack/androidx)
         */

        /**
         * [AndroidX Core](https://developer.android.com/kotlin/ktx)
         */
        val ktx = Ktx

        /**
         * [AndroidX Tech](https://androidx.tech/)
         */
        object Ktx {
            const val core = "1.7.0"

            /**
             * [Android Tech](https://androidx.tech/artifacts/activity/activity-ktx/)
             */
            const val activity = "1.4.0"
            const val collections = "1.2.0"
            const val lifecycle = "2.4.1"
            const val fragment = "1.4.1"
            const val livedata = "2.4.1"
            const val navigation = "2.4.2"
            const val palette = "1.0.0"
            const val viewmodel = "2.4.1"
            const val workmanager = "2.7.1"
        }

        /**
         * [AppCompact](https://developer.android.com/jetpack/androidx/releases/appcompat)
         */
        const val appCompat = "1.4.1"

        /**
         * [Constrain Layout](https://developer.android.com/jetpack/androidx/releases/constraintlayout)
         */
        const val constraintLayout = "2.1.3"

        // Material
        /**
         * [Material Android](https://github.com/material-components/material-components-android)
         */
        const val material = "1.6.0-rc01"

        /**
         * [Jetpack Compose](https://developer.android.com/jetpack/androidx/releases/compose)
         * [Release Notes](https://developer.android.com/jetpack/androidx/versions/all-channel)
         * [Compatibility Notes](https://developer.android.com/jetpack/androidx/releases/compose-kotlin)
         */

        val compose = Compose

        object Compose {
            const val core = "1.1.1"
            const val compiler = "1.1.1"
            const val constraintLayout = "1.0.0"
            const val activity = "1.4.0"
            const val viewmodel = "2.4.1"
        }

        val test = Test

        object Test {
            /**
             * [Android Testing](https://developer.android.com/testing)
             */
            /**
             * [Android Testing Releases](https://developer.android.com/jetpack/androidx/releases/test)
             */
            const val test = "1.4.0"
            const val espresso = "3.4.0"
            const val orchestrator = "1.4.1"
            const val junit = "1.1.3"

            /**
             * [Android UITest Automation explained](https://developer.android.com/training/testing/ui-testing/uiautomator-testing)
             * [Android UITest Automation](https://androidx.tech/artifacts/test.uiautomator/uiautomator/)
             */
            const val uiAutomator = "2.2.0"

            /**
             * [Robolectric](https://github.com/robolectric/robolectric)
             */
            const val robolectric = "4.8"
        }
    }

    val jvm = Jvm

    object Jvm {
        val test = Test

        object Test {
            /**
             * [JUnit](https://github.com/junit-team/junit5/)
             */
            const val junit = "5.8.2"

            /**
             * [JUnit](https://github.com/junit-team/junit4/)
             */
            const val junit4 = "4.13.2"
        }
    }

    val js = JS

    object JS {
        /**
         * [KotlinNodeJS](https://github.com/Kotlin/kotlinx-nodejs)
         */
        const val nodeJs = "0.0.7"

        val test = Test

        object Test
    }
}
