/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.dependency

object Version {
    const val kotlin = "1.5.31"
    const val android = "7.0.3"

    val gradle = GradlePlugin
    val test = Test

    object GradlePlugin {
        const val dependencyUpdate = "0.39.0"
        const val jacoco = "0.8.7"
        const val publishing = "5.13.0.202109080827-r"
        const val versioning = "0.12.2" // see: https://github.com/palantir/gradle-git-version/issues/353
    }

    object Test {
        const val jacoco = "0.8.7"
        const val junit = "5.8.1"
        const val mockk = "1.12.1"
        const val fixture = "1.2.0"
    }
}
