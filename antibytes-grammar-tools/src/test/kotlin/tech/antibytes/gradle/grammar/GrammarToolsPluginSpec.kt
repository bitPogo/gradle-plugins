/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.junit.Test
import tech.antibytes.gradle.grammar.bison.BisonTask
import tech.antibytes.gradle.grammar.jflex.JFlexTask
import tech.antibytes.gradle.test.invokeGradleAction
import kotlin.test.assertTrue

class GrammarToolsPluginSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = GrammarToolsPlugin()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a project it adds a plain JFlex Task`() {
        // Given
        val project: Project = mockk()

        val jflexTask: JFlexTask = mockk(relaxed = true)

        invokeGradleAction(
            { probe -> project.tasks.create("jflex", JFlexTask::class.java, probe) },
            jflexTask,
            jflexTask
        )

        every { project.tasks.create(any(), BisonTask::class.java, any()) } returns mockk()

        // When
        GrammarToolsPlugin().apply(project)

        // Then
        verify(exactly = 1) { jflexTask.group = "Code Generation" }
        verify(exactly = 1) { jflexTask.description = "Generates a scanner from an (Java)FlexFile" }
    }

    @Test
    fun `Given apply is called with a project it adds a plain Bison Task`() {
        // Given
        val project: Project = mockk()

        val bisonTask: BisonTask = mockk(relaxed = true)

        invokeGradleAction(
            { probe -> project.tasks.create("bison", BisonTask::class.java, probe) },
            bisonTask,
            bisonTask
        )

        every { project.tasks.create(any(), JFlexTask::class.java, any()) } returns mockk()

        // When
        GrammarToolsPlugin().apply(project)

        // Then
        verify(exactly = 1) { bisonTask.group = "Code Generation" }
        verify(exactly = 1) { bisonTask.description = "Generates a parser from an Grammar File" }
    }
}
