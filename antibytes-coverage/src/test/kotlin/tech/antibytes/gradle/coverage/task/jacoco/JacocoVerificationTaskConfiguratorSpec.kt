/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.tasks.TaskContainer
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRulesContainer
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.coverage.api.AndroidJacocoConfiguration
import tech.antibytes.gradle.coverage.api.JacocoReporterSettings
import tech.antibytes.gradle.coverage.api.JacocoVerificationRule
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.task.TaskContract
import tech.antibytes.gradle.test.invokeGradleAction
import java.io.File
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class JacocoVerificationTaskConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        mockkObject(JacocoVerificationRuleMapper)
    }

    @After
    fun tearDown() {
        unmockkObject(JacocoVerificationRuleMapper)
    }

    @Test
    fun `It fulfils VerificationTaskConfigurator`() {
        val configurator: Any = JacocoVerificationTaskConfigurator

        assertTrue(configurator is TaskContract.VerificationTaskConfigurator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it does nothing if no VerificationRules had been given`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture()
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = mockk(),
            additionalSources = mockk(),
            additionalClasses = mockk(),
            verificationRules = emptySet()
        )

        // When
        val verificator = JacocoVerificationTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it filters invalid VerificationRules`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture()
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = mockk(),
            additionalSources = mockk(),
            additionalClasses = mockk(),
            verificationRules = setOf(JacocoVerificationRule())
        )

        // When
        val verificator = JacocoVerificationTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it adds a CoverageVerificationTask for Jvm`() {
        // Given
        val project: Project = mockk()
        val projectName: String = fixture()
        val contextId: String = fixture()
        val configuration = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture()
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = mockk(),
            additionalSources = mockk(),
            additionalClasses = mockk(),
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = fixture<BigDecimal>()
                )
            )
        )

        val tasks: TaskContainer = mockk()
        val reportTask: JacocoReport = mockk()

        val jacocoTask: JacocoCoverageVerification = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val violationRules: JacocoViolationRulesContainer = mockk(relaxed = true)

        val buildDir: File = mockk()
        val projectDir: File = mockk()

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        every { project.tasks } returns tasks
        every { project.buildDir } returns buildDir
        every { project.projectDir } returns projectDir
        every { project.name } returns projectName

        every { tasks.getByName("${contextId}Coverage") } returns reportTask

        invokeGradleAction(
            { probe ->
                tasks.create(
                    "${contextId}CoverageVerification",
                    JacocoCoverageVerification::class.java,
                    probe
                )
            },
            jacocoTask,
            jacocoTask
        )

        // please note there is a bug in Mockk which prevents to simply verify the provided arguments
        val dependencies: CapturingSlot<Set<Task>> = slot()
        every { jacocoTask.setDependsOn(capture(dependencies)) } just Runs

        every { jacocoTask.sourceDirectories } returns sourceDirectories
        every { jacocoTask.classDirectories } returns classDirectories
        every { jacocoTask.additionalClassDirs } returns additionalClassesDirectories
        every { jacocoTask.additionalSourceDirs } returns additionalSourceDirectories
        every { jacocoTask.executionData } returns executionData

        invokeGradleAction(
            { probe -> project.fileTree(projectDir, probe) },
            fileTreeClassFiles,
            classFiles
        )

        every { fileTreeClassFiles.setExcludes(configuration.classFilter) } returns mockk()
        every { fileTreeClassFiles.setIncludes(configuration.classPattern) } returns mockk()

        invokeGradleAction(
            { probe -> project.fileTree(buildDir, probe) },
            fileTreeExecutionFiles,
            executionFiles
        )

        every {
            fileTreeExecutionFiles.setIncludes(
                configuration.testDependencies.map { name -> "jacoco/$name.exec" }.toSet()
            )
        } returns mockk()

        invokeGradleAction(
            { probe -> jacocoTask.violationRules(probe) },
            violationRules,
            mockk()
        )

        every { JacocoVerificationRuleMapper.map(violationRules, configuration.verificationRules) } just Runs

        // When
        val verificator = JacocoVerificationTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = verificator,
            expected = jacocoTask
        )

        verify(exactly = 1) {
            tasks.create("${contextId}CoverageVerification", JacocoCoverageVerification::class.java, any())
        }

        assertEquals(
            actual = dependencies.captured,
            expected = setOf(reportTask)
        )
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) {
            jacocoTask.description = "Verifies the coverage reports against a given set of rules for ${contextId.capitalize()}."
        }

        verify(exactly = 1) { classDirectories.setFrom(classFiles) }
        verify(exactly = 1) { sourceDirectories.setFrom(configuration.sources) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(configuration.additionalClasses) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(configuration.additionalSources) }
        verify(exactly = 1) { executionData.setFrom(executionFiles) }

        verify(exactly = 1) { violationRules.isFailOnViolation = true }
        verify(exactly = 1) { JacocoVerificationRuleMapper.map(violationRules, configuration.verificationRules) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it adds a CoverageVerificationTask for Android`() {
        // Given
        val project: Project = mockk()
        val projectName: String = fixture()
        val contextId: String = fixture()
        val configuration = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture()
            ),
            testDependencies = setOf(fixture(), fixture()),
            instrumentedTestDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = mockk(),
            additionalSources = mockk(),
            additionalClasses = mockk(),
            verificationRules = setOf(
                JacocoVerificationRule(
                    maximum = fixture<BigDecimal>()
                )
            ),
            flavour = "",
            variant = fixture()
        )

        val tasks: TaskContainer = mockk()
        val reportTask: JacocoReport = mockk()

        val jacocoTask: JacocoCoverageVerification = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val violationRules: JacocoViolationRulesContainer = mockk(relaxed = true)

        val buildDir: File = mockk()
        val projectDir: File = mockk()

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        every { project.tasks } returns tasks
        every { project.buildDir } returns buildDir
        every { project.projectDir } returns projectDir
        every { project.name } returns projectName

        every { tasks.getByName("${contextId}Coverage") } returns reportTask

        invokeGradleAction(
            { probe ->
                tasks.create(
                    "${contextId}CoverageVerification",
                    JacocoCoverageVerification::class.java,
                    probe
                )
            },
            jacocoTask,
            jacocoTask
        )

        // please note there is a bug in Mockk which prevents to simply verify the provided arguments
        val dependencies: CapturingSlot<Set<Task>> = slot()
        every { jacocoTask.setDependsOn(capture(dependencies)) } just Runs

        every { jacocoTask.sourceDirectories } returns sourceDirectories
        every { jacocoTask.classDirectories } returns classDirectories
        every { jacocoTask.additionalClassDirs } returns additionalClassesDirectories
        every { jacocoTask.additionalSourceDirs } returns additionalSourceDirectories
        every { jacocoTask.executionData } returns executionData

        invokeGradleAction(
            { probe -> project.fileTree(projectDir, probe) },
            fileTreeClassFiles,
            classFiles
        )

        every { fileTreeClassFiles.setExcludes(configuration.classFilter) } returns mockk()
        every { fileTreeClassFiles.setIncludes(configuration.classPattern) } returns mockk()

        invokeGradleAction(
            { probe -> project.fileTree(buildDir, probe) },
            fileTreeExecutionFiles,
            executionFiles
        )

        every {
            fileTreeExecutionFiles.setIncludes(
                configuration.testDependencies
                    .map { name -> "jacoco/$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.variant}UnitTest/test${configuration.variant.capitalize()}UnitTest.exec"
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.variant}AndroidTest/**/*coverage.ec"
                        )
                        it.add("jacoco/${configuration.variant}.exec")
                        it.add("jacoco/jacoco.exec")
                    }
            )
        } returns mockk()

        invokeGradleAction(
            { probe -> jacocoTask.violationRules(probe) },
            violationRules,
            mockk()
        )

        every { JacocoVerificationRuleMapper.map(violationRules, configuration.verificationRules) } just Runs

        // When
        val verificator = JacocoVerificationTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = verificator,
            expected = jacocoTask
        )

        verify(exactly = 1) {
            tasks.create("${contextId}CoverageVerification", JacocoCoverageVerification::class.java, any())
        }

        assertEquals(
            actual = dependencies.captured,
            expected = setOf(reportTask)
        )
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) {
            jacocoTask.description = "Verifies the coverage reports against a given set of rules for ${contextId.capitalize()}."
        }

        verify(exactly = 1) { classDirectories.setFrom(classFiles) }
        verify(exactly = 1) { sourceDirectories.setFrom(configuration.sources) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(configuration.additionalClasses) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(configuration.additionalSources) }
        verify(exactly = 1) { executionData.setFrom(executionFiles) }

        verify(exactly = 1) { violationRules.isFailOnViolation = true }
        verify(exactly = 1) { JacocoVerificationRuleMapper.map(violationRules, configuration.verificationRules) }
    }
}
