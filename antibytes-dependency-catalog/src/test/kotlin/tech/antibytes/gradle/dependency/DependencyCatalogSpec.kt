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
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library("mkDocs-includeMarkdown", "python", "mkdocs-include-markdown-plugin")
        }

        verify(exactly = 1) {
            module.version("mkDocs-includeMarkdown")
        }
    }

    @Test
    fun `It contains Node ProductionDependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library("node-production-axios", "node-production", "axios")
        }

        verify(exactly = 1) {
            module.version("node-axios")
        }
    }

    @Test
    fun `It contains Node DevelopmentDependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library("node-development-copyWebpackPlugin", "node-development", "copy-webpack-plugin")
        }

        verify(exactly = 1) {
            module.version("node-copyWebpackPlugin")
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
    fun `It contains Kotlinx Coroutines KMP Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs

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
            module.version("kotlinx-coroutines-core")
        }
    }

    @Test
    fun `It contains Kotlinx Coroutines BOM Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "bom-kotlinx-coroutines",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-bom",
            )
        }

        verify(atLeast = 1) {
            module.withoutVersion()
        }
    }

    @Test
    fun `It contains Kotlinx Coroutines Maven Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs

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
            module.version("kotlinx-coroutines-android")
        }
    }

    @Test
    fun `It contains Kotlinx Serialisation BOM Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library(
                "bom-kotlinx-serialization",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-bom",
            )
        }

        verify(atLeast = 1) {
            module.withoutVersion()
        }
    }

    @Test
    fun `It contains Kotlinx Serialisation Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs

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
            module.version("kotlinx-serialization-core")
        }
    }

    @Test
    fun `It contains Ktor Client Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs

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
            module.version("ktor-client-core")
        }
    }

    @Test
    fun `It contains Ktor Serialisation Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs
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
            module.version("ktor-serialization-core")
        }
    }

    @Test
    fun `It contains Ktor Server Dependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs
        every { module.withoutVersion() } just Runs
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
            module.version("ktor-server-base")
        }
    }
}
