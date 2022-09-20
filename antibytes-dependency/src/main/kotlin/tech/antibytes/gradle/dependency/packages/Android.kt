/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages

import tech.antibytes.gradle.dependency.Version

object Android {
    // AGP
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Version.android.androidGradlePlugin}"

    // Android
    const val desugar = "com.android.tools:desugar_jdk_libs:${Version.android.desugar}"

    // AndroidX
    val ktx = Ktx

    object Ktx {
        const val annotation = "androidx.annotation:annotation:${Version.android.ktx.annotation}"
        const val core = "androidx.core:core-ktx:${Version.android.ktx.core}"
        const val activity = "androidx.activity:activity-ktx:${Version.android.ktx.activity}"
        const val collections = "androidx.collection:collection-ktx:${Version.android.ktx.collections}"
        const val fragment = "androidx.fragment:fragment-ktx:${Version.android.ktx.fragment}"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.android.ktx.lifecycle}"
        const val navigationRuntime = "androidx.navigation:navigation-runtime-ktx:${Version.android.ktx.navigation}"
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Version.android.ktx.navigation}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Version.android.ktx.navigation}"
        const val palette = "androidx.palette:palette-ktx:${Version.android.ktx.palette}"
        const val paging = "androidx.paging:paging-runtime:${Version.android.ktx.pageing}"
        const val runtime = "androidx.compose.runtime:runtime:${Version.android.compose.runtime}"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.android.ktx.viewmodel}"
        const val viewmodelSaver = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.android.ktx.viewmodel}"
        const val viewmodelCoroutine = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.android.ktx.viewmodel}"
        const val workmanager = "androidx.work:work-runtime-ktx:${Version.android.ktx.workmanager}"
    }

    val appCompact = AppCompact

    object AppCompact {
        const val core = "androidx.appcompat:appcompat:${Version.android.appCompat}"
        const val resources = "androidx.appcompat:appcompat-resources:${Version.android.appCompat}"
    }

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.android.constraintLayout}"

    // Material
    const val material = "com.google.android.material:material:${Version.android.material}"

    val compose = Compose

    // see: https://developer.android.com/jetpack/compose/setup
    object Compose {
        const val ui = "androidx.compose.ui:ui:${Version.android.compose.ui}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Version.android.compose.ui}"
        const val uiManifest = "androidx.compose.ui:ui-test-manifest:${Version.android.compose.ui}"
        const val foundation = "androidx.compose.foundation:foundation:${Version.android.compose.foundation}"
        const val navigation = "androidx.navigation:navigation-compose:${Version.android.ktx.navigation}"
        const val material = "androidx.compose.material:material:${Version.android.compose.material}"
        const val materialIcons = "androidx.compose.material:material-icons-core:${Version.android.compose.material}"
        const val materialIconsExtended = "androidx.compose.material:material-icons-extended:${Version.android.compose.material}"
        const val activity = "androidx.activity:activity-compose:${Version.android.compose.activity}"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Version.android.compose.viewmodel}"
        const val constrainLayout = "androidx.constraintlayout:constraintlayout-compose:${Version.android.compose.constraintLayout}"
        const val paging = "androidx.paging:paging-compose:${Version.android.compose.paging}"
    }

    val test = Test

    object Test {
        const val core = "androidx.test:core:${Version.android.test.test}"
        const val ktx = "androidx.test:core-ktx:${Version.android.test.test}"
        const val runner = "androidx.test:runner:${Version.android.test.test}"
        const val rules = "androidx.test:rules:${Version.android.test.test}"

        const val junit = "androidx.test.ext:junit:${Version.android.test.junit}"
        const val junitKtx = "androidx.test.ext:junit-ktx:${Version.android.test.junit}"

        const val junit5 = "org.junit.jupiter:junit-jupiter-api:${Version.jvm.test.junit}"
        const val junit5Parameterized = "org.junit.jupiter:junit-jupiter-params:${Version.jvm.test.junit}"

        const val junit4 = "junit:junit:${Version.jvm.test.junit4}"
        const val junit4LegacyEngine = "org.junit.vintage:junit-vintage-engine:${Version.jvm.test.junit}"

        const val espressoCore = "androidx.test.espresso:espresso-core:${Version.android.test.espresso}"
        const val espressoIntents = "androidx.test.espresso:espresso-intents:${Version.android.test.espresso}"
        const val espressoWeb = "androidx.test.espresso:espresso-web:${Version.android.test.espresso}"

        const val composeJunit4 = "androidx.compose.ui:ui-test-junit4:${Version.android.compose.ui}"

        const val uiAutomator = "androidx.test.uiautomator:uiautomator:${Version.android.test.uiAutomator}"

        const val robolectric = "org.robolectric:robolectric:${Version.android.test.robolectric}"

        const val paging = "androidx.paging:paging-common:${Version.android.ktx.pageing}"

        val mockk = Mockk

        object Mockk {
            const val unit = "io.mockk:mockk:${Version.kotlin.test.mockk}"
            const val instrumented = "io.mockk:mockk-android:${Version.kotlin.test.mockk}"
        }
    }

    val coil = Coil

    object Coil {
        const val core = "io.coil-kt:coil:${Version.android.coil}"
        const val compose = "io.coil-kt:coil-compose:${Version.android.coil}"
    }

    val hilt = Hilt

    object Hilt {
        const val gradle = "com.google.dagger:hilt-android-gradle-plugin:${Version.google.hilt}"
        const val core = "com.google.dagger:hilt-android:${Version.google.hilt}"
        const val compiler = "com.google.dagger:hilt-compiler:${Version.google.hilt}"
        const val composeNavigation = "androidx.hilt:hilt-navigation-compose:${Version.google.hiltCompose}"
        const val test = "com.google.dagger:hilt-android-testing:${Version.google.hilt}"
    }
}
