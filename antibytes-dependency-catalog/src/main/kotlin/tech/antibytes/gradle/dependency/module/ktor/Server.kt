/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.ktor

import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.MavenKmpArtifact
import tech.antibytes.gradle.dependency.MavenKmpTestArtifact
import tech.antibytes.gradle.dependency.Platform
import tech.antibytes.gradle.dependency.module.Ktor.group

internal object Server {
    private val allPlatforms = listOf(
        Platform.COMMON,
        Platform.IOS_ARM32,
        Platform.IOS_ARM64,
        Platform.IOS_X64,
        Platform.IOS_SIMULATOR_ARM64,
        Platform.JVM,
        Platform.LINUX_X64,
        Platform.MACOS_ARM64,
        Platform.MACOS_X64,
        Platform.TVOS_ARM64,
        Platform.TVOS_SIMULATOR_ARM64,
        Platform.TVOS_X64,
        Platform.WATCHOS_ARM32,
        Platform.WATCHOS_ARM64,
        Platform.WATCHOS_SIMULATOR_ARM64,
        Platform.WATCHOS_X64,
        Platform.WATCHOS_X86,
    )

    val auth = MavenKmpArtifact(
        group = group,
        id = "ktor-server-auth",
        platforms = allPlatforms,
    )

    val authJwt = MavenArtifact(
        group = group,
        id = "ktor-server-auth-jwt",
    )

    val authLdap = MavenArtifact(
        group = group,
        id = "ktor-server-auth-ldap",
    )

    val autoHeadResponse = MavenKmpArtifact(
        group = group,
        id = "ktor-server-auto-head-response",
        platforms = allPlatforms,
    )

    val base = MavenKmpArtifact(
        group = group,
        id = "ktor-server",
        platforms = allPlatforms,
    )

    val cachingHeaders = MavenKmpArtifact(
        group = group,
        id = "ktor-server-caching-headers",
        platforms = allPlatforms,
    )

    val callId = MavenKmpArtifact(
        group = group,
        id = "ktor-server-caching-headers",
        platforms = allPlatforms,
    )

    val callLogging = MavenArtifact(
        group = group,
        id = "ktor-server-call-logging",
    )

    val cio = MavenKmpArtifact(
        group = group,
        id = "ktor-server-cio",
        platforms = allPlatforms,
    )

    val compression = MavenArtifact(
        group = group,
        id = "ktor-server-compression",
    )

    val conditionalHeaders = MavenKmpArtifact(
        group = group,
        id = "ktor-server-conditional-headers",
        platforms = allPlatforms,
    )

    val configYaml = MavenKmpArtifact(
        group = group,
        id = "ktor-server-config-yaml",
        platforms = allPlatforms,
    )

    val contentNegotiation = MavenKmpArtifact(
        group = group,
        id = "ktor-server-content-negotiation",
        platforms = allPlatforms,
    )

    val core = MavenKmpArtifact(
        group = group,
        id = "ktor-server-core",
        platforms = allPlatforms,
    )

    val cors = MavenKmpArtifact(
        group = group,
        id = "ktor-server-cors",
        platforms = allPlatforms,
    )

    val dataConversion = MavenKmpArtifact(
        group = group,
        id = "ktor-server-data-conversion",
        platforms = allPlatforms,
    )

    val defaultHeaders = MavenArtifact(
        group = group,
        id = "ktor-server-default-headers",
    )

    val doubleReceive = MavenKmpArtifact(
        group = group,
        id = "ktor-server-double-receive",
        platforms = allPlatforms,
    )

    val forwardedHeader = MavenKmpArtifact(
        group = group,
        id = "ktor-server-forwarded-header",
        platforms = allPlatforms,
    )

    val freemaker = MavenArtifact(
        group = group,
        id = "ktor-server-freemarker",
    )

    val hostCommon = MavenKmpArtifact(
        group = group,
        id = "ktor-server-host-common",
        platforms = allPlatforms,
    )

    val hsts = MavenKmpArtifact(
        group = group,
        id = "ktor-server-hsts",
        platforms = allPlatforms,
    )

    val htmlBuilder = MavenKmpArtifact(
        group = group,
        id = "ktor-server-html-builder",
        platforms = allPlatforms,
    )

    val httpRedirect = MavenKmpArtifact(
        group = group,
        id = "ktor-server-http-redirect",
        platforms = allPlatforms,
    )

    val jetty = MavenArtifact(
        group = group,
        id = "ktor-server-jetty",
    )

    val jite = MavenArtifact(
        group = group,
        id = "ktor-server-jite",
    )

    val locations = MavenArtifact(
        group = group,
        id = "ktor-server-locations",
    )

    val methodOverride = MavenKmpArtifact(
        group = group,
        id = "ktor-server-method-override",
        platforms = allPlatforms,
    )

    val metrics = MavenArtifact(
        group = group,
        id = "ktor-server-metrics",
    )

    val metricsMicrometer = MavenArtifact(
        group = group,
        id = "ktor-server-metrics-micrometer",
    )

    val mustache = MavenArtifact(
        group = group,
        id = "ktor-server-mustache",
    )

    val netty = MavenArtifact(
        group = group,
        id = "ktor-server-netty",
    )

    val partialContent = MavenKmpArtifact(
        group = group,
        id = "ktor-server-partial-content",
        platforms = allPlatforms,
    )

    val pebble = MavenArtifact(
        group = group,
        id = "ktor-server-pebble",
    )

    val plugins = MavenArtifact(
        group = group,
        id = "ktor-server-plugins",
    )

    val requestValidation = MavenKmpArtifact(
        group = group,
        id = "ktor-server-request-validation",
        platforms = allPlatforms,
    )

    val resources = MavenKmpArtifact(
        group = group,
        id = "ktor-server-resources",
        platforms = allPlatforms,
    )

    val servlet = MavenArtifact(
        group = group,
        id = "ktor-server-servlet",
    )

    val sessions = MavenArtifact(
        group = group,
        id = "ktor-server-sessions",
    )

    val statusPages = MavenKmpArtifact(
        group = group,
        id = "ktor-server-status-pages",
        platforms = allPlatforms,
    )

    val testHost = MavenKmpTestArtifact(
        group = group,
        id = "ktor-server-test-host",
        platforms = allPlatforms,
    )

    val thymeleaf = MavenArtifact(
        group = group,
        id = "ktor-server-thymeleaf",
    )

    val tomcat = MavenArtifact(
        group = group,
        id = "ktor-server-tomcat",
    )

    val velocity = MavenArtifact(
        group = group,
        id = "ktor-server-velocity",
    )

    val webjars = MavenArtifact(
        group = group,
        id = "ktor-server-webjars",
    )

    val websockets = MavenKmpArtifact(
        group = group,
        id = "ktor-server-websockets",
        platforms = allPlatforms,
    )
}
