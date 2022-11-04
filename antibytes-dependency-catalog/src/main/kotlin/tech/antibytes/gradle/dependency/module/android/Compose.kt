/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.android

import tech.antibytes.gradle.dependency.Version

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
