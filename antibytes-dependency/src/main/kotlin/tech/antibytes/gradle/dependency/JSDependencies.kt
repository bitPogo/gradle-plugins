/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

object JSDependencies {
    val gradle = mapOf(
        "org.jetbrains.kotlinx:kotlinx-nodejs" to "0.0.7"
    )

    val npm = Npm
    object Npm {
        val build = Build
        object Build {

        }

        val dev = Dev
        object Dev {

        }

        val peer = Peer
        object Peer {

        }
    }
}
