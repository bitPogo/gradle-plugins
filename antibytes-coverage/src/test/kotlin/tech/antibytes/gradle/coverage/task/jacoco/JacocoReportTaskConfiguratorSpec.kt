/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
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
import io.mockk.slot
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.reporting.DirectoryReport
import org.gradle.api.reporting.SingleFileReport
import org.gradle.api.tasks.TaskContainer
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportsContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.api.AndroidJacocoConfiguration
import tech.antibytes.gradle.coverage.api.JacocoReporterSettings
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.task.TaskContract
import tech.antibytes.gradle.test.invokeGradleAction
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class JacocoReportTaskConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils ReportTaskConfigurator`() {
        val configurator: Any = JacocoReportTaskConfigurator

        assertTrue(configurator is TaskContract.ReportTaskConfigurator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it adds a CoverageTask for Jvm and returns it`() {
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
            verificationRules = emptySet()
        )

        val tasks: TaskContainer = mockk()
        val testTasks1: Task = mockk()
        val testTasks2: Task = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val reports: JacocoReportsContainer = mockk()
        val html: DirectoryReport = mockk()
        val xml: SingleFileReport = mockk()
        val csv: SingleFileReport = mockk()

        val isHtmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isXmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isCsvUsed: Property<Boolean> = mockk(relaxed = true)
        val outputDir: DirectoryProperty = mockk(relaxUnitFun = true)
        val outputFile: RegularFileProperty = mockk()

        val buildDir: File = mockk()
        val projectDir: File = mockk()
        val buildDirLayout: DirectoryProperty = mockk()

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val testDependencies = configuration.testDependencies.toList()

        val htmlDir: File = mockk()
        val csvFile: File = mockk()
        val xmlFile: File = mockk()

        every { project.tasks } returns tasks
        every { project.buildDir } returns buildDir
        every { project.projectDir } returns projectDir
        every { project.name } returns projectName

        every { tasks.findByName(testDependencies[0]) } returns testTasks1
        every { tasks.findByName(testDependencies[1]) } returns testTasks2

        invokeGradleAction(
            { probe -> tasks.create("${contextId}Coverage", JacocoReport::class.java, probe) },
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
                testDependencies.map { name -> "jacoco/$name.exec" }.toSet()
            )
        } returns mockk()

        invokeGradleAction(
            { probe -> jacocoTask.reports(probe) },
            reports,
            reports
        )

        every { reports.html } returns html
        every { html.required } returns isHtmlUsed
        every { html.outputLocation } returns outputDir

        every { reports.xml } returns xml
        every { xml.required } returns isXmlUsed
        every { xml.outputLocation } returns outputFile

        every { reports.csv } returns csv
        every { csv.required } returns isCsvUsed
        every { csv.outputLocation } returns outputFile

        every { outputDir.set(any<File>()) } just Runs
        every { outputFile.set(any<File>()) } just Runs

        every { project.layout.buildDirectory } returns buildDirLayout
        every {
            buildDirLayout
                .dir("reports/jacoco/$contextId/$projectName")
                .get()
                .asFile
        } returns htmlDir

        every {
            buildDirLayout
                .file("reports/jacoco/$contextId/$projectName.csv")
                .get()
                .asFile
        } returns csvFile

        every {
            buildDirLayout
                .file("reports/jacoco/$contextId/$projectName.xml")
                .get()
                .asFile
        } returns xmlFile

        // When
        val reporter = JacocoReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = reporter,
            expected = jacocoTask
        )

        verify(exactly = 1) { tasks.create("${contextId}Coverage", JacocoReport::class.java, any()) }

        assertEquals(
            actual = dependencies.captured,
            expected = setOf(testTasks1, testTasks2)
        )
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Generate coverage reports for ${contextId.capitalize()}." }

        verify(exactly = 1) { classDirectories.setFrom(classFiles) }
        verify(exactly = 1) { sourceDirectories.setFrom(configuration.sources) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(configuration.additionalClasses) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(configuration.additionalSources) }
        verify(exactly = 1) { executionData.setFrom(executionFiles) }

        verify(exactly = 1) { isHtmlUsed.set(configuration.reportSettings.useHtml) }
        verify(exactly = 1) { isXmlUsed.set(configuration.reportSettings.useXml) }
        verify(exactly = 1) { isCsvUsed.set(configuration.reportSettings.useCsv) }

        verify(exactly = 1) { outputDir.set(htmlDir) }
        verify(exactly = 1) { outputFile.set(xmlFile) }
        verify(exactly = 1) { outputFile.set(csvFile) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it adds a CoverageTask for Jvm, while filtering unregistered Dependencies`() {
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
            verificationRules = emptySet()
        )

        val tasks: TaskContainer = mockk()
        val testTasks1: Task = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)
        val buildDir: File = mockk()
        val projectDir: File = mockk()

        val testDependencies = configuration.testDependencies.toList()

        every { project.tasks } returns tasks
        every { project.buildDir } returns buildDir
        every { project.projectDir } returns projectDir
        every { project.name } returns projectName

        every { tasks.findByName(testDependencies[0]) } returns testTasks1
        every { tasks.findByName(testDependencies[1]) } returns null

        invokeGradleAction(
            { probe -> tasks.create("${contextId}Coverage", JacocoReport::class.java, probe) },
            jacocoTask,
            jacocoTask
        )

        // please note there is a bug in Mockk which prevents to simply verify the provided arguments
        val dependencies: CapturingSlot<Set<Task>> = slot()
        every { jacocoTask.setDependsOn(capture(dependencies)) } just Runs

        invokeGradleAction(
            { probe -> project.fileTree(projectDir, probe) },
            mockk<ConfigurableFileTree>(relaxed = true),
            mockk()
        )
        invokeGradleAction(
            { probe -> project.fileTree(buildDir, probe) },
            mockk<ConfigurableFileTree>(relaxed = true),
            mockk()
        )

        // When
        JacocoReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        verify(exactly = 1) { tasks.create("${contextId}Coverage", JacocoReport::class.java, any()) }

        assertEquals(
            actual = dependencies.captured,
            expected = setOf(testTasks1)
        )
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it adds a CoverageTask for Android and returns it`() {
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
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            flavour = fixture(),
            variant = fixture()
        )

        val tasks: TaskContainer = mockk()
        val testTasks1: Task = mockk()
        val testTasks2: Task = mockk()
        val instrumentedTestTasks1: Task = mockk()
        val instrumentedTestTasks2: Task = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val reports: JacocoReportsContainer = mockk()
        val html: DirectoryReport = mockk()
        val xml: SingleFileReport = mockk()
        val csv: SingleFileReport = mockk()

        val isHtmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isXmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isCsvUsed: Property<Boolean> = mockk(relaxed = true)
        val outputDir: DirectoryProperty = mockk(relaxUnitFun = true)
        val outputFile: RegularFileProperty = mockk()

        val buildDir: File = mockk()
        val projectDir: File = mockk()
        val buildDirLayout: DirectoryProperty = mockk()

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val testDependencies = configuration.testDependencies.toList()
        val instrumentedTestDependencies = configuration.instrumentedTestDependencies.toList()

        val htmlDir: File = mockk()
        val csvFile: File = mockk()
        val xmlFile: File = mockk()

        every { project.tasks } returns tasks
        every { project.buildDir } returns buildDir
        every { project.projectDir } returns projectDir
        every { project.name } returns projectName

        every { tasks.findByName(testDependencies[0]) } returns testTasks1
        every { tasks.findByName(testDependencies[1]) } returns testTasks2
        every { tasks.findByName(instrumentedTestDependencies[0]) } returns instrumentedTestTasks1
        every { tasks.findByName(instrumentedTestDependencies[1]) } returns instrumentedTestTasks2

        invokeGradleAction(
            { probe -> tasks.create("${contextId}Coverage", JacocoReport::class.java, probe) },
            jacocoTask,
            jacocoTask
        )

        // please note there is a bug in Mockk which prevents to simply verify the provided arguments
        val dependencies: CapturingSlot<Set<Task>> = slot()
        every { jacocoTask.setDependsOn(capture(dependencies)) } just Runs

        every { jacocoTask.sourceDirectories } returns sourceDirectories
        every { jacocoTask.classDirectories } returns classDirectories
        every { jacocoTask.additionalSourceDirs } returns additionalSourceDirectories
        every { jacocoTask.additionalClassDirs } returns additionalClassesDirectories
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
                testDependencies
                    .map { name -> "jacoco${File.separator}$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.flavour}${configuration.variant.capitalize()}UnitTest/test${configuration.flavour.capitalize()}${configuration.variant.capitalize()}UnitTest.exec"
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.flavour}${configuration.variant.capitalize()}AndroidTest/**/*coverage.ec"
                        )
                        it.add("jacoco/${configuration.flavour}${configuration.variant.capitalize()}.exec")
                        it.add("jacoco/jacoco.exec")
                    }
            )
        } returns mockk()

        invokeGradleAction(
            { probe -> jacocoTask.reports(probe) },
            reports,
            reports
        )

        every { reports.html } returns html
        every { html.required } returns isHtmlUsed
        every { html.outputLocation } returns outputDir

        every { reports.xml } returns xml
        every { xml.required } returns isXmlUsed
        every { xml.outputLocation } returns outputFile

        every { reports.csv } returns csv
        every { csv.required } returns isCsvUsed
        every { csv.outputLocation } returns outputFile

        every { outputDir.set(any<File>()) } just Runs
        every { outputFile.set(any<File>()) } just Runs

        every { project.layout.buildDirectory } returns buildDirLayout
        every {
            buildDirLayout
                .dir("reports/jacoco/$contextId/$projectName")
                .get()
                .asFile
        } returns htmlDir

        every {
            buildDirLayout
                .file("reports/jacoco/$contextId/$projectName.csv")
                .get()
                .asFile
        } returns csvFile

        every {
            buildDirLayout
                .file("reports/jacoco/$contextId/$projectName.xml")
                .get()
                .asFile
        } returns xmlFile

        // When
        val reporter = JacocoReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = reporter,
            expected = jacocoTask
        )

        verify(exactly = 1) { tasks.create("${contextId}Coverage", JacocoReport::class.java, any()) }

        assertEquals(
            actual = dependencies.captured,
            expected = setOf(testTasks1, testTasks2, instrumentedTestTasks1, instrumentedTestTasks2)
        )
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Generate coverage reports for ${contextId.capitalize()}." }

        verify(exactly = 1) { classDirectories.setFrom(classFiles) }
        verify(exactly = 1) { sourceDirectories.setFrom(configuration.sources) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(configuration.additionalClasses) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(configuration.additionalSources) }
        verify(exactly = 1) { executionData.setFrom(executionFiles) }

        verify(exactly = 1) { isHtmlUsed.set(configuration.reportSettings.useHtml) }
        verify(exactly = 1) { isXmlUsed.set(configuration.reportSettings.useXml) }
        verify(exactly = 1) { isCsvUsed.set(configuration.reportSettings.useCsv) }

        verify(exactly = 1) { outputDir.set(htmlDir) }
        verify(exactly = 1) { outputFile.set(xmlFile) }
        verify(exactly = 1) { outputFile.set(csvFile) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it adds a CoverageTask for Android, while filtering unregistered Dependencies`() {
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
            verificationRules = emptySet(),
            flavour = fixture(),
            variant = fixture()
        )

        val tasks: TaskContainer = mockk()
        val testTasks1: Task = mockk()
        val instrumentedTestTasks1: Task = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)
        val buildDir: File = mockk()
        val projectDir: File = mockk()

        val testDependencies = configuration.testDependencies.toList()
        val instrumentedTestDependencies = configuration.instrumentedTestDependencies.toList()

        every { project.tasks } returns tasks
        every { project.buildDir } returns buildDir
        every { project.projectDir } returns projectDir
        every { project.name } returns projectName

        every { tasks.findByName(testDependencies[0]) } returns testTasks1
        every { tasks.findByName(testDependencies[1]) } returns null
        every { tasks.findByName(instrumentedTestDependencies[0]) } returns null
        every { tasks.findByName(instrumentedTestDependencies[1]) } returns instrumentedTestTasks1

        invokeGradleAction(
            { probe -> tasks.create("${contextId}Coverage", JacocoReport::class.java, probe) },
            jacocoTask,
            jacocoTask
        )

        // please note there is a bug in Mockk which prevents to simply verify the provided arguments
        val dependencies: CapturingSlot<Set<Task>> = slot()
        every { jacocoTask.setDependsOn(capture(dependencies)) } just Runs

        invokeGradleAction(
            { probe -> project.fileTree(projectDir, probe) },
            mockk<ConfigurableFileTree>(relaxed = true),
            mockk()
        )
        invokeGradleAction(
            { probe -> project.fileTree(buildDir, probe) },
            mockk<ConfigurableFileTree>(relaxed = true),
            mockk()
        )

        // When
        JacocoReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        verify(exactly = 1) { tasks.create("${contextId}Coverage", JacocoReport::class.java, any()) }

        assertEquals(
            actual = dependencies.captured,
            expected = setOf(testTasks1, instrumentedTestTasks1)
        )
    }
}
