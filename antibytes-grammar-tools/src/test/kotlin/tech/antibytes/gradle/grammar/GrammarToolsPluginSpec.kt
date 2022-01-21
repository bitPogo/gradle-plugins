/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar

import io.mockk.mockk
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.junit.Test
import kotlin.test.assertTrue

class GrammarToolsPluginSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = GrammarToolsPlugin()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a project it just runs`() {
        // Given
        val project: Project = mockk()

        // When
        GrammarToolsPlugin().apply(project)

        // Then
        assertTrue(true)
    }
}
