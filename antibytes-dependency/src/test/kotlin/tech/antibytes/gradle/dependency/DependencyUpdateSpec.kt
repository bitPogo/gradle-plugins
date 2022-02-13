/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionRulesWithCurrent
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ResolutionStrategyWithCurrent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.GradlePropertyBuilder
import tech.antibytes.gradle.test.invokeGradleAction
import kotlin.test.assertTrue

class DependencyUpdateSpec {
    @Test
    fun `It fulfils DependencyUpdate`() {
        val updater: Any = DependencyUpdate

        assertTrue(updater is DependencyContract.Update)
    }

    @Test
    fun `Given configure is called with a Project and DependencyExtension, it rejects if the current version is stable and the candidate version is not`() {
        // Given
        val currentVersion = "xyz-C"
        val candidateVersion = "lmp-X"

        val config = object : DependencyContract.DependencyPluginExtension {
            override val keywords: SetProperty<String> = GradlePropertyBuilder.makeSetProperty(
                String::class.java,
                setOf("A", "B", "C")
            )
            override val versionRegex: Property<Regex> = GradlePropertyBuilder.makeProperty(
                Regex::class.java,
                "xyz.*".toRegex()
            )
        }
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val taskProvider: TaskProvider<DependencyUpdatesTask> = mockk()
        val dependencyTask: DependencyUpdatesTask = mockk()
        val strategy: ResolutionStrategyWithCurrent = mockk()
        val ruledSelection: ComponentSelectionRulesWithCurrent = mockk()
        val selection: ComponentSelectionWithCurrent = mockk()

        every { project.tasks } returns tasks
        every {
            tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java)
        } returns taskProvider
        every { selection.reject(any()) } just Runs

        invokeGradleAction(
            { probe -> taskProvider.configure(probe) },
            dependencyTask,
            mockk()
        )

        invokeGradleAction(
            { probe -> dependencyTask.resolutionStrategy(probe) },
            strategy,
            mockk()
        )

        invokeGradleAction(
            { probe -> strategy.componentSelection(probe) },
            ruledSelection,
            mockk()
        )

        invokeGradleAction(
            { probe -> ruledSelection.all(probe) },
            selection,
            mockk()
        )

        every { selection.currentVersion } returns currentVersion
        every { selection.candidate.version } returns candidateVersion

        // When
        DependencyUpdate.configure(
            project,
            config
        )

