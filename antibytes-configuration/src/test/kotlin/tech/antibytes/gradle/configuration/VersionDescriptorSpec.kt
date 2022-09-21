/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VersionDescriptorSpec {
    @Test
    fun `Given a VersionDescriptor is initialized with a String it extracts all version parts`() {
        // Given
        val serializedVersion = "12.13.14"

        // When
        val version = VersionDescriptor(serializedVersion)

        // Then
        assertEquals(
            expected = 12,
            actual = version.major
        )

        assertEquals(
            expected = 13,
            actual = version.minor
        )

        assertEquals(
            expected = 14,
            actual = version.patch
        )
    }

    @Test
    fun `Given a VersionDescriptor is initialized with a String it extracts major and minor version parts`() {
        // Given
        val serializedVersion = "12.13"

        // When
        val version = VersionDescriptor(serializedVersion)

        // Then
        assertEquals(
            expected = 12,
            actual = version.major
        )

        assertEquals(
            expected = 13,
            actual = version.minor
        )

        assertEquals(
            expected = 0,
            actual = version.patch
        )
    }

    @Test
    fun `Given a VersionDescriptor is initialized with a String it extracts major version parts`() {
        // Given
        val serializedVersion = "12"

        // When
        val version = VersionDescriptor(serializedVersion)

        // Then
        assertEquals(
            expected = 12,
            actual = version.major
        )

        assertEquals(
            expected = 0,
            actual = version.minor
        )

        assertEquals(
            expected = 0,
            actual = version.patch
        )
    }

    @Test
    fun `Given toString is called it serializes`() {
        // Given
        val version = VersionDescriptor(12, 13, 14)

        // When
        val serialized = version.toString()

        // Then
        assertEquals(
            actual = serialized,
            expected = "12.13.14"
        )
    }
}
