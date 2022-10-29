/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

internal object Android {
    /**
     * [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin)
     */
    const val androidGradlePlugin = "7.3.0"

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
        const val core = "1.9.0"

        /**
         * [Android Tech](https://androidx.tech/artifacts/activity/activity-ktx/)
         */
        const val activity = "1.5.1"
        const val collections = "1.2.0"
        const val lifecycle = "2.5.1"
        const val fragment = "1.5.2"
        const val livedata = "2.5.1"
        const val navigation = "2.5.2"
        const val palette = "1.0.0"
        const val pageing = "3.1.1"
        const val viewmodel = "2.5.1"
        const val workmanager = "2.7.1"

        /**
         * [Annotation](https://developer.android.com/jetpack/androidx/releases/annotation)
         */
        const val annotation = "1.4.0"
    }

    /**
     * [AppCompact](https://developer.android.com/jetpack/androidx/releases/appcompat)
     */
    const val appCompat = "1.5.1"

    /**
     * [Constrain Layout](https://developer.android.com/jetpack/androidx/releases/constraintlayout)
     */
    const val constraintLayout = "2.1.4"

    // Material
    /**
     * [Material Android](https://github.com/material-components/material-components-android)
     */
    const val material = "1.7.0-rc01"

    /**
     * [Jetpack Compose](https://developer.android.com/jetpack/androidx/releases/compose)
     * [Release Notes](https://developer.android.com/jetpack/androidx/versions/all-channel)
     * [Compatibility Notes](https://developer.android.com/jetpack/androidx/releases/compose-kotlin)
     */

    val compose = Compose

    object Compose {
        const val core = "1.2.1"
        const val compiler = "1.3.1"
        const val foundation = "1.2.1"
        const val material = "1.2.1"
        const val runtime = "1.2.1"
        const val ui = "1.2.1"
        const val constraintLayout = "1.0.1"
        const val activity = "1.5.1"
        const val viewmodel = "2.5.1"
        const val paging = "1.0.0-alpha16"
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
        const val robolectric = "4.8.2"
    }

    /**
     * [Coil](https://github.com/coil-kt/coil)
     */
    const val coil = "2.2.1"
}
