/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.docs

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.File
import kotlin.test.assertTrue
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.tasks.TaskCollection
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.GradleDokkaSourceSetBuilder
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.ConfigurationContract
import tech.antibytes.gradle.test.invokeGradleAction

class DokkaConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Configurator`() {
        val configurator: Any = DokkaConfigurator

        assertTrue(configurator is ConfigurationContract.Configurator<*>)
    }

    @Test
    fun `Given configure is called it preconfigures the Dokka Task`() {
        // Given
        val project: Project = mockk()
        val dokkaTask: DokkaTask = mockk(relaxed = true)
        val dokkaBuilder: GradleDokkaSourceSetBuilder = mockk(relaxed = true)
        val buildDir = File(fixture<String>())
        val taskCollection: TaskCollection<DokkaTask> = mockk()
        val builderContainer: NamedDomainObjectContainer<GradleDokkaSourceSetBuilder> = mockk()

        every { project.buildDir } returns buildDir
        every { project.tasks.withType(DokkaTask::class.java) } returns taskCollection
        every { dokkaTask.dokkaSourceSets } returns builderContainer

        invokeGradleAction(
            { probe -> taskCollection.configureEach(probe) },
            dokkaTask,
        )

        invokeGradleAction(
            { probe -> builderContainer.configureEach(probe) },
            dokkaBuilder,
        )

        // When
        DokkaConfigurator.configure(project, Any())

        // Then
        verify(exactly = 1) { dokkaTask.outputDirectory.set(buildDir.resolve("dokka")) }
        verify(exactly = 1) { dokkaTask.offlineMode.set(true) }
        verify(exactly = 1) { dokkaTask.suppressObviousFunctions.set(true) }
        verify(exactly = 1) { dokkaBuilder.reportUndocumented.set(true) }
        verify(exactly = 1) { dokkaBuilder.skipEmptyPackages.set(true) }
        verify(exactly = 1) { dokkaBuilder.noStdlibLink.set(false) }
        verify(exactly = 1) { dokkaBuilder.noJdkLink.set(false) }
        verify(exactly = 1) { dokkaBuilder.noAndroidSdkLink.set(false) }
        verify(exactly = 1) { dokkaBuilder.jdkVersion.set(8) }
    }
}
