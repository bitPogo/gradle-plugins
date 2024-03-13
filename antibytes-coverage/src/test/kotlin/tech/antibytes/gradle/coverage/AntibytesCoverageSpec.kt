/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.CoverageApiContract.CoverageConfiguration
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.coverage.task.TaskController
import tech.antibytes.gradle.test.GradlePropertyFactory.makeMapProperty
import tech.antibytes.gradle.test.GradlePropertyFactory.makeProperty
import tech.antibytes.gradle.test.invokeGradleAction

class AntibytesCoverageSpec {
    private val taskController: TaskController = mockk()
    private val defaultConfiguration: DefaultConfigurationProvider = mockk()
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setup() {
        clearMocks(taskController, defaultConfiguration)
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntibytesCoverage(defaultConfiguration, taskController)

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and processes it after the project was evaluated`() {
        // Given
        val project: Project = mockk()
        val rootPath: String = fixture()

        val extension: AntibytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val subProjects: List<Project> = listOf(mockk(), mockk(), mockk())

        subProjects.forEach {
            every { it.path } returns fixture()
            every { it.rootProject } returns project
        }

        every { project.path } returns rootPath
        every { project.rootProject } returns project
        every { project.subprojects } returns subProjects.toSet()

        every {
            project.extensions.create(any(), AntibytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every {
            project.allprojects
        } returns listOf(listOf(project), subProjects).flatten().toSet()

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOn(any()) } returns mockk()

        invokeGradleAction(project) { probe ->
            project.afterEvaluate(probe)
        }

        every { taskController.configure(any(), any()) } just Runs

        // When
        AntibytesCoverage(defaultConfiguration, taskController).apply(project)

        // Then
        verify(exactly = 1) {
            project.extensions.create(
                "antibytesCoverage",
                AntibytesCoveragePluginExtension::class.java,
                project,
            )
        }
        verify(exactly = 1) { plugins.apply("jacoco") }
        verify(exactly = 0) { project.evaluationDependsOn(rootPath) }
        subProjects.forEach {
            val path = it.path

            verify(exactly = 1) { project.evaluationDependsOn(path) }
        }
        verify(exactly = 1) { taskController.configure(project, extension) }
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and processes it after the project and ignores application order if it is not the root project`() {
        // Given
        val project: Project = mockk()
        val rootPath: String = fixture()

        val extension: AntibytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val subProjects: List<Project> = listOf(mockk(), mockk(), mockk())

        subProjects.forEach {
            every { it.path } returns fixture()
            every { it.rootProject } returns project
        }

        every { project.path } returns rootPath
        every { project.rootProject } returns mockk()

        every {
            project.extensions.create(any(), AntibytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every {
            project.allprojects
        } returns listOf(listOf(project), subProjects).flatten().toSet()

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOn(any()) } returns mockk()

        invokeGradleAction(project) { probe ->
            project.afterEvaluate(probe)
        }

        every { taskController.configure(any(), any()) } just Runs

        // When
        AntibytesCoverage(defaultConfiguration, taskController).apply(project)

        // Then
        verify(exactly = 1) {
            project.extensions.create(
                "antibytesCoverage",
                AntibytesCoveragePluginExtension::class.java,
                project,
            )
        }
        verify(exactly = 1) { plugins.apply("jacoco") }
        verify(exactly = 0) { project.evaluationDependsOn(rootPath) }
        subProjects.forEach {
            val path = it.path

            verify(exactly = 0) { project.evaluationDependsOn(path) }
        }
        verify(exactly = 1) { taskController.configure(project, extension) }
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and processes it while ignoring the Evaluation if it is a root without subs`() {
        // Given
        val project: Project = mockk()
        val rootPath: String = fixture()

        val extension: AntibytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val subProjects: List<Project> = listOf(mockk(), mockk(), mockk())

        subProjects.forEach {
            every { it.path } returns fixture()
            every { it.rootProject } returns project
            every { it.subprojects } returns setOf(mockk())
        }

        every { project.path } returns rootPath
        every { project.rootProject } returns project
        every { project.subprojects } returns emptySet()
        every {
            project.extensions.create(any(), AntibytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every {
            project.allprojects
        } returns listOf(listOf(project), subProjects).flatten().toSet()
        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOn(any()) } returns mockk()

        invokeGradleAction(project) { probe ->
            project.afterEvaluate(probe)
        }

        every { taskController.configure(any(), any()) } just Runs

        // When
        AntibytesCoverage(defaultConfiguration, taskController).apply(project)

        // Then
        verify(exactly = 1) {
            project.extensions.create(
                "antibytesCoverage",
                AntibytesCoveragePluginExtension::class.java,
                project,
            )
        }
        verify(exactly = 1) { plugins.apply("jacoco") }

        verify(exactly = 0) { project.evaluationDependsOn(rootPath) }
        subProjects.forEach {
            val path = it.path

            verify(exactly = 0) { project.evaluationDependsOn(path) }
        }

        verify(exactly = 1) { taskController.configure(project, extension) }
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and will not override an existing contexts`() {
        // Given
        val project: Project = mockk()

        val extension: AntibytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context" to mockk(),
        )
        val defaultConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "context" to mockk(),
        )

        val actualConfigurations = makeMapProperty(
            String::class.java,
            CoverageConfiguration::class.java,
            givenConfigurations.toMutableMap(),
        )

        every {
            project.extensions.create(any(), AntibytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { project.rootProject } returns mockk()
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOn(any()) } returns mockk()

        invokeGradleAction(project) { probe ->
            project.afterEvaluate(probe)
        }

        every { taskController.configure(any(), any()) } just Runs

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { extension.appendKmpJvmTask } returns makeProperty(Boolean::class.java, true)
        every { defaultConfiguration.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntibytesCoverage(defaultConfiguration, taskController).apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations.get(),
            expected = givenConfigurations,
        )
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and will not override an existing jvm contexts`() {
        // Given
        val project: Project = mockk()

        val extension: AntibytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
        )
        val defaultConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context2" to mockk(),
        )

        val actualConfigurations = makeMapProperty(
            String::class.java,
            CoverageConfiguration::class.java,
            givenConfigurations.toMutableMap(),
        )

        every {
            project.extensions.create(any(), AntibytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { project.rootProject } returns mockk()
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOn(any()) } returns mockk()

        invokeGradleAction(project) { probe ->
            project.afterEvaluate(probe)
        }

        every { taskController.configure(any(), any()) } just Runs

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { extension.appendKmpJvmTask } returns makeProperty(Boolean::class.java, true)
        every { defaultConfiguration.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntibytesCoverage(defaultConfiguration, taskController).apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations.get(),
            expected = givenConfigurations,
        )
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and adds missing the jvm contexts, if the project is KMP and has no preset jvm`() {
        // Given
        val project: Project = mockk()

        val extension: AntibytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf()
        val defaultConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context2" to mockk(),
        )

        val actualConfigurations = makeMapProperty(
            String::class.java,
            CoverageConfiguration::class.java,
            givenConfigurations.toMutableMap(),
        )

        every {
            project.extensions.create(any(), AntibytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { project.rootProject } returns mockk()

        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOn(any()) } returns mockk()

        invokeGradleAction(project) { probe ->
            project.afterEvaluate(probe)
        }

        every { taskController.configure(any(), any()) } just Runs

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { extension.appendKmpJvmTask } returns makeProperty(Boolean::class.java, true)
        every { defaultConfiguration.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntibytesCoverage(defaultConfiguration, taskController).apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations.get(),
            expected = givenConfigurations.also {
                it["jvm"] = defaultConfigurations["jvm"]!!
            },
        )
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and will not add a jvm contexts, if the append policy is false`() {
        // Given
        val project: Project = mockk()

        val extension: AntibytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf()
        val defaultConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context2" to mockk(),
        )

        val actualConfigurations = makeMapProperty(
            String::class.java,
            CoverageConfiguration::class.java,
            givenConfigurations.toMutableMap(),
        )

        every {
            project.extensions.create(any(), AntibytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { project.rootProject } returns mockk()
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOn(any()) } returns mockk()

        invokeGradleAction(project) { probe ->
            project.afterEvaluate(probe)
        }

        every { taskController.configure(any(), any()) } just Runs

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { extension.appendKmpJvmTask } returns makeProperty(Boolean::class.java, false)
        every { defaultConfiguration.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntibytesCoverage(defaultConfiguration, taskController).apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations.get(),
            expected = givenConfigurations,
        )
    }

    @Test
    fun `Given apply is called with a Project, it will not applies the JacocoPlugin, if it is already present`() {
        // Given
        val project: Project = mockk()

        val extension: AntibytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()

        every {
            project.extensions.create(any(), AntibytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { project.rootProject } returns mockk()
        every { plugins.hasPlugin("jacoco") } returns true
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOn(any()) } returns mockk()

        every { defaultConfiguration.createDefaultCoverageConfiguration(any()) } returns mutableMapOf()

        every { project.afterEvaluate(any<Action<Project>>()) } just Runs

        every { taskController.configure(any(), any()) } just Runs

        // When
        AntibytesCoverage(defaultConfiguration, taskController).apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("jacoco") }
    }
}
