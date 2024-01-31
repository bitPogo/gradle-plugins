/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.kotlin

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.Platform
import tech.antibytes.gradle.dependency.catalog.module.Kotlin.group

internal object StdLib {
    val common = MavenArtifact(
        group = group,
        id = "kotlin-stdlib-common",
        platform = Platform.COMMON,
    )
    val jdk = MavenArtifact(
        group = group,
        id = "kotlin-stdlib",
        platform = Platform.JVM,
    )
    val jdk7 = MavenArtifact(
        group = group,
        id = "kotlin-stdlib-jdk7",
        platform = Platform.JVM,
    )
    val jdk8 = MavenArtifact(
        group = group,
        id = "kotlin-stdlib-jdk8",
        platform = Platform.JVM,
    )
    val js = MavenArtifact(
        group = group,
        id = "kotlin-stdlib-js",
        platform = Platform.JS,
    )
    val wasm = MavenArtifact(
        group = group,
        id = "kotlin-stdlib-wasm",
        platform = Platform.WASM32,
    )
}
