/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.signing

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.plugins.signing.SigningExtension
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.invokeGradleAction

class CommonSigningSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils CommonSigning`() {
        val publisher: Any = CommonSigning

        assertTrue(publisher is SigningContract.CommonSigning)
    }

    @Test
    fun `Given configure set required true when publishing task is present`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val gradle: Gradle = mockk()
        val taskExecutionGraph: TaskExecutionGraph = mockk()
        val allTasks = listOf<Task>(
            mockk<PublishToMavenRepository>(),
        )

        val signingExtension: SigningExtension = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { signingExtension.isRequired = any() } just Runs
        every { project.gradle } returns gradle
        every { gradle.taskGraph } returns taskExecutionGraph
        every { taskExecutionGraph.allTasks } returns allTasks

        invokeGradleAction(
            { probe -> extensions.configure(SigningExtension::class.java, probe) },
            signingExtension,
        )

        CommonSigning.configure(project)

        verify(exactly = 1) { signingExtension.isRequired = true }
    }

    @Test
    fun `Given configure set required false when publishing task is not present`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val gradle: Gradle = mockk()
        val taskExecutionGraph: TaskExecutionGraph = mockk()
        val allTasks = listOf<Task>(mockk(relaxed = true))

        val signingExtension: SigningExtension = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { signingExtension.isRequired = any() } just Runs
        every { project.gradle } returns gradle
        every { gradle.taskGraph } returns taskExecutionGraph
        every { taskExecutionGraph.allTasks } returns allTasks

        invokeGradleAction(
            { probe -> extensions.configure(SigningExtension::class.java, probe) },
            signingExtension,
        )

        CommonSigning.configure(project)

        verify(exactly = 1) { signingExtension.isRequired = false }
    }
}