        // Then
        verify(exactly = 1) { selection.reject(any()) }
    }

    @Test
    fun `Given configure is called with a Project and DependencyExtension, it accepts if the current version and the candidate version are stable`() {
        // Given
        val currentVersion = "lmp-B"
        val candidateVersion = "xyz-Y"

        val config = object : DependencyContract.DependencyPluginExtension {
            override val keywords: SetProperty<String> = GradlePropertyBuilder.makeSetProperty(
                String::class.java,
                setOf("A", "B", "C")
            )
            override val versionRegex: Property<Regex> = GradlePropertyBuilder.makeProperty(
                Regex::class.java,
                "xyz.*".toRegex()
            )
        }
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val taskProvider: TaskProvider<DependencyUpdatesTask> = mockk()
        val dependencyTask: DependencyUpdatesTask = mockk()
        val strategy: ResolutionStrategyWithCurrent = mockk()
        val ruledSelection: ComponentSelectionRulesWithCurrent = mockk()
        val selection: ComponentSelectionWithCurrent = mockk()

        every { project.tasks } returns tasks
        every {
            tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java)
        } returns taskProvider
        every { selection.reject(any()) } just Runs

        invokeGradleAction(
            { probe -> taskProvider.configure(probe) },
            dependencyTask,
            mockk()
        )

        invokeGradleAction(
            { probe -> dependencyTask.resolutionStrategy(probe) },
            strategy,
            mockk()
        )

        invokeGradleAction(
            { probe -> strategy.componentSelection(probe) },
            ruledSelection,
            mockk()
        )

        invokeGradleAction(
            { probe -> ruledSelection.all(probe) },
            selection,
            mockk()
        )

        every { selection.currentVersion } returns currentVersion
        every { selection.candidate.version } returns candidateVersion

        // When
        DependencyUpdate.configure(
            project,
            config
        )

        // Then
        verify(exactly = 0) { selection.reject("Release candidate") }
    }

    @Test
    fun `Given configure is called with a Project and DependencyExtension, it accepts if the current version is not stable and the candidate is`() {
        // Given
        val currentVersion = "lmp-Y"
        val candidateVersion = "xyz-B"

        val config = object : DependencyContract.DependencyPluginExtension {
            override val keywords: SetProperty<String> = GradlePropertyBuilder.makeSetProperty(
                String::class.java,
                setOf("A", "B", "C")
            )
            override val versionRegex: Property<Regex> = GradlePropertyBuilder.makeProperty(
                Regex::class.java,
                "xyz.*".toRegex()
            )
        }
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val taskProvider: TaskProvider<DependencyUpdatesTask> = mockk()
        val dependencyTask: DependencyUpdatesTask = mockk()
        val strategy: ResolutionStrategyWithCurrent = mockk()
        val ruledSelection: ComponentSelectionRulesWithCurrent = mockk()
        val selection: ComponentSelectionWithCurrent = mockk()

        every { project.tasks } returns tasks
        every {
            tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java)
        } returns taskProvider
        every { selection.reject(any()) } just Runs

        invokeGradleAction(
            { probe -> taskProvider.configure(probe) },
            dependencyTask,
            mockk()
        )

        invokeGradleAction(
            { probe -> dependencyTask.resolutionStrategy(probe) },
            strategy,
            mockk()
        )

        invokeGradleAction(
            { probe -> strategy.componentSelection(probe) },
            ruledSelection,
            mockk()
        )

        invokeGradleAction(
            { probe -> ruledSelection.all(probe) },
            selection,
            mockk()
        )

        every { selection.currentVersion } returns currentVersion
        every { selection.candidate.version } returns candidateVersion

        // When
        DependencyUpdate.configure(
            project,
            config
        )

        // Then
        verify(exactly = 0) { selection.reject("Release candidate") }
    }

    @Test
    fun `Given configure is called with a Project and DependencyExtension, it accepts if the current and candidate version are not stable`() {
        // Given
        val currentVersion = "lmp-Y"
        val candidateVersion = "lmp-Y"

        val config = object : DependencyContract.DependencyPluginExtension {
            override val keywords: SetProperty<String> = GradlePropertyBuilder.makeSetProperty(
                String::class.java,
                setOf("A", "B", "C")
            )
            override val versionRegex: Property<Regex> = GradlePropertyBuilder.makeProperty(
                Regex::class.java,
                "xyz.*".toRegex()
            )
        }
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val taskProvider: TaskProvider<DependencyUpdatesTask> = mockk()
        val dependencyTask: DependencyUpdatesTask = mockk()
        val strategy: ResolutionStrategyWithCurrent = mockk()
        val ruledSelection: ComponentSelectionRulesWithCurrent = mockk()
        val selection: ComponentSelectionWithCurrent = mockk()

        every { project.tasks } returns tasks
        every {
            tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java)
        } returns taskProvider
        every { selection.reject(any()) } just Runs

        invokeGradleAction(
            { probe -> taskProvider.configure(probe) },
            dependencyTask,
            mockk()
        )

        invokeGradleAction(
            { probe -> dependencyTask.resolutionStrategy(probe) },
            strategy,
            mockk()
        )

        invokeGradleAction(
            { probe -> strategy.componentSelection(probe) },
            ruledSelection,
            mockk()
        )

        invokeGradleAction(
            { probe -> ruledSelection.all(probe) },
            selection,
            mockk()
        )

        every { selection.currentVersion } returns currentVersion
        every { selection.candidate.version } returns candidateVersion

        // When
        DependencyUpdate.configure(
            project,
            config
        )

        // Then
        verify(exactly = 0) { selection.reject("Release candidate") }
    }
}
