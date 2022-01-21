/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.test

import com.appmattus.kotlinfixture.kotlinFixture
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GradlePropertyBuilderSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given makeProperty it builds a Property with the given Value`() {
        // Given
        val value: String = fixture()

        // When
        val result: Any = GradlePropertyBuilder.makeProperty(String::class.java, value)

        // Then
        assertTrue(result is Property<*>)
        assertEquals(
            actual = result.get(),
            expected = value
        )
    }

    @Test
    fun `Given makeListProperty it builds a Property with the given Value`() {
        // Given
        val value: String = fixture()

        // When
        val result: Any = GradlePropertyBuilder.makeListProperty(String::class.java, listOf(value))

        // Then
        assertTrue(result is ListProperty<*>)
        assertEquals(
            actual = result.get(),
            expected = listOf(value)
        )
    }

    @Test
    fun `Given makeSetProperty it builds a Property with the given Value`() {
        // Given
        val value: String = fixture()

        // When
        val result: Any = GradlePropertyBuilder.makeSetProperty(String::class.java, setOf(value))

        // Then
        assertTrue(result is SetProperty<*>)
        assertEquals(
            actual = result.get(),
            expected = setOf(value)
        )
    }
}
