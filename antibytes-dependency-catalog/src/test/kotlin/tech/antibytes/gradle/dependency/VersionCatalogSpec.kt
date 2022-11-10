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
    fun `It contains Kotlin Stdlib Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-stdlib-common", any<String>())
        }
    }

    @Test
    fun `It contains Kotlin Test Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-test-annotations", any<String>())
        }
    }

    @Test
    fun `It contains Kotlin Scripting Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-scripting-core", any<String>())
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
            catalog.version("kotlin-kotlin-plugin", any<String>())
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
    fun `It contains Squares Okio Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-okio-core", any<String>())
        }
    }

    @Test
    fun `It contains Squares SqlDelight Driver Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-sqldelight-driver-android", any<String>())
        }
    }

    @Test
    fun `It contains Squares SqlDelight Coroutine Extension Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-sqldelight-coroutines", any<String>())
        }
    }

    @Test
    fun `It contains Squares OkHttp Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-okhttp-bom", any<String>())
        }
    }

    @Test
    fun `It contains Squares OkHttp Mockserver Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-okhttp-mockserver-core", any<String>())
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
    fun `It contains Stately Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("stately-collections", any<String>())
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

    @Test
    fun `It contains Gradle Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("gradle-versioning-dependency", any<String>())
        }
    }
}
