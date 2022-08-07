/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
package tech.antibytes.gradle.test

import groovy.lang.Closure
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class ClosureHelperSpec {
    @Test
    fun `Given createClosure is called with a Value it creates a Closure`() {
        // Given
        val value = Any()

        // When
        val closure: Any = createClosure(value)

        // Then
        assertTrue(closure is Closure<*>)
    }

    @Test
    fun `Given doCall is called with something it returns the delegated value`() {
        // Given
        val value = Any()

        // When
        val closure = createClosure(value)
        val actual = closure.call(Any())

        // Then
        assertSame(
            actual = actual,
            expected = value,
        )
    }
}
