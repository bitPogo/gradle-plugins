/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.catalog.GradleBundleVersion

internal object Android {
    /**
     * [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin)
     */
    val agp = "7.3.0"

    val appCompact = AppCompact

    /**
     * [AppCompact](https://developer.android.com/jetpack/androidx/releases/appcompat)
     */
    internal object AppCompact {
        private const val version = "1.5.1"

        const val core = version
        const val resources = version
    }

    /**
     * [Coil](https://github.com/coil-kt/coil)
     */
    val coil = Coil

    internal object Coil {
        private const val version = "2.2.1"
        const val core = version
        const val compose = version
    }

    val constraintLayout = ConstraintLayout

    /**
     * [Constrain Layout](https://developer.android.com/jetpack/androidx/releases/constraintlayout)
     */
    internal object ConstraintLayout {
        const val core = "2.1.4"
        const val compose = "1.0.1"
    }

    /**
     * [Jetpack Compose](https://developer.android.com/jetpack/androidx/releases/compose)
     * [Release Notes](https://developer.android.com/jetpack/androidx/versions/all-channel)
     * [Compatibility Notes](https://developer.android.com/jetpack/androidx/releases/compose-kotlin)
     * [Overview](https://developer.android.com/jetpack/compose/setup)
     */

    val compose = Compose

    internal object Compose {
        val animation = Animation

        internal object Animation {
            private const val version = "1.2.1"
            const val core = version
            const val runtime = version
            const val graphics = version
        }

        const val bom = "2022.11.00"
        const val compiler = "1.3.1"
        val foundation = Foundation

        internal object Foundation {
            private const val version = "1.2.1"
            const val core = version
            const val layout = version
        }

        const val runtime = "1.2.1"
        const val saveable = "1.2.1"
        val ui = UI

        internal object UI {
            private const val version = "1.2.1"
            const val core = version
            const val geometry = version
            const val graphics = version
            const val text = version
            const val util = version
            const val unit = version

            val tooling = Tooling
            internal object Tooling {
                private const val version = "1.2.1"
                const val core = version
                const val data = version
                const val preview = version
            }
        }
    }

    /**
     * [Android Desugaring explained](https://developer.android.com/studio/write/java8-support)
     * [Android Desugaring](https://github.com/google/desugar_jdk_libs)
     */
    const val desugar = "1.1.5"

    val hilt = Hilt

    internal object Hilt {
        /**
         * [Google Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
         */
        private const val version = "2.43.2"

        const val core = version
        val gradle = GradleBundleVersion(version)
        const val compiler = version
        const val test = version

        /**
         * [Google Hilt Compose](https://developer.android.com/jetpack/androidx/releases/hilt)
         */
        const val compose = "1.0.0"
    }

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
    internal object Ktx {
        const val core = "1.9.0"

        /**
         * [Annotation](https://developer.android.com/jetpack/androidx/releases/annotation)
         */
        const val annotation = "1.4.0"

        /**
         * [Android Tech](https://androidx.tech/artifacts/activity/activity-ktx/)
         */
        val activity = Activity

        internal object Activity {
            private const val version = "1.5.1"
            const val core = version
            const val compose = version
        }

        const val collections = "1.2.0"
        const val lifecycle = "2.5.1"
        const val fragment = "1.5.2"
        const val livedata = "2.5.1"
        val navigation = Navigation

        internal object Navigation {
            private const val version = "2.5.2"

            val runtime = version
            val fragment = version
            val ui = version
            val compose = version
        }

        const val palette = "1.0.0"
        val paging = Paging

        /**
         * [Androidx Pageing](https://developer.android.com/jetpack/androidx/releases/paging)
         */
        internal object Paging {
            const val core = "3.1.1"
            const val compose = "1.0.0-alpha16"
        }

        val viewmodel = Viewmodel

        internal object Viewmodel {
            private const val version = "2.5.1"
            const val core = version
            const val saver = version
            const val compose = version
        }

        const val workmanager = "2.7.1"
    }

    // Material
    /**
     * [Material Android](https://github.com/material-components/material-components-android)
     */
    val material = Material

    internal object Material {
        private const val version = "1.7.0-rc01"
        const val core = version
        val compose = Compose

        internal object Compose {
            private const val version = "1.2.1"
            const val core = version
            const val icons = version
            const val extendedIcons = version
            const val ripple = version
        }
    }

    /**
     * [Material3 Compose](https://developer.android.com/jetpack/androidx/releases/compose-material3)
     */
    val material3 = Material3

    internal object Material3 {
        private const val version = "1.0.1"
        const val core = version
        const val windowSize = version
    }

    val test = Test

    internal object Test {
        /**
         * [Jetpack Compose](https://developer.android.com/jetpack/androidx/releases/compose)
         * [Release Notes](https://developer.android.com/jetpack/androidx/versions/all-channel)
         * [Compatibility Notes](https://developer.android.com/jetpack/androidx/releases/compose-kotlin)
         * [Overview](https://developer.android.com/jetpack/compose/setup)
         */
        val compose = Compose
        internal object Compose {
            private const val version = "1.2.1"
            const val core = version
            const val manifest = version
            const val junit4 = version
        }

        /**
         * [Android Testing](https://developer.android.com/testing)
         */
        /**
         * [Android Testing Releases](https://developer.android.com/jetpack/androidx/releases/test)
         */
        const val core = "1.4.0"
        const val ktx = "1.4.0"
        const val runner = "1.4.0"
        const val rules = "1.4.0"

        val espresso = Espresso
        internal object Espresso {
            private const val version = "3.4.0"

            const val core = version
            const val intents = version
            const val web = version
        }

        const val orchestrator = "1.4.1"

        val junit = JUnit
        internal object JUnit {
            private const val version = "1.1.3"
            const val core = version
            const val ktx = version
        }

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
}
