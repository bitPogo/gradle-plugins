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
    fun `It contains Js Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("js-nodeJs", any<String>())
        }
    }

    @Test
    fun `It contains Jvm Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("jvm-test-junit", any<String>())
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
    fun `It contains KotlinX Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlinx-coroutines", any<String>())
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
}
