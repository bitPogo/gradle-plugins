/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.MavenArtifact

/**
 * [SLF4J](http://www.slf4j.org/)
 */
internal object SLF4J {
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
