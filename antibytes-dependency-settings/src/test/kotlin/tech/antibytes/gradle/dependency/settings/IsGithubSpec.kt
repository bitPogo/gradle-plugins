/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.settings

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.ClearEnvironmentVariable
import org.junitpioneer.jupiter.SetEnvironmentVariable

class IsGithubSpec {
    @Test
    @ClearEnvironmentVariable(key = "GITHUB")
    fun `Given isGitHub is called it returns false if the Github Environment Variable is not present`() {
        // When
        val actual = isGitHub()

        // Then
        assertFalse(actual)
    }

    @Test
    @SetEnvironmentVariable(key = "GITHUB", value = "any")
    fun `Given isGitHub is called it returns true if the Github Environment Variable is present`() {
        // When
        val actual = isGitHub()

        // Then
        assertTrue(actual)
    }
}
