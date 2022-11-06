/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.junit.jupiter.api.Test

class VersionCatalogSpec {
    @Test
    fun `It contains Android Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-ktx-core", any<String>())
        }
    }

    @Test
    fun `It contains Kotlin Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-stdlib", any<String>())
        }
    }

    @Test
    fun `It contains Kotlinx Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlinx-coroutines-core", any<String>())
        }
    }

    @Test
    fun `It contains MkDocs Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("mkDocs-includeMarkdown", any<String>())
        }
    }

    @Test
    fun `It contains Node Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("node-axios", any<String>())
        }
    }

    @Test
    fun `It contains Square Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-sqldelight", any<String>())
        }
    }

    @Test
    fun `It contains Ktor Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("ktor-events", any<String>())
        }
    }

    @Test
    fun `It contains Ktor Client Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("ktor-client-core", any<String>())
        }
    }

    @Test
    fun `It contains Ktor Serialisation Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("ktor-serialization-core", any<String>())
        }
    }

    @Test
    fun `It contains Ktor Server Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("ktor-server-core", any<String>())
        }
    }

    @Test
    fun `It contains SLF4J Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("slf4j-noop", any<String>())
        }
    }

    @Test
    fun `It contains Koin Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("koin-core", any<String>())
        }
    }

    @Test
    fun `It contains Vendor Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("uuid", any<String>())
        }
    }
}
