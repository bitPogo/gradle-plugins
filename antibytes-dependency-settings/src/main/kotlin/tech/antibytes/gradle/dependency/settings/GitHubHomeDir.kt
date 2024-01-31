/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.settings

fun gitHubHomeDir(): String {
    val workdir = System.getenv("GITHUB") ?: ""

    return workdir.split("work")[0]
}
