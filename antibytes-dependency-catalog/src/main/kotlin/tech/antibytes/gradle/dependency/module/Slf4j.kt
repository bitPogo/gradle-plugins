/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module

import tech.antibytes.gradle.dependency.MavenArtifact

/**
 * [SLF4J](http://www.slf4j.org/)
 */
internal object Slf4j {
    private const val group = "org.slf4j"

    val api = MavenArtifact(
        group = group,
        id = "slf4j-api",
    )
    val noop = MavenArtifact(
        group = group,
        id = "slf4j-nop",
    )
}
