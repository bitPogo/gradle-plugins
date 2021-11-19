/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.provider.Property
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateExtensionSpec {
    @Test
    fun `Given createExtension is called it creates an Extension`() {
        // When
        val extension: Any = createExtension<TestExtension>()

        // Then
        assertTrue(extension is TestExtension)
        assertEquals(
            actual = extension.test.get(),
            expected = "test"
        )
    }

    @Test
    fun `Given createExtension is called with arguments it creates an Extension`() {
        // Given
        val arg = "test"

        // When
        val extension: Any = createExtension<TestExtensionWithArgs>(arg)

        // Then
        assertTrue(extension is TestExtensionWithArgs)
        assertEquals(
            actual = extension.test.get(),
            expected = arg
        )
    }
}

private abstract class TestExtension {
    abstract val test: Property<String>

    init {
        test.convention("test")
    }
}

private abstract class TestExtensionWithArgs(arg: String) {
    abstract val test: Property<String>

    init {
        test.convention(arg)
    }
}
