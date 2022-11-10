/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class DependencyCatalogSpec {
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
            catalog.library("mkDocs-includeMarkdown", "python", "mkdocs-include-markdown-plugin")
        }

        verify(exactly = 1) {
            module.versionRef("mkDocs-includeMarkdown")
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
            catalog.library("node-production-axios", "node-production", "axios")
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
            catalog.library("node-development-copyWebpackPlugin", "node-development", "copy-webpack-plugin")
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
                "jvm-test-kotlin",
                "org.jetbrains.kotlin",
                "kotlin-test",
            )
        }

        verify(exactly = 1) {
            module.versionRef("kotlin-test-jvm")
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
}
