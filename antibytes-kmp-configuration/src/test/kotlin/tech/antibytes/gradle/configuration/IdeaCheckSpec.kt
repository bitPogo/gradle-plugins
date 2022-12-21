/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import java.util.Properties
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class IdeaCheckSpec {
    @Test
    fun `Given isIdea is called it returns false if the there is no idea error notification`() {
        // When
        val isIdea = isIdea()

        // Then
        assertFalse(isIdea)
    }

    @Test
    fun `Given isIdea is called it returns true if the there is a idea error notification`() {
        // Given
        val properties = Properties()
        properties.setProperty("idea.fatal.error.notification", "something")
        System.setProperties(properties)

        // When
        val isIdea = isIdea()

        // Then
        assertTrue(isIdea)

        System.clearProperty("idea.fatal.error.notification")
    }
}
