/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.test

import com.appmattus.kotlinfixture.kotlinFixture
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.junit.jupiter.api.Test

class GradlePropertyFactorySpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given makeProperty it builds a Property with the given Value`() {
        // Given
        val value: String = fixture()

        // When
        val result: Any = GradlePropertyFactory.makeProperty(String::class.java, value)

        // Then
        assertTrue(result is Property<*>)
        assertEquals(
            actual = result.get(),
            expected = value,
        )
    }

    @Test
    fun `Given makeListProperty it builds a Property with the given Value`() {
        // Given
        val value: String = fixture()

        // When
        val result: Any = GradlePropertyFactory.makeListProperty(String::class.java, listOf(value))

        // Then
        assertTrue(result is ListProperty<*>)
        assertEquals(
            actual = result.get(),
            expected = listOf(value),
        )
    }

    @Test
    fun `Given makeSetProperty it builds a Property with the given Value`() {
        // Given
        val value: String = fixture()

        // When
        val result: Any = GradlePropertyFactory.makeSetProperty(String::class.java, setOf(value))

        // Then
        assertTrue(result is SetProperty<*>)
        assertEquals(
            actual = result.get(),
            expected = setOf(value),
        )
    }

    @Test
    fun `Given makeMapProperty it builds a Property with the given Value`() {
        // Given
        val value: Map<String, String> = fixture()

        // When
        val result: Any = GradlePropertyFactory.makeMapProperty(String::class.java, String::class.java, value)

        // Then
        assertTrue(result is MapProperty<*, *>)
        assertEquals(
            actual = result.get(),
            expected = value,
        )
    }
}
