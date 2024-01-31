/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class CapitalizerSpec {
    @Test
    fun `Given capitalize is called it does nothing if the first character is not letter`() {
        // Given
        val input = "1test"

        // When
        val actual = input.capitalize()

        // Then
        assertEquals(
            expected = input,
            actual = actual,
        )
    }

    @Test
    fun `Given capitalize is called it uppercases only the first character is a letter`() {
        // Given
        val input = "test"

        // When
        val actual = input.capitalize()

        // Then
        assertEquals(
            expected = "Test",
            actual = actual,
        )
    }

    @Test
    fun `Given decapitalize is called it does nothing if the first character is not letter`() {
        // Given
        val input = "1test"

        // When
        val actual = input.decapitalize()

        // Then
        assertEquals(
            expected = input,
            actual = actual,
        )
    }

    @Test
    fun `Given decapitalize is called it lowercases only the first character is a letter`() {
        // Given
        val input = "Test"

        // When
        val actual = input.decapitalize()

        // Then
        assertEquals(
            expected = "test",
            actual = actual,
        )
    }
}
