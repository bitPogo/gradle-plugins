/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages.android

import tech.antibytes.gradle.dependency.Version

object Coil {
    const val core = "io.coil-kt:coil:${Version.android.coil}"
    const val compose = "io.coil-kt:coil-compose:${Version.android.coil}"
}
