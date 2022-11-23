/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.catalog.GradleBundleVersion
import tech.antibytes.gradle.dependency.config.GradleVersions

internal object Android {
    /**
     * [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin)
     */
    val agp = GradleVersions.agp

    val appCompact = AppCompact

    /**
     * [AppCompact](https://developer.android.com/jetpack/androidx/releases/appcompat)
     */
    internal object AppCompact {
        private const val version = GradleVersions.appCompact

        const val core = version
        const val resources = version
    }

    /**
     * [Coil](https://github.com/coil-kt/coil)
     */
    val coil = Coil

    internal object Coil {
        private const val version = GradleVersions.coil
        const val core = version
        const val compose = version
    }

    val constraintLayout = ConstraintLayout

    /**
     * [Constrain Layout](https://developer.android.com/jetpack/androidx/releases/constraintlayout)
     */
    internal object ConstraintLayout {
        const val core = GradleVersions.constraintLayout
        const val compose = GradleVersions.constraintLayoutCompose
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
            private const val version = GradleVersions.androidComposeAnimation
            const val core = version
            const val runtime = version
            const val graphics = version
        }

        const val bom = GradleVersions.androidComposeBom
        const val compiler = GradleVersions.androidComposeCompiler
        val foundation = Foundation

        internal object Foundation {
            private const val version = GradleVersions.androidComposeFoundation
            const val core = version
            const val layout = version
        }

        const val runtime = GradleVersions.androidComposeRuntime
        const val saveable = GradleVersions.androidComposeSaveable
        val ui = UI

        internal object UI {
            private const val version = GradleVersions.androidComposeUi
            const val core = version
            const val geometry = version
            const val graphics = version
            const val text = version
            const val util = version
            const val unit = version

            val tooling = Tooling

            internal object Tooling {
                private const val version = GradleVersions.androidComposeUiTooling
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
    const val desugar = GradleVersions.desugar

    val hilt = Hilt

    internal object Hilt {
        /**
         * [Google Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
         */
        private const val version = GradleVersions.hilt

        const val core = version
        val gradle = GradleBundleVersion(version)
        const val compiler = version
        const val test = version

        /**
         * [Google Hilt Compose](https://developer.android.com/jetpack/androidx/releases/hilt)
         */
        const val compose = GradleVersions.hiltCompose
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
        const val core = GradleVersions.androidKtxCore

        /**
         * [Annotation](https://developer.android.com/jetpack/androidx/releases/annotation)
         */
        const val annotation = GradleVersions.androidKtxAnnotation

        /**
         * [Android Tech](https://androidx.tech/artifacts/activity/activity-ktx/)
         */
        val activity = Activity

        internal object Activity {
            private const val version = GradleVersions.androidKtxActivity
            const val core = version
            const val compose = version
        }

        const val collections = GradleVersions.androidKtxCollections
        const val fragment = GradleVersions.androidKtxFragment
        const val lifecycle = GradleVersions.androidKtxLifecycle
        val navigation = Navigation

        internal object Navigation {
            private const val version = GradleVersions.androidKtxNavigation

            val runtime = version
            val fragment = version
            val ui = version
            val compose = version
        }

        const val palette = GradleVersions.androidKtxPalette
        val paging = Paging

        /**
         * [Androidx Pageing](https://developer.android.com/jetpack/androidx/releases/paging)
         */
        internal object Paging {
            const val core = GradleVersions.androidKtxPagingCore
            const val compose = GradleVersions.androidKtxPagingCompose
        }

        val viewmodel = Viewmodel

        internal object Viewmodel {
            private const val version = GradleVersions.androidKtxViewmodel
            const val core = version
            const val saver = version
            const val compose = version
        }

        const val workmanager = GradleVersions.androidKtxWorkmanager
    }

    // Material
    /**
     * [Material Android](https://github.com/material-components/material-components-android)
     */
    val material = Material

    internal object Material {
        private const val version = GradleVersions.androidMaterial
        const val core = version
        val compose = Compose

        internal object Compose {
            private const val version = GradleVersions.androidMaterialCompose
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
        private const val version = GradleVersions.androidMaterial3
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
            private const val version = GradleVersions.androidTestCompose
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
        const val core = GradleVersions.androidTestCore
        const val ktx = GradleVersions.androidTestKtx
        const val runner = GradleVersions.androidTestRunner
        const val rules = GradleVersions.androidTestRules

        val espresso = Espresso
        internal object Espresso {
            private const val version = GradleVersions.androidTestEspresso

            const val core = version
            const val intents = version
            const val web = version
        }

        const val orchestrator = GradleVersions.androidTestOrchestrator

        val junit = JUnit
        internal object JUnit {
            private const val version = GradleVersions.androidTestJunit
            const val core = version
            const val ktx = version
        }

        /**
         * [Android UITest Automation explained](https://developer.android.com/training/testing/ui-testing/uiautomator-testing)
         * [Android UITest Automation](https://androidx.tech/artifacts/test.uiautomator/uiautomator/)
         */
        const val uiAutomator = GradleVersions.androidTestUiAutomator

        /**
         * [Robolectric](https://github.com/robolectric/robolectric)
         */
        const val robolectric = GradleVersions.androidTestRobolectric
    }
}
