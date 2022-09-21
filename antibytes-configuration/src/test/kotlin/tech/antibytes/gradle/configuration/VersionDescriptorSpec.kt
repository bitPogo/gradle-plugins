/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    @Test
    fun `Given 2 VersionDescriptors they are equal if all version parts are equal`() {
        // Given
        val version = VersionDescriptor(12, 13, 14)

        // When
        val equal = version == version

        // Then
        assertTrue(equal)
    }

    @Test
    fun `Given 2 VersionDescriptors a version is greater if patch is greater`() {
        // Given
        val version1 = VersionDescriptor(12, 13, 14)
        val version2 = VersionDescriptor(12, 13, 15)

        // When
        val greater = version2 > version1

        // Then
        assertTrue(greater)
    }

    @Test
    fun `Given 2 VersionDescriptors a version is smaller if patch is smaller`() {
        // Given
        val version1 = VersionDescriptor(12, 13, 14)
        val version2 = VersionDescriptor(12, 13, 15)

        // When
        val smaller = version1 < version2

        // Then
        assertTrue(smaller)
    }

    @Test
    fun `Given 2 VersionDescriptors a version is greater if minor is greater`() {
        // Given
        val version1 = VersionDescriptor(12, 13, 14)
        val version2 = VersionDescriptor(12, 14, 14)

        // When
        val greater = version2 > version1

        // Then
        assertTrue(greater)
    }

    @Test
    fun `Given 2 VersionDescriptors a version is smaller if minor is smaller`() {
        // Given
        val version1 = VersionDescriptor(12, 13, 14)
        val version2 = VersionDescriptor(12, 14, 14)

        // When
        val smaller = version1 < version2

        // Then
        assertTrue(smaller)
    }

    @Test
    fun `Given 2 VersionDescriptors a version is greater if major is greater`() {
        // Given
        val version1 = VersionDescriptor(12, 13, 14)
        val version2 = VersionDescriptor(13, 13, 14)

        // When
        val greater = version2 > version1

        // Then
        assertTrue(greater)
    }

    @Test
    fun `Given 2 VersionDescriptors a version is smaller if major is smaller`() {
        // Given
        val version1 = VersionDescriptor(12, 13, 14)
        val version2 = VersionDescriptor(13, 13, 14)

        // When
        val smaller = version1 < version2

        // Then
        assertTrue(smaller)
    }
}
