/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.coverage.AntiBytesCoverageExtension
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageContract
import tech.antibytes.gradle.coverage.CoverageError
import tech.antibytes.gradle.coverage.configuration.PlatformContextResolver
import tech.antibytes.gradle.coverage.task.extension.AndroidExtensionConfigurator
import tech.antibytes.gradle.coverage.task.extension.JacocoExtensionConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoAggregationReportTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoReportTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoVerificationTaskConfigurator
import tech.antibytes.gradle.publishing.invokeGradleAction
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TaskControllerSpec {
    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        mockkObject(PlatformContextResolver)

        every { PlatformContextResolver.isKmp(any<Project>()) } returns false
    }

    @After
    fun tearDown() {
        unmockkObject(PlatformContextResolver)
    }

    @Test
    fun `It fulfils TaskController`() {
        val configurator: Any = TaskController

        assertTrue(configurator is CoverageContract.TaskController)
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, it fails if the the map contains a unknown CoverageConfigurations Type`() {
        // Given
        val project: Project = mockk()
        val extension: AntiBytesCoverageExtension = mockk()
        val configuration: CoverageApiContract.CoverageConfiguration = mockk()

        every { project.rootProject } returns mockk()
        every { extension.configurations } returns mutableMapOf("unknown" to configuration)

        // Then
        assertFailsWith<CoverageError.UnknownPlatformConfiguration> {
            // When
            TaskController.configure(project, extension)
        }
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which contains a JacocoConfiguration it configures the coverage and verification task and extension`() {
        mockkObject(JacocoReportTaskConfigurator)
        mockkObject(JacocoVerificationTaskConfigurator)
        mockkObject(JacocoExtensionConfigurator)

        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val extension: AntiBytesCoverageExtension = mockk()
        val configuration: CoverageApiContract.JacocoCoverageConfiguration = mockk()

        every { project.rootProject } returns mockk()
        every { extension.configurations } returns mutableMapOf(contextId to configuration)
        every { JacocoReportTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { JacocoVerificationTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { JacocoExtensionConfigurator.configure(any(), any()) } just Runs

        // When
        TaskController.configure(project, extension)

        // Then
        verify(exactly = 1) { JacocoReportTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { JacocoVerificationTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { JacocoExtensionConfigurator.configure(project, extension) }

        unmockkObject(JacocoReportTaskConfigurator)
        unmockkObject(JacocoVerificationTaskConfigurator)
        unmockkObject(JacocoExtensionConfigurator)
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which contains a AndroidJacocoConfiguration it configures the coverage and verification task and extensions`() {
        mockkObject(JacocoReportTaskConfigurator)
        mockkObject(JacocoVerificationTaskConfigurator)
        mockkObject(JacocoExtensionConfigurator)
        mockkObject(AndroidExtensionConfigurator)

        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val reporter: Task = mockk()
        val extension: AntiBytesCoverageExtension = mockk()
        val configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()

        every { project.rootProject } returns mockk()
        every { extension.configurations } returns mutableMapOf(contextId to configuration)
        every { JacocoReportTaskConfigurator.configure(any(), any(), any()) } returns reporter
        every { JacocoVerificationTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { JacocoExtensionConfigurator.configure(any(), any()) } just Runs
        every { AndroidExtensionConfigurator.configure(any()) } just Runs

        // When
        TaskController.configure(project, extension)

        // Then
        verify(exactly = 1) { JacocoReportTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { JacocoVerificationTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { JacocoExtensionConfigurator.configure(project, extension) }
        verify(exactly = 1) { AndroidExtensionConfigurator.configure(project) }

        unmockkObject(JacocoReportTaskConfigurator)
        unmockkObject(JacocoVerificationTaskConfigurator)
        unmockkObject(JacocoExtensionConfigurator)
        unmockkObject(AndroidExtensionConfigurator)
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which indicates a KMP setup, it adds a multiplatform reporter task`() {
        mockkObject(JacocoReportTaskConfigurator)
        mockkObject(JacocoVerificationTaskConfigurator)
        mockkObject(JacocoExtensionConfigurator)
        mockkObject(AndroidExtensionConfigurator)

        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val extension: AntiBytesCoverageExtension = mockk()
        val configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()
        val jacocoReporterTask: Task = mockk()

        val kmpReporterTask: Task = mockk(relaxed = true)

        every { project.rootProject } returns mockk()
        every { extension.configurations } returns mutableMapOf(contextId to configuration)
        every { JacocoReportTaskConfigurator.configure(any(), any(), any()) } returns jacocoReporterTask
        every { JacocoVerificationTaskConfigurator.configure(any(), any(), any()) } returns null
        every { JacocoExtensionConfigurator.configure(any(), any()) } just Runs
        every { AndroidExtensionConfigurator.configure(any()) } just Runs

        every { PlatformContextResolver.isKmp(project) } returns true
        every { project.tasks.create(any(), any<Action<Task>>()) } returns mockk()

        invokeGradleAction(
            { probe -> project.tasks.create("multiplatformCoverage", probe) },
            kmpReporterTask,
            mockk()
        )

        // When
        TaskController.configure(project, extension)

        // Then
        verify(exactly = 1) { kmpReporterTask.group = "Verification" }
        verify(exactly = 1) { kmpReporterTask.description = "Generate a coverage reports for all platforms of multiplatform projects." }
        verify(exactly = 1) { kmpReporterTask.dependsOn(setOf(jacocoReporterTask)) }

        unmockkObject(JacocoReportTaskConfigurator)
        unmockkObject(JacocoVerificationTaskConfigurator)
        unmockkObject(JacocoExtensionConfigurator)
        unmockkObject(AndroidExtensionConfigurator)
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which indicates a KMP setup it will not add a multiplatform verification task, if no verification tasks had been setup`() {
        mockkObject(JacocoReportTaskConfigurator)
        mockkObject(JacocoVerificationTaskConfigurator)
        mockkObject(JacocoExtensionConfigurator)
        mockkObject(AndroidExtensionConfigurator)

        // Given
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val contextId: String = fixture()
        val extension: AntiBytesCoverageExtension = mockk()
        val configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()

        every { project.rootProject } returns mockk()
        every { project.tasks } returns tasks
        every { extension.configurations } returns mutableMapOf(contextId to configuration)
        every { JacocoReportTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { JacocoVerificationTaskConfigurator.configure(any(), any(), any()) } returns null
        every { JacocoExtensionConfigurator.configure(any(), any()) } just Runs
        every { AndroidExtensionConfigurator.configure(any()) } just Runs

        every { PlatformContextResolver.isKmp(project) } returns true
        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()

        // When
        TaskController.configure(project, extension)

        // Then
        verify(exactly = 0) { tasks.create("multiplatformCoverageVerification", any<Action<Task>>()) }

        unmockkObject(JacocoReportTaskConfigurator)
        unmockkObject(JacocoVerificationTaskConfigurator)
        unmockkObject(JacocoExtensionConfigurator)
        unmockkObject(AndroidExtensionConfigurator)
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which indicates a KMP setup, it adds a multiplatform verification task, if verification tasks had been setup`() {
        mockkObject(JacocoReportTaskConfigurator)
        mockkObject(JacocoVerificationTaskConfigurator)
        mockkObject(JacocoExtensionConfigurator)
        mockkObject(AndroidExtensionConfigurator)

        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val extension: AntiBytesCoverageExtension = mockk()
        val configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()
        val jacocoVerificationTask: Task = mockk()

        val kmpVerificationTask: Task = mockk(relaxed = true)

        every { project.rootProject } returns mockk()
        every { extension.configurations } returns mutableMapOf(contextId to configuration)
        every { JacocoReportTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { JacocoVerificationTaskConfigurator.configure(any(), any(), any()) } returns jacocoVerificationTask
        every { JacocoExtensionConfigurator.configure(any(), any()) } just Runs
        every { AndroidExtensionConfigurator.configure(any()) } just Runs

        every { PlatformContextResolver.isKmp(project) } returns true
        every { project.tasks.create(any(), any<Action<Task>>()) } returns mockk()

        invokeGradleAction(
            { probe -> project.tasks.create("multiplatformCoverageVerification", probe) },
            kmpVerificationTask,
            mockk()
        )

        // When
        TaskController.configure(project, extension)

        // Then
        verify(exactly = 1) { kmpVerificationTask.group = "Verification" }
        verify(exactly = 1) { kmpVerificationTask.description = "Verifies the coverage for all platforms of multiplatform projects." }
        verify(exactly = 1) { kmpVerificationTask.dependsOn(listOf(jacocoVerificationTask)) }

        unmockkObject(JacocoReportTaskConfigurator)
        unmockkObject(JacocoVerificationTaskConfigurator)
        unmockkObject(JacocoExtensionConfigurator)
        unmockkObject(AndroidExtensionConfigurator)
    }

    @Test
    fun `Given configure is called with a Project, which is the ProjectRoot and AntiBytesCoverageExtension, it fails if the the map contains a unknown CoverageConfigurations Type`() {
        // Given
        val project: Project = mockk()
        val extension: AntiBytesCoverageExtension = mockk()
        val configuration: CoverageApiContract.CoverageConfiguration = mockk()

        every { project.rootProject } returns project
        every { extension.configurations } returns mutableMapOf("unknown" to configuration)

        // Then
        assertFailsWith<CoverageError.UnknownPlatformConfiguration> {
            // When
            TaskController.configure(project, extension)
        }
    }

    @Test
    fun `Given configure is called with a Project, which is the ProjectRoot and AntiBytesCoverageExtension, which contains a JacocoConfiguration it configures the coverage and verification task and extension`() {
        mockkObject(JacocoAggregationReportTaskConfigurator)
        mockkObject(JacocoExtensionConfigurator)

        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val extension: AntiBytesCoverageExtension = mockk()
        val configuration: CoverageApiContract.JacocoAggregationConfiguration = mockk()

        every { project.rootProject } returns project
        every { extension.configurations } returns mutableMapOf(contextId to configuration)
        every { JacocoAggregationReportTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { JacocoExtensionConfigurator.configure(any(), any()) } just Runs

        // When
        TaskController.configure(project, extension)

        // Then
        verify(exactly = 1) { JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { JacocoExtensionConfigurator.configure(project, extension) }

        unmockkObject(JacocoAggregationReportTaskConfigurator)
        unmockkObject(JacocoExtensionConfigurator)
    }
}
