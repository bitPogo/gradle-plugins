/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

object Version {

    val gradle = Gradle

    object Gradle {
        const val kotlin = Version.kotlin.stdlib

        /**
         * [AGP](https://developer.android.com/studio/releases/gradle-plugin)
         */
        const val android = "7.0.2"

        /**
         * [OWASP](https://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html)
         */
        const val owasp = "6.4.1"
    }

    // Kotlin
    val kotlin = Kotlin

    object Kotlin {
        /**
         * [Kotlin](https://github.com/JetBrains/kotlin)
         */
        const val stdlib = "1.5.31"

        /**
         * [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
         */
        const val coroutines = "1.5.2-native-mt"

        /**
         * [Koin](https://github.com/InsertKoinIO/koin)
         */
        const val koin = "3.1.2"
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
        const val ktx = "1.6.0"

        /**
         * [AppCompact]()https://developer.android.com/jetpack/androidx/releases/appcompat
         */
        const val appCompat = "1.3.1"

        /**
         * [Constrain Layout](https://developer.android.com/jetpack/androidx/releases/constraintlayout)
         */
        const val constraintLayout = "2.1.1"

        // Material
        /**
         * [Material Android](https://github.com/material-components/material-components-android)
         */
        const val material = "1.4.0"
    }

    val jvmTest = JvmTest

    object JvmTest {
        /**
         * [JUnit](https://github.com/junit-team/junit4/)
         */
        const val junit = "4.13.2"
    }

    val androidTest = AndroidTest

    object AndroidTest {
        /**
         * [Android Testing](https://developer.android.com/testing)
         */
        /**
         * [Android Testing Releases](https://developer.android.com/jetpack/androidx/releases/test)
         */
        const val test = "1.4.0"
        const val espresso = "3.4.0"

        /**
         * [Android UITest Automation](https://developer.android.com/training/testing/ui-testing/uiautomator-testing)
         */
        const val uiAutomator = "2.2.0"

        /**
         * [Robolectric](https://github.com/robolectric/robolectric)
         */
        const val robolectric = "4.6.1"
    }

    val kotlinTest = KotlinTest

    object KotlinTest {
        /**
         * [Kotest](https://github.com/kotest/kotest)
         */
        const val kotest = "4.6.3"

        /**
         * [mockk](http://mockk.io)
         */
        const val mockk = "1.12.0"
    }
}
