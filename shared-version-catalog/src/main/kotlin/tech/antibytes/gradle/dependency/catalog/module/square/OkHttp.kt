/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.square

import tech.antibytes.gradle.dependency.catalog.MavenArtifact

internal object OkHttp {
    private const val group = "com.squareup.okhttp3"
    val core = MavenArtifact(
        group = group,
        id = "okhttp",
    )

    /*val coroutines = MavenKmpArtifact(
        group = group,
        id = "okhttp-coroutines",
        platforms = listOf(
            Platform.COMMON,
            // Platform.ANDROID,
            Platform.JVM,
            Platform.JS
        )
    )*/
    val bom = MavenArtifact(
        group = group,
        id = "okhttp-bom",
    )
    val brotli = MavenArtifact(
        group = group,
        id = "okhttp-brotli",
    )
    val dnsOverHttps = MavenArtifact(
        group = group,
        id = "okhttp-dnsoverhttps",
    )
    val logger = MavenArtifact(
        group = group,
        id = "okhttp-logging-interceptor",
    )
    val tls = MavenArtifact(
        group = group,
        id = "okhttp-tls",
    )
    val urlConnection = MavenArtifact(
        group = group,
        id = "okhttp-urlconnection",
    )
    val curl = MavenArtifact(
        group = group,
        id = "okcurl",
    )
    val sse = MavenArtifact(
        group = group,
        id = "okhttp-sse",
    )

    val mockserver = Mockserver

    internal object Mockserver {
        val core = MavenArtifact(
            group = group,
            id = "okhttp-mockserver",
        )
        /* val junit4 = MavenArtifact(
            group = group,
            id = "mockwebserver3-junit4",
        )
        val junit5 = MavenArtifact(
            group = group,
            id = "mockwebserver3-junit5",
        )*/
    }
}
