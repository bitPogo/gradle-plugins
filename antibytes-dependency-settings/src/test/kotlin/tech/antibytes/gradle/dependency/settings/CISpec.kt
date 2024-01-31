/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.settings

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.ClearEnvironmentVariable
import org.junitpioneer.jupiter.SetEnvironmentVariable

class CISpec {
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

    @ClearEnvironmentVariable(key = "AZURE_HTTP_USER_AGENT")
    @Test
    fun `Given isAzureDevops is called it returns false if no AZURE_HTTP_USER_AGENT Env was found`() {
        assertFalse { isAzureDevops() }
    }

    @Test
    @SetEnvironmentVariable(key = "AZURE_HTTP_USER_AGENT", value = "whatever the value is")
    fun `Given isAzureDevops is called it returns true  if AZURE_HTTP_USER_AGENT Env was found`() {
        assertTrue { isAzureDevops() }
    }
}
