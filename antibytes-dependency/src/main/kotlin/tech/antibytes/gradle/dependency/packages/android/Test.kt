/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages.android

import tech.antibytes.gradle.dependency.Version

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
