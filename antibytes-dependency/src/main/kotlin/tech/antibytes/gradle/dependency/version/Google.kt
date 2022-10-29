/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

internal object Google {
    val hilt = Hilt

    object Hilt {
        /**
         * [Google Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
         */
        const val core = "2.43.2"

        /**
         * [Google Hilt Compose](https://developer.android.com/jetpack/androidx/releases/hilt)
         */
        const val compose = "1.0.0"
    }
}
