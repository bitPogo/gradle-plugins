/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.jflex

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.junit.Test
import tech.antibytes.gradle.test.invokeGradleAction
import kotlin.test.assertTrue

class JFlexPluginSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = JFlexPlugin()

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
        every { project.tasks.create(any(), any<Class<out Task>>()) } returns mockk()
        // When
        JFlexPlugin().apply(project)

        // Then
        verify(exactly = 1) { jflexTask.group = "Code Generation" }
        verify(exactly = 1) { jflexTask.description = "Generate a scanner from an (Java)FlexFile" }
    }
}
