/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog

import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class DependencyCatalogSpec {
    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `It contains PythonDependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library("python-mkdocs-includeMarkdown", "python", "mkdocs-include-markdown-plugin")
        }

        verify(exactly = 1) {
            module.versionRef("python-mkdocs-includeMarkdown")
        }
    }

    @Test
    fun `It contains Node ProductionDependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library("node-axios", "node-production", "axios")
        }

        verify(exactly = 1) {
            module.versionRef("node-axios")
        }
    }

    @Test
    fun `It contains Node DevelopmentDependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library("node-copyWebpackPlugin", "node-development", "copy-webpack-plugin")
        }

        verify(exactly = 1) {
            module.versionRef("node-copyWebpackPlugin")
        }
    }

    @Test
    @Disabled
    fun `It contains Node PeerDependencies`() {
        // Given

        // When

        // Then
    }

    @Test
    @Disabled
    fun `It contains Node OptionalDependencies`() {
        // Given

        // When

        // Then
    }

    @Test
    fun `It contains Koin Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-koin-core",
                "io.insert-koin",
                "koin-core",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-koin-core",
                "io.insert-koin",
                "koin-core-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("koin-core")
        }
    }

    @Test
    fun `It contains Koin Test Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-test-koin-junit5",
                "io.insert-koin",
                "koin-test-junit5",
            )
        }

        verify(exactly = 1) {
            module.versionRef("koin-junit5")
        }
    }

    @Test
    fun `It contains Kotlinx Coroutines KMP Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-kotlinx-coroutines-core",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-core",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-kotlinx-coroutines-core",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-core-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("kotlinx-coroutines-core")
        }
    }

    @Test
    fun `It contains Kotlinx Coroutines KMP Test Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-test-kotlinx-coroutines",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-test",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-test-kotlinx-coroutines",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-test-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("kotlinx-coroutines-test")
        }
    }

    @Test
    fun `It contains Kotlinx Coroutines BOM Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-kotlinx-coroutines-bom",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-bom",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("kotlinx-coroutines-bom")
        }
    }

    @Test
    fun `It contains Kotlinx Coroutines Maven Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-kotlinx-coroutines",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-android",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("kotlinx-coroutines-android")
        }
    }

    @Test
    fun `It contains Kotlinx AtomicFu Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-kotlinx-atomicfu-core",
                "org.jetbrains.kotlinx",
                "atomicfu",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-kotlinx-atomicfu-core",
                "org.jetbrains.kotlinx",
                "atomicfu-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("kotlinx-atomicfu-core")
        }
    }

    @Test
    fun `It contains Kotlinx Serialisation BOM Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-kotlinx-serialization-bom",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-bom",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("kotlinx-serialization-bom")
        }
    }

    @Test
    fun `It contains Kotlinx Serialisation Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-kotlinx-serialization-core",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-core",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-kotlinx-serialization-core",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-core-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("kotlinx-serialization-core")
        }
    }

    @Test
    fun `It contains Ktor Client Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-ktor-client-core",
                "io.ktor",
                "ktor-client-core",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-ktor-client-core",
                "io.ktor",
                "ktor-client-core-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("ktor-client-core")
        }
    }

    @Test
    fun `It contains Ktor Serialisation Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-ktor-serialization-core",
                "io.ktor",
                "ktor-serialization-kotlinx",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-ktor-serialization-core",
                "io.ktor",
                "ktor-serialization-kotlinx-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("ktor-serialization-core")
        }
    }

    @Test
    fun `It contains Ktor Server Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-ktor-server-base",
                "io.ktor",
                "ktor-server",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "jvm-ktor-server-base",
                "io.ktor",
                "ktor-server-jvm",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("ktor-server-base")
        }
    }

    @Test
    fun `It contains SLF4J Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-slf4j-noop",
                "org.slf4j",
                "slf4j-nop",
            )
        }

        verify(exactly = 1) {
            module.versionRef("slf4j-noop")
        }
    }

    @Test
    fun `It contains Stately Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-stately-collections",
                "co.touchlab",
                "stately-iso-collections",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-stately-collections",
                "co.touchlab",
                "stately-iso-collections-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("stately-collections")
        }
    }

    @Test
    fun `It contains Square Okio Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-square-okio-bom",
                "com.squareup.okio",
                "okio-bom",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-square-okio-core",
                "com.squareup.okio",
                "okio-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("square-okio-core")
        }
    }

    @Test
    fun `It contains Square SqlDelight Driver Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-square-sqldelight-driver-native",
                "com.squareup.sqldelight",
                "native-driver",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "iosarm64-square-sqldelight-driver-native",
                "com.squareup.sqldelight",
                "native-driver-iosarm64",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("square-sqldelight-driver-native")
        }
    }

    @Test
    fun `It contains Square SqlDelight Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-square-sqldelight-coroutines",
                "com.squareup.sqldelight",
                "coroutines-extensions",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "js-square-sqldelight-coroutines",
                "com.squareup.sqldelight",
                "coroutines-extensions-js",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("square-sqldelight-coroutines")
        }
    }

    @Test
    fun `It contains Square the SqlDelight Gradle Plugin`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.plugin(
                "square-sqldelight",
                "com.squareup.sqldelight",
            )
        }

        verify(exactly = 1) {
            plugin.versionRef("square-sqldelight-plugin")
        }
    }

    @Test
    fun `It contains Square the SqlDelight OkHttp`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-square-okhttp-core",
                "com.squareup.okhttp3",
                "okhttp",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("square-okhttp-core")
        }
    }

    @Test
    fun `It contains Square the SqlDelight OkHttp Mockserver`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-square-okhttp-mockserver-core",
                "com.squareup.okhttp3",
                "okhttp-mockserver",
            )
        }

        verify(exactly = 1) {
            module.versionRef("square-okhttp-mockserver-core")
        }
    }

    @Test
    fun `It contains Square KotlinPoet`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-square-kotlinPoet-ksp",
                "com.squareup",
                "kotlinpoet-ksp",
            )
        }

        verify(exactly = 1) {
            module.versionRef("square-kotlinPoet-ksp")
        }
    }

    @Test
    fun `It contains Vendor Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns mockk(relaxed = true)
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-uuid",
                "com.benasher44",
                "uuid",
            )
        }
        verify(exactly = 1) {
            catalog.library(
                "jvm-uuid",
                "com.benasher44",
                "uuid-jvm",
            )
        }

        verify(atLeast = 2) {
            module.versionRef("uuid")
        }
    }

    @Test
    fun `It contains Gradle`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "gradle-versioning",
                "com.palantir.gradle.gitversion",
                "gradle-git-version",
            )
        }

        verify(exactly = 1) {
            module.versionRef("gradle-versioning-dependency")
        }
    }

    @Test
    fun `It contains Kotlin StdLib`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-kotlin-stdlib",
                "org.jetbrains.kotlin",
                "kotlin-stdlib-common",
            )
        }

        verify(exactly = 1) {
            module.versionRef("kotlin-stdlib-common")
        }
    }

    @Test
    fun `It contains Kotlin Script`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin
        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-kotlin-scripting-jsr223",
                "org.jetbrains.kotlin",
                "kotlin-scripting-jsr223",
            )
        }

        verify(exactly = 1) {
            module.versionRef("kotlin-scripting-jsr223")
        }
    }

    @Test
    fun `It contains Kotlin Test`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-test-kotlin-core",
                "org.jetbrains.kotlin",
                "kotlin-test",
            )
        }

        verify(exactly = 1) {
            module.versionRef("kotlin-test-core-jvm")
        }
    }

    @Test
    fun `It contains Kotlin Test Tools for WASM`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "wasm32-test-kotlin-core-wasm",
                "org.jetbrains.kotlin",
                "kotlin-test-wasm",
            )
        }

        verify(exactly = 1) {
            module.versionRef("kotlin-test-core-wasm")
        }
    }

    @Test
    fun `It contains Kotlin`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-kotlin-bom",
                "org.jetbrains.kotlin",
                "kotlin-bom",
            )
        }

        verify(exactly = 1) {
            module.versionRef("kotlin-bom")
        }
    }

    @Test
    fun `It contains Android`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "gradle-agp",
                "com.android.tools.build",
                "gradle",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-agp")
        }
    }

    @Test
    fun `It contains Android KTX`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-ktx-core",
                "androidx.core",
                "core-ktx",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-ktx-core")
        }
    }

    @Test
    fun `It contains Android Ktx Activity`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-ktx-activity-core",
                "androidx.activity",
                "activity-ktx",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-ktx-activity-core")
        }
    }

    @Test
    fun `It contains Android KTX Navigation`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-ktx-navigation-ui",
                "androidx.navigation",
                "navigation-ui-ktx",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-ktx-navigation-ui")
        }
    }

    @Test
    fun `It contains Android KTX Paging`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-ktx-paging-compose",
                "androidx.paging",
                "paging-compose",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-ktx-paging-compose")
        }
    }

    @Test
    fun `It contains Android KTX Viewmodel`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-ktx-viewmodel-core",
                "androidx.lifecycle",
                "lifecycle-viewmodel-ktx",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-ktx-viewmodel-core")
        }
    }

    @Test
    fun `It contains Android AppCompact`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-appCompact-core",
                "androidx.appcompat",
                "appcompat",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-appCompact-core")
        }
    }

    @Test
    fun `It contains Android Coil`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-coil-core",
                "io.coil-kt",
                "coil",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-coil-core")
        }
    }

    @Test
    fun `It contains Android ConstraintLayout`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-constraintLayout-compose",
                "androidx.constraintlayout",
                "constraintlayout-compose",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-constraintLayout-compose")
        }
    }

    @Test
    fun `It contains Android Material`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-material-core",
                "com.google.android.material",
                "material",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-material-core")
        }
    }

    @Test
    fun `It contains Android Material Compose`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-material-compose-core",
                "androidx.compose.material",
                "material",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-material-compose-core")
        }
    }

    @Test
    fun `It contains Android Material3`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-material3-core",
                "androidx.compose.material3",
                "material3",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-material3-core")
        }
    }

    @Test
    fun `It contains Android Hilt`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-hilt-compose",
                "com.google.dagger",
                "hilt-navigation-compose",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-hilt-compose")
        }
    }

    @Test
    fun `It contains Android Compose`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-compose-bom",
                "androidx.compose",
                "compose-bom",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-compose-bom")
        }
    }

    @Test
    fun `It contains Android Compose Animations`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-compose-animation-graphics",
                "androidx.compose.animation",
                "animation-graphics",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-compose-animation-graphics")
        }
    }

    @Test
    fun `It contains Android Compose Foundation`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-compose-foundation-layout",
                "androidx.compose.foundation",
                "foundation-layout",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-compose-foundation-layout")
        }
    }

    @Test
    fun `It contains Android Compose UI`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-compose-ui-geometry",
                "androidx.compose.ui",
                "ui-geometry",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-compose-ui-geometry")
        }
    }

    @Test
    fun `It contains Android Compose UI Tooling`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-compose-ui-tooling-preview",
                "androidx.compose.ui",
                "ui-tooling-preview",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-compose-ui-tooling-preview")
        }
    }

    @Test
    fun `It contains Android Test Compose`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-test-compose-manifest",
                "androidx.compose.ui",
                "ui-test-manifest",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-test-compose-manifest")
        }
    }

    @Test
    fun `It contains Android Test Espresso`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-test-espresso-web",
                "androidx.test.espresso",
                "espresso-web",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-test-espresso-web")
        }
    }

    @Test
    fun `It contains Android Test JUnit`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-test-junit-ktx",
                "androidx.test.ext",
                "junit-ktx",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-test-junit-ktx")
        }
    }

    @Test
    fun `It contains Android Test`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "android-test-ktx",
                "androidx.test",
                "core-ktx",
            )
        }

        verify(exactly = 1) {
            module.versionRef("android-test-ktx")
        }
    }

    @Test
    fun `It contains Vendor Test`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "common-test-mockk",
                "io.mockk",
                "mockk",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("test-mockk")
        }
    }

    @Test
    fun `It contains Vendor Test JUnit`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-test-junit-parameterized",
                "org.junit.jupiter",
                "junit-jupiter-params",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("test-junit-parameterized")
        }
    }

    @Test
    fun `It contains Vendor Test CompilerTest`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "jvm-test-compiler-ksp",
                "com.github.tschuchortdev",
                "kotlin-compile-testing-ksp",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("test-compiler-ksp")
        }
    }

    @Test
    fun `It contains Antibytes Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "gradle-test-antibytes-testUtils",
                "tech.antibytes.gradle",
                "antibytes-gradle-test-utils",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("gradle-antibytes-testUtils")
        }
    }

    @Test
    fun `It contains KSP Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "gradle-ksp-plugin",
                "com.google.devtools.ksp",
                "symbol-processing-gradle-plugin",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("gradle-ksp-plugin-dependency")
        }
    }

    @Test
    fun `It contains Wrapper Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "js-kotlin-wrappers-popper",
                "org.jetbrains.kotlin-wrappers",
                "kotlin-popper",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("kotlin-wrappers-popper")
        }
    }

    @Test
    fun `It contains Wrappers react Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "js-kotlin-wrappers-react-popper",
                "org.jetbrains.kotlin-wrappers",
                "kotlin-react-popper",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("kotlin-wrappers-react-popper")
        }
    }

    @Test
    fun `It contains Wrappers tanstack Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "js-kotlin-wrappers-tanstack-queryCore",
                "org.jetbrains.kotlin-wrappers",
                "kotlin-tanstack-query-core",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("kotlin-wrappers-tanstack-queryCore")
        }
    }

    @Test
    fun `It contains Wrappers tanstack react Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "js-kotlin-wrappers-tanstack-react-query",
                "org.jetbrains.kotlin-wrappers",
                "kotlin-tanstack-react-query",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("kotlin-wrappers-tanstack-react-query")
        }
    }

    @Test
    fun `It contains Jetbrains Compose`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val plugin: VersionCatalogBuilder.PluginAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { catalog.plugin(any(), any()) } returns plugin

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "gradle-jetbrains-compose",
                "org.jetbrains.compose",
                "compose-gradle-plugin",
            )
        }

        verify(atLeast = 1) {
            module.versionRef("jetbrains-compose-dependency")
        }
    }
}
