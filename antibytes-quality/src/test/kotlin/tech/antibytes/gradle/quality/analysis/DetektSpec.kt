/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.analysis

import com.appmattus.kotlinfixture.kotlinFixture
import io.gitlab.arturbosch.detekt.Detekt as DetektTask
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.tasks.TaskCollection
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.quality.AntibytesQualityExtension
import tech.antibytes.gradle.quality.QualityContract
import tech.antibytes.gradle.quality.api.CodeAnalysisConfiguration
import tech.antibytes.gradle.quality.api.DetektReport
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.test.invokeGradleAction

class DetektSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Configurator`() {
        val configurator: Any = Detekt

        assertTrue(configurator is QualityContract.Configurator)
    }

    @Test
    fun `Given configure is called and no analysis Config was given it does nothing`() {
        // Given
        val project: Project = mockk()
        val extension = createExtension<AntibytesQualityExtension>()

        extension.codeAnalysis.set(null)

        // When
        Detekt.configure(project, extension)
    }

    @Test
    fun `Given configure is called with an analysis Config it configures the detekt extension`() {
        // Given
        val project: Project = mockk()
        val extension = createExtension<AntibytesQualityExtension>()

        val configuration = CodeAnalysisConfiguration(
            jvmVersion = fixture(),
            autoCorrection = fixture(),
            exclude = fixture(),
            excludeBaseline = fixture(),
            reports = DetektReport(
                html = fixture(),
                xml = fixture(),
                sarif = fixture(),
                txt = fixture(),
            ),
            configurationFiles = mockk(),
            baselineFile = mockk(),
            sourceFiles = mockk(),
        )
        val detekt: DetektExtension = mockk(relaxed = true)

        extension.codeAnalysis.set(configuration)

        every { project.plugins.apply(any()) } returns mockk()
        every { project.tasks.withType(DetektTask::class.java) } returns mockk(relaxed = true)
        every { project.tasks.withType(DetektCreateBaselineTask::class.java) } returns mockk(relaxed = true)

        invokeGradleAction(
            { probe -> project.extensions.configure(DetektExtension::class.java, probe) },
            detekt,
            detekt,
        )
        // When
        Detekt.configure(project, extension)

        // Then
        verify(exactly = 1) { project.plugins.apply("io.gitlab.arturbosch.detekt") }
        verify(exactly = 1) { detekt.toolVersion = "1.21.0" }
        verify(exactly = 1) { detekt.config = configuration.configurationFiles }
        verify(exactly = 1) { detekt.baseline = configuration.baselineFile }
        verify(exactly = 1) { detekt.source = configuration.sourceFiles }
        verify(exactly = 1) { detekt.autoCorrect = configuration.autoCorrection }
        verify(exactly = 1) { detekt.allRules = false }
        verify(exactly = 1) { detekt.buildUponDefaultConfig = true }
    }

    @Test
    fun `Given configure is called with an analysis Config it configures the DetektTask`() {
        // Given
        val project: Project = mockk()
        val extension = createExtension<AntibytesQualityExtension>()

        val configuration = CodeAnalysisConfiguration(
            jvmVersion = fixture(),
            autoCorrection = fixture(),
            exclude = fixture(),
            excludeBaseline = fixture(),
            reports = DetektReport(
                html = fixture(),
                xml = fixture(),
                sarif = fixture(),
                txt = fixture(),
            ),
            configurationFiles = mockk(),
            baselineFile = mockk(),
            sourceFiles = mockk(),
        )
        val collection: TaskCollection<DetektTask> = mockk()
        val detektTask: DetektTask = mockk()
        val excluded: MutableSet<String> = mutableSetOf()

        extension.codeAnalysis.set(configuration)

        every { project.plugins.apply(any()) } returns mockk()
        every { project.tasks.withType(DetektCreateBaselineTask::class.java) } returns mockk(relaxed = true)
        every { project.tasks.withType(DetektTask::class.java) } returns collection
        every {
            project.extensions.configure(any<Class<DetektExtension>>(), any())
        } returns mockk(relaxed = true)

        invokeGradleAction(
            { probe -> collection.configureEach(probe) },
            detektTask,
        )

        every { detektTask.jvmTarget = any() } just Runs
        every { detektTask.exclude(*anyVararg()) } answers {
            @Suppress("UNCHECKED_CAST")
            excluded.addAll(this.args[0] as Array<String>)
            mockk()
        }
        every { detektTask.reports.html.required.set(any<Boolean>()) } just Runs
        every { detektTask.reports.xml.required.set(any<Boolean>()) } just Runs
        every { detektTask.reports.sarif.required.set(any<Boolean>()) } just Runs
        every { detektTask.reports.txt.required.set(any<Boolean>()) } just Runs

        // When
        Detekt.configure(project, extension)

        // Then
        assertEquals(
            actual = excluded,
            expected = configuration.exclude,
        )

        verify(exactly = 1) { project.plugins.apply("io.gitlab.arturbosch.detekt") }
        verify(exactly = 1) { detektTask.jvmTarget = configuration.jvmVersion }
        verify(exactly = 1) { detektTask.reports.html.required.set(configuration.reports.html) }
        verify(exactly = 1) { detektTask.reports.xml.required.set(configuration.reports.xml) }
        verify(exactly = 1) { detektTask.reports.sarif.required.set(configuration.reports.sarif) }
        verify(exactly = 1) { detektTask.reports.txt.required.set(configuration.reports.txt) }
    }

    @Test
    fun `Given configure is called with an analysis Config it configures the DetektBaselineTask`() {
        // Given
        val project: Project = mockk()
        val extension = createExtension<AntibytesQualityExtension>()

        val configuration = CodeAnalysisConfiguration(
            jvmVersion = fixture(),
            autoCorrection = fixture(),
            exclude = fixture(),
            excludeBaseline = fixture(),
            reports = DetektReport(
                html = fixture(),
                xml = fixture(),
                sarif = fixture(),
                txt = fixture(),
            ),
            configurationFiles = mockk(),
            baselineFile = mockk(),
            sourceFiles = mockk(),
        )
        val collection: TaskCollection<DetektCreateBaselineTask> = mockk()
        val detektTask: DetektCreateBaselineTask = mockk()
        val excluded: MutableSet<String> = mutableSetOf()

        extension.codeAnalysis.set(configuration)

        every { project.plugins.apply(any()) } returns mockk()
        every { project.tasks.withType(DetektCreateBaselineTask::class.java) } returns collection
        every { project.tasks.withType(DetektTask::class.java) } returns mockk(relaxed = true)
        every {
            project.extensions.configure(any<Class<DetektExtension>>(), any())
        } returns mockk(relaxed = true)

        invokeGradleAction(
            { probe -> collection.configureEach(probe) },
            detektTask,
        )

        every { detektTask.jvmTarget = any() } just Runs
        every { detektTask.exclude(*anyVararg()) } answers {
            @Suppress("UNCHECKED_CAST")
            excluded.addAll(this.args[0] as Array<String>)
            mockk()
        }

        // When
        Detekt.configure(project, extension)

        // Then
        assertEquals(
            actual = excluded,
            expected = configuration.excludeBaseline,
        )

        verify(exactly = 1) { project.plugins.apply("io.gitlab.arturbosch.detekt") }
        verify(exactly = 1) { detektTask.jvmTarget = configuration.jvmVersion }
    }
}
