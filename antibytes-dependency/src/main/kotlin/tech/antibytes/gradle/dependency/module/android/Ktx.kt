/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.android

import tech.antibytes.gradle.dependency.Version

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
    const val viewmodelCoroutine = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.android.ktx.viewmodel}"
    const val workmanager = "androidx.work:work-runtime-ktx:${Version.android.ktx.workmanager}"
}
