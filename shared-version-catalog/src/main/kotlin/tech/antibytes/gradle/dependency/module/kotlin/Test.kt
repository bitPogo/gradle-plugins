/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.kotlin

import tech.antibytes.gradle.dependency.MavenTestArtifact
import tech.antibytes.gradle.dependency.Platform
import tech.antibytes.gradle.dependency.module.Kotlin

internal object Test {
    val annotations = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-annotations",
        platform = Platform.JVM,
    )
    val annotationsCommon = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-annotations-common",
        platform = Platform.COMMON,
    )
    val jvm = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test",
        platform = Platform.JVM,
    )
    val common = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-common",
        platform = Platform.COMMON,
    )
    val js = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-js",
        platform = Platform.JS,
    )
    val junit4 = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-junit4",
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
    val wasm = MavenTestArtifact(
        group = Kotlin.group,
        id = "kotlin-test-wasm",
        platform = Platform.WASM32,
    )
}
