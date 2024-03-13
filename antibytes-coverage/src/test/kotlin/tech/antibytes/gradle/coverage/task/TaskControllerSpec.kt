/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:argument-list-wrapping")

package tech.antibytes.gradle.coverage.task

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.AntibytesCoveragePluginExtension
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageContract
import tech.antibytes.gradle.coverage.CoverageError
import tech.antibytes.gradle.coverage.task.extension.AndroidExtensionConfigurator
import tech.antibytes.gradle.coverage.task.extension.JacocoExtensionConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoAggregationReportTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoAggregationVerificationTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoReportTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoVerificationTaskConfigurator
import tech.antibytes.gradle.test.GradlePropertyFactory
import tech.antibytes.gradle.test.invokeGradleAction

class TaskControllerSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils TaskController`() {
        val configurator: Any = TaskController()

        assertTrue(configurator is CoverageContract.TaskController)
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, it fails if the the map contains a unknown CoverageConfigurations Type`() {
        // Given
        val project: Project = mockk()
        val extension: AntibytesCoveragePluginExtension = mockk()
        val configuration: CoverageApiContract.CoverageConfiguration = mockk()

        every { project.rootProject } returns mockk()
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf("unknown" to configuration),
        )
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false

        // Then
        assertFailsWith<CoverageError.UnknownPlatformConfiguration> {
            // When
            TaskController().configure(project, extension)
        }
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which contains a JacocoConfiguration it configures the coverage and verification task and extension`() {
        // Given
        val jacocoReportTaskConfigurator: JacocoReportTaskConfigurator = mockk()
        val jacocoExtensionConfigurator: JacocoExtensionConfigurator = mockk()
        val verificationTaskConfigurator: JacocoVerificationTaskConfigurator = mockk()
        val project: Project = mockk()
        val contextId: String = fixture()
        val extension: AntibytesCoveragePluginExtension = mockk()
        val configuration: CoverageApiContract.JacocoCoverageConfiguration = mockk()

        every { project.rootProject } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java, mutableMapOf(contextId to configuration),
        )
        every { jacocoReportTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { verificationTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { jacocoExtensionConfigurator.configure(any(), any()) } just Runs

        // When
        TaskController(
            jacocoExtensionConfigurator = jacocoExtensionConfigurator,
            jacocoVerificationTaskConfigurator = verificationTaskConfigurator,
            jacocoReporterTaskConfigurator = jacocoReportTaskConfigurator,
        ).configure(project, extension)

        // Then
        verify(exactly = 1) { jacocoReportTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { verificationTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { jacocoExtensionConfigurator.configure(project, extension) }
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which contains a AndroidJacocoConfiguration it configures the coverage and verification task and extensions`() {
        // Given
        val jacocoReportTaskConfigurator: JacocoReportTaskConfigurator = mockk()
        val jacocoExtensionConfigurator: JacocoExtensionConfigurator = mockk()
        val androidExtensionConfigurator: AndroidExtensionConfigurator = mockk()
        val verificationTaskConfigurator: JacocoVerificationTaskConfigurator = mockk()
        val project: Project = mockk()
        val contextId: String = fixture()
        val reporter: Task = mockk()
        val extension: AntibytesCoveragePluginExtension = mockk()
        val configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()

        every { project.rootProject } returns mockk()
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java, mutableMapOf(contextId to configuration),
        )
        every { jacocoReportTaskConfigurator.configure(any(), any(), any()) } returns reporter
        every { verificationTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { jacocoExtensionConfigurator.configure(any(), any()) } just Runs
        every { androidExtensionConfigurator.configure(any()) } just Runs

        // When
        TaskController(
            androidExtensionConfigurator = androidExtensionConfigurator,
            jacocoExtensionConfigurator = jacocoExtensionConfigurator,
            jacocoReporterTaskConfigurator = jacocoReportTaskConfigurator,
            jacocoVerificationTaskConfigurator = verificationTaskConfigurator,
        ).configure(project, extension)

        // Then
        verify(exactly = 1) { jacocoReportTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { verificationTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { jacocoExtensionConfigurator.configure(project, extension) }
        verify(exactly = 1) { androidExtensionConfigurator.configure(project) }
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which indicates a KMP setup, it adds a multiplatform reporter task`() {
        // Given
        val jacocoReportTaskConfigurator: JacocoReportTaskConfigurator = mockk()
        val jacocoExtensionConfigurator: JacocoExtensionConfigurator = mockk()
        val androidExtensionConfigurator: AndroidExtensionConfigurator = mockk()
        val verificationTaskConfigurator: JacocoVerificationTaskConfigurator = mockk()
        val project: Project = mockk()
        val contextId: String = fixture()
        val extension: AntibytesCoveragePluginExtension = mockk()
        val configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()
        val jacocoReporterTask: Task = mockk()

        val kmpReporterTask: Task = mockk(relaxed = true)

        every { project.rootProject } returns mockk()
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java, mutableMapOf(contextId to configuration),
        )
        every { jacocoReportTaskConfigurator.configure(any(), any(), any()) } returns jacocoReporterTask
        every { verificationTaskConfigurator.configure(any(), any(), any()) } returns null
        every { jacocoExtensionConfigurator.configure(any(), any()) } just Runs
        every { androidExtensionConfigurator.configure(any()) } just Runs

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.tasks.create(any(), any<Action<Task>>()) } returns mockk()

        invokeGradleAction(
            kmpReporterTask,
            mockk(),
        ) { probe ->
            project.tasks.create("multiplatformCoverage", probe)
        }

        // When
        TaskController(
            androidExtensionConfigurator = androidExtensionConfigurator,
            jacocoExtensionConfigurator = jacocoExtensionConfigurator,
            jacocoReporterTaskConfigurator = jacocoReportTaskConfigurator,
            jacocoVerificationTaskConfigurator = verificationTaskConfigurator,
        ).configure(project, extension)

        // Then
        verify(exactly = 1) { kmpReporterTask.group = "Verification" }
        verify(exactly = 1) { kmpReporterTask.description = "Generate a coverage reports for all platforms of multiplatform projects." }
        verify(exactly = 1) { kmpReporterTask.dependsOn(setOf(jacocoReporterTask)) }
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which indicates a KMP setup it will not add a multiplatform verification task, if no verification tasks had been setup`() {
        // Given
        val jacocoReportTaskConfigurator: JacocoReportTaskConfigurator = mockk()
        val jacocoExtensionConfigurator: JacocoExtensionConfigurator = mockk()
        val androidExtensionConfigurator: AndroidExtensionConfigurator = mockk()
        val verificationTaskConfigurator: JacocoVerificationTaskConfigurator = mockk()
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val contextId: String = fixture()
        val extension: AntibytesCoveragePluginExtension = mockk()
        val configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()

        every { project.rootProject } returns mockk()
        every { project.tasks } returns tasks
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java, mutableMapOf(contextId to configuration),
        )
        every { jacocoReportTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { verificationTaskConfigurator.configure(any(), any(), any()) } returns null
        every { jacocoExtensionConfigurator.configure(any(), any()) } just Runs
        every { androidExtensionConfigurator.configure(any()) } just Runs

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()

        // When
        TaskController(
            androidExtensionConfigurator = androidExtensionConfigurator,
            jacocoExtensionConfigurator = jacocoExtensionConfigurator,
            jacocoReporterTaskConfigurator = jacocoReportTaskConfigurator,
            jacocoVerificationTaskConfigurator = verificationTaskConfigurator,
        ).configure(project, extension)

        // Then
        verify(exactly = 0) { tasks.create("multiplatformCoverageVerification", any<Action<Task>>()) }
    }

    @Test
    fun `Given configure is called with a Project and AntiBytesCoverageExtension, which indicates a KMP setup, it adds a multiplatform verification task, if verification tasks had been setup`() {
        // Given
        val jacocoReportTaskConfigurator: JacocoReportTaskConfigurator = mockk()
        val jacocoExtensionConfigurator: JacocoExtensionConfigurator = mockk()
        val androidExtensionConfigurator: AndroidExtensionConfigurator = mockk()
        val verificationTaskConfigurator: JacocoVerificationTaskConfigurator = mockk()
        val project: Project = mockk()
        val contextId: String = fixture()
        val extension: AntibytesCoveragePluginExtension = mockk()
        val configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration = mockk()
        val jacocoVerificationTask: Task = mockk()

        val kmpVerificationTask: Task = mockk(relaxed = true)

        every { project.rootProject } returns mockk()
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java, mutableMapOf(contextId to configuration),
        )
        every { jacocoReportTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { verificationTaskConfigurator.configure(any(), any(), any()) } returns jacocoVerificationTask
        every { jacocoExtensionConfigurator.configure(any(), any()) } just Runs
        every { androidExtensionConfigurator.configure(any()) } just Runs

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.tasks.create(any(), any<Action<Task>>()) } returns mockk()

        invokeGradleAction(
            kmpVerificationTask,
            mockk(),
        ) { probe ->
            project.tasks.create("multiplatformCoverageVerification", probe)
        }

        // When
        TaskController(
            androidExtensionConfigurator = androidExtensionConfigurator,
            jacocoExtensionConfigurator = jacocoExtensionConfigurator,
            jacocoReporterTaskConfigurator = jacocoReportTaskConfigurator,
            jacocoVerificationTaskConfigurator = verificationTaskConfigurator,
        ).configure(project, extension)

        // Then
        verify(exactly = 1) { kmpVerificationTask.group = "Verification" }
        verify(exactly = 1) { kmpVerificationTask.description = "Verifies the coverage for all platforms of multiplatform projects." }
        verify(exactly = 1) { kmpVerificationTask.dependsOn(listOf(jacocoVerificationTask)) }
    }

    @Test
    fun `Given configure is called with a Project, which is the ProjectRoot and AntiBytesCoverageExtension, it fails if the the map contains a unknown CoverageConfigurations Type`() {
        // Given
        val project: Project = mockk()
        val extension: AntibytesCoveragePluginExtension = mockk()
        val configuration: CoverageApiContract.CoverageConfiguration = mockk()

        every { project.rootProject } returns project
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf("unknown" to configuration),
        )
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false

        // Then
        assertFailsWith<CoverageError.UnknownPlatformConfiguration> {
            // When
            TaskController().configure(project, extension)
        }
    }

    @Test
    fun `Given configure is called with a Project, which is the ProjectRoot and AntiBytesCoverageExtension, which contains a JacocoConfiguration it configures the coverage and verification task and extension`() {
        // Given
        val jacocoAggregationReportTaskConfigurator: JacocoAggregationReportTaskConfigurator = mockk()
        val jacocoExtensionConfigurator: JacocoExtensionConfigurator = mockk()
        val aggregationVerification: JacocoAggregationVerificationTaskConfigurator = mockk()
        val project: Project = mockk()
        val contextId: String = fixture()
        val extension: AntibytesCoveragePluginExtension = mockk()
        val configuration: CoverageApiContract.JacocoAggregationConfiguration = mockk()

        every { project.rootProject } returns project
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java, mutableMapOf(contextId to configuration),
        )
        every { jacocoAggregationReportTaskConfigurator.configure(any(), any(), any()) } returns mockk()
        every { aggregationVerification.configure(any(), any(), any()) } returns mockk()
        every { jacocoExtensionConfigurator.configure(any(), any()) } just Runs

        // When
        TaskController(
            jacocoExtensionConfigurator = jacocoExtensionConfigurator,
            jacocoAggregationReportTaskConfigurator = jacocoAggregationReportTaskConfigurator,
            jacocoAggregationVerificationTaskConfigurator = aggregationVerification,
        ).configure(project, extension)

        // Then
        verify(exactly = 1) { jacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration) }
        verify(exactly = 1) { aggregationVerification.configure(project, contextId, configuration) }
        verify(exactly = 1) { jacocoExtensionConfigurator.configure(project, extension) }
    }

    @Test
    fun `Given configure is called with a Project, which is the Root and AntiBytesCoverageExtension, it adds a multiplatform reporter task, if multiple contexts are present`() {
        // Given
        val jacocoAggregationReportTaskConfigurator: JacocoAggregationReportTaskConfigurator = mockk()
        val jacocoExtensionConfigurator: JacocoExtensionConfigurator = mockk()
        val aggregationVerification: JacocoAggregationVerificationTaskConfigurator = mockk()
        val project: Project = mockk()
        val contextId1: String = fixture()
        val contextId2: String = fixture()
        val extension: AntibytesCoveragePluginExtension = mockk()
        val configuration1: CoverageApiContract.AndroidJacocoAggregationConfiguration = mockk()
        val configuration2: CoverageApiContract.AndroidJacocoAggregationConfiguration = mockk()
        val task1: Task = mockk()
        val task2: Task = mockk()

        val kmpReporterTask: Task = mockk(relaxed = true)
        val dependencies = slot<Set<Task>>()

        every { project.rootProject } returns project
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(
                contextId1 to configuration1,
                contextId2 to configuration2,
            ),
        )
        every { jacocoAggregationReportTaskConfigurator.configure(any(), any(), any()) } returnsMany listOf(
            task1,
            task2,
        )
        every { aggregationVerification.configure(any(), any(), any()) } returns null
        every { jacocoExtensionConfigurator.configure(any(), any()) } just Runs

        every { project.tasks.create(any(), any<Action<Task>>()) } returns mockk()

        invokeGradleAction(
            kmpReporterTask,
            mockk(),
        ) { probe ->
            project.tasks.create("multiplatformCoverage", probe)
        }

        every { kmpReporterTask.dependsOn(capture(dependencies)) } returns mockk()

        // When
        TaskController(
            jacocoExtensionConfigurator = jacocoExtensionConfigurator,
            jacocoAggregationReportTaskConfigurator = jacocoAggregationReportTaskConfigurator,
            jacocoAggregationVerificationTaskConfigurator = aggregationVerification,
        ).configure(project, extension)

        // Then
        verify(exactly = 1) { kmpReporterTask.group = "Verification" }
        verify(exactly = 1) { kmpReporterTask.description = "Generate a coverage reports for all platforms of multiplatform projects." }
        assertEquals(
            actual = dependencies.captured,
            expected = setOf(task1, task2),
        )
    }

    @Test
    fun `Given configure is called with a Project, which is the Root and AntiBytesCoverageExtension, it will not add a multiplatform verification task, if no verification tasks had been setup`() {
        // Given
        val jacocoAggregationReportTaskConfigurator: JacocoAggregationReportTaskConfigurator = mockk()
        val jacocoExtensionConfigurator: JacocoExtensionConfigurator = mockk()
        val aggregationVerification: JacocoAggregationVerificationTaskConfigurator = mockk()
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val contextId1: String = fixture()
        val contextId2: String = fixture()
        val extension: AntibytesCoveragePluginExtension = mockk()

        val configuration1: CoverageApiContract.AndroidJacocoAggregationConfiguration = mockk()
        val configuration2: CoverageApiContract.AndroidJacocoAggregationConfiguration = mockk()
        val task1: Task = mockk()
        val task2: Task = mockk()

        val kmpReporterTask: Task = mockk(relaxed = true)
        val dependencies = slot<Set<Task>>()

        every { project.rootProject } returns project
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { project.tasks } returns tasks
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(
                contextId1 to configuration1,
                contextId2 to configuration2,
            ),
        )
        every { jacocoAggregationReportTaskConfigurator.configure(any(), any(), any()) } returnsMany listOf(
            task1,
            task2,
        )
        every { aggregationVerification.configure(any(), any(), any()) } returns null
        every { jacocoExtensionConfigurator.configure(any(), any()) } just Runs

        every { project.tasks.create(any(), any<Action<Task>>()) } returns mockk()

        invokeGradleAction(
            kmpReporterTask,
            mockk(),
        ) { probe ->
            project.tasks.create("multiplatformCoverage", probe)
        }

        every { kmpReporterTask.dependsOn(capture(dependencies)) } returns mockk()

        // When
        TaskController(
            jacocoExtensionConfigurator = jacocoExtensionConfigurator,
            jacocoAggregationReportTaskConfigurator = jacocoAggregationReportTaskConfigurator,
            jacocoAggregationVerificationTaskConfigurator = aggregationVerification,
        ).configure(project, extension)

        // Then
        verify(exactly = 0) { tasks.create("multiplatformCoverageVerification", any<Action<Task>>()) }
    }

    @Test
    fun `Given configure is called with a Project, which is Root  it adds a multiplatform verification task if verification tasks had been setup and multiple contexts are present`() {
        // Given
        val jacocoAggregationReportTaskConfigurator: JacocoAggregationReportTaskConfigurator = mockk()
        val jacocoExtensionConfigurator: JacocoExtensionConfigurator = mockk()
        val aggregationVerification: JacocoAggregationVerificationTaskConfigurator = mockk()
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val extension: AntibytesCoveragePluginExtension = mockk()

        val contextId1: String = fixture()
        val contextId2: String = fixture()

        val configuration1: CoverageApiContract.AndroidJacocoAggregationConfiguration = mockk()
        val configuration2: CoverageApiContract.AndroidJacocoAggregationConfiguration = mockk()

        val task1: Task = mockk()
        val task2: Task = mockk()

        val verificationTask1: Task = mockk()
        val verificationTask2: Task = mockk()

        every { project.rootProject } returns project
        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { project.tasks } returns tasks
        every { extension.configurations } returns GradlePropertyFactory.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(
                contextId1 to configuration1,
                contextId2 to configuration2,
            ),
        )
        every { jacocoAggregationReportTaskConfigurator.configure(any(), any(), any()) } returnsMany listOf(
            task1,
            task2,
        )
        every { aggregationVerification.configure(any(), any(), any()) } returnsMany listOf(
            verificationTask1,
            verificationTask2,
        )
        every { jacocoExtensionConfigurator.configure(any(), any()) } just Runs

        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()

        // When
        TaskController(
            jacocoExtensionConfigurator = jacocoExtensionConfigurator,
            jacocoAggregationReportTaskConfigurator = jacocoAggregationReportTaskConfigurator,
            jacocoAggregationVerificationTaskConfigurator = aggregationVerification,
        ).configure(project, extension)

        // Then
        // Please invokeGradleAction causes a Error, which is probably caused by mockk
        verify(exactly = 1) { tasks.create("multiplatformCoverageVerification", any<Action<Task>>()) }
    }
}
