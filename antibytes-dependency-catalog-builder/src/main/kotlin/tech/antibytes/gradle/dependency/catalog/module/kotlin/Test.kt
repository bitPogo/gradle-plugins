/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.kotlin

import tech.antibytes.gradle.dependency.catalog.MavenTestArtifact
import tech.antibytes.gradle.dependency.catalog.Platform
import tech.antibytes.gradle.dependency.catalog.module.Kotlin

internal object Test {
    val annotations = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-annotations-common",
        platform = Platform.COMMON,
    )

    val core = Core
    internal object Core {
        val common = MavenTestArtifact(
            group = Kotlin.group,
            id = "kotlin-test-common",
            platform = Platform.COMMON,
        )
        val jvm = MavenTestArtifact(
            group = Kotlin.group,
            id = "kotlin-test",
            platform = Platform.JVM,
        )
        val js = MavenTestArtifact(
            group = Kotlin.group,
            id = "kotlin-test-js",
            platform = Platform.JS,
        )
        val wasm = MavenTestArtifact(
            group = Kotlin.group,
            id = "kotlin-test-wasm",
            platform = Platform.WASM32,
        )
    }
    val junit4 = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-junit",
        platform = Platform.JVM,
    )
    val junit5 = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-junit5",
        platform = Platform.JVM,
    )
    val testng = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-testng",
        platform = Platform.JVM,
    )
}
