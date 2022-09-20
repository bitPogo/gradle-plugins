/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

data class NpmPackage(
    val name: String,
    val version: String,
)

object JSDependencies {
    val npm = Npm

    object Npm {
        val build = Build

        object Build

        val dev = Dev

        object Dev

        val peer = Peer

        object Peer
    }
}
