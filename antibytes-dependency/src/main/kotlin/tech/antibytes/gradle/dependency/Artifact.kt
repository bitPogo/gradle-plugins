/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

internal interface Artifact {
    val id: String
}

internal data class MavenArtifact(
    val group: String,
    override val id: String,
) : Artifact

internal data class NpmArtifact(
    override val id: String
) : Artifact
