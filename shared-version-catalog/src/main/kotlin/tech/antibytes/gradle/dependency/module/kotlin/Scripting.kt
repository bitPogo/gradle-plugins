/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.kotlin

import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.Platform
import tech.antibytes.gradle.dependency.module.Kotlin

internal object Scripting {
    val core = MavenArtifact(
        group = Kotlin.group,
        id = "kotlin-scripting",
        platform = Platform.JVM,
    )
    val common = MavenArtifact(
        group = Kotlin.group,
        id = "kotlin-scripting-common",
        platform = Platform.JVM,
    )
    val jsr223 = MavenArtifact(
        group = Kotlin.group,
        id = "kotlin-scripting-jsr223",
        platform = Platform.JVM,
    )
    val jsr223Unshaded = MavenArtifact(
        group = Kotlin.group,
        id = "kotlin-scripting-jsr223-unshaded",
        platform = Platform.JVM,
    )
    val jvm = MavenArtifact(
        group = Kotlin.group,
        id = "kotlin-scripting-jvm",
        platform = Platform.JVM,
    )
    val jvmHostUnshaded = MavenArtifact(
        group = Kotlin.group,
        id = "kotlin-scripting-jvm-host-unshaded",
        platform = Platform.JVM,
    )
    val dependencies = MavenArtifact(
        group = Kotlin.group,
        id = "kotlin-scripting-dependencies",
        platform = Platform.JVM,
    )
    val dependenciesMaven = MavenArtifact(
        group = Kotlin.group,
        id = "kotlin-scripting-dependencies-maven",
        platform = Platform.JVM,
    )
}
