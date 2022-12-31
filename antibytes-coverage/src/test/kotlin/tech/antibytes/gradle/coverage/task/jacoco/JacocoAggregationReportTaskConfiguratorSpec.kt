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
import java.io.File
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.reporting.DirectoryReport
import org.gradle.api.reporting.SingleFileReport
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportsContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.AntibytesCoveragePluginExtension
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.api.AndroidJacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.api.AndroidJacocoConfiguration
import tech.antibytes.gradle.coverage.api.JacocoReporterSettings
import tech.antibytes.gradle.coverage.api.JvmJacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.task.TaskContract
import tech.antibytes.gradle.test.GradlePropertyBuilder
import tech.antibytes.gradle.test.invokeGradleAction

class JacocoAggregationReportTaskConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils ReportTaskConfigurator`() {
        val configurator: Any = JacocoAggregationReportTaskConfigurator

        assertTrue(configurator is TaskContract.ReportTaskConfigurator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters non covered Projects`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration()

        val subproject1: Project = mockk()
        val subproject2: Project = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)

        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { project.tasks.create(any(), JacocoReport::class.java, any()) }
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 0) { jacocoTask.setDependsOn(any()) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters non matching ProjectContexts`() {
        // Given
        val project: Project = mockk()
        val subprojectName: String = fixture()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration()

        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject1: Project = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)

        every { project.subprojects } returns setOf(subproject1)

        every { subproject1.name } returns subprojectName
        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject1CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(fixture<String>() to mockk()),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { project.tasks.create(any(), JacocoReport::class.java, any()) }
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 0) { jacocoTask.setDependsOn(any()) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters excluded Projects`() {
        // Given
        val project: Project = mockk()
        val subprojectName: String = fixture()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration(
            exclude = setOf(subprojectName),
        )

        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject1: Project = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)

        every { project.subprojects } returns setOf(subproject1)

        every { subproject1.name } returns subprojectName
        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { project.tasks.create(any(), JacocoReport::class.java, any()) }
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 0) { jacocoTask.setDependsOn(any()) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters non fitting Projects for Jvm`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration()

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            variant = "debug",
            flavour = "",
            instrumentedTestDependencies = emptySet(),
        )

        val subproject1: Project = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject2: Project = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)

        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension

        every { subproject2.name } returns fixture()
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null

        every { subproject1CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { project.tasks.create(any(), JacocoReport::class.java, any()) }
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 0) { jacocoTask.setDependsOn(any()) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it aggregates the Configurations from its Subprojects and adds a ReporterTask for Jvm`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration()

        val subConfiguration1 = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
        )

        val subConfiguration2 = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
        )

        val projectName: String = fixture()
        val projectDir: File = mockk()
        val buildDir: File = mockk()

        val subproject1: Project = mockk()
        val subproject1BuildDir: File = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject1Reporter: JacocoReport = mockk()

        val subproject2: Project = mockk()
        val subproject2BuildDir: File = mockk()
        val subproject2ProjectDir: File = mockk()
        val subproject2CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject2Reporter: JacocoReport = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk()
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val aggregationClassFileTree: ConfigurableFileTree = mockk()
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val buildDirLayout: DirectoryProperty = mockk()

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val reports: JacocoReportsContainer = mockk()
        val html: DirectoryReport = mockk()
        val xml: SingleFileReport = mockk()
        val csv: SingleFileReport = mockk()

        val isHtmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isXmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isCsvUsed: Property<Boolean> = mockk(relaxed = true)
        val outputDir: DirectoryProperty = mockk(relaxUnitFun = true)
        val outputFile: RegularFileProperty = mockk()

        val htmlDir: File = mockk()
        val csvFile: File = mockk()
        val xmlFile: File = mockk()

        every { project.name } returns projectName
        every { project.projectDir } returns projectDir
        every { project.buildDir } returns buildDir
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject2CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir
        every { subproject2.projectDir } returns subproject2ProjectDir

        every { subproject1.buildDir } returns subproject1BuildDir
        every { subproject2.buildDir } returns subproject2BuildDir

        every { subproject1CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )
        every { subproject2CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration2),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // please note there is a bug in Mockk which prevents to simply verify the provided arguments
        val dependencies: CapturingSlot<Set<Task>> = slot()
        every { jacocoTask.setDependsOn(capture(dependencies)) } just Runs

        every { jacocoTask.sourceDirectories } returns sourceDirectories
        every { jacocoTask.classDirectories } returns classDirectories
        every { jacocoTask.additionalClassDirs } returns additionalClassesDirectories
        every { jacocoTask.additionalSourceDirs } returns additionalSourceDirectories
        every { jacocoTask.executionData } returns executionData

        invokeGradleAction(
            fileTreeClassFiles,
            classFiles,
        ) { probe ->
            subproject1.fileTree(subproject1ProjectDir, probe)
        }
        invokeGradleAction(
            fileTreeClassFiles,
            classFiles,
        ) { probe ->
            subproject2.fileTree(subproject2ProjectDir, probe)
        }

        every {
            fileTreeClassFiles.setIncludes(subConfiguration1.classPattern)
        } returns mockk()

        every {
            fileTreeClassFiles.setIncludes(subConfiguration2.classPattern)
        } returns mockk()

        every {
            fileTreeClassFiles.setExcludes(subConfiguration1.classFilter)
        } returns mockk()

        every {
            fileTreeClassFiles.setExcludes(subConfiguration2.classFilter)
        } returns mockk()

        invokeGradleAction(
            fileTreeExecutionFiles,
            executionFiles,
        ) { probe ->
            subproject1.fileTree(subproject1BuildDir, probe)
        }
        invokeGradleAction(
            fileTreeExecutionFiles,
            executionFiles,
        ) { probe ->
            subproject2.fileTree(subproject2BuildDir, probe)
        }

        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration1.testDependencies.map { name -> "jacoco/$name.exec" }.toSet(),
            )
        } returns mockk()
        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration2.testDependencies.map { name -> "jacoco/$name.exec" }.toSet(),
            )
        } returns mockk()

        every { subproject1.tasks.getByName("${contextId}Coverage") } returns subproject1Reporter
        every { subproject2.tasks.getByName("${contextId}Coverage") } returns subproject2Reporter

        invokeGradleAction(
            reports,
            reports,
        ) { probe ->
            jacocoTask.reports(probe)
        }

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

        every {
            sourceDirectories.setFrom(
                subConfiguration1.sources.toMutableSet().also {
                    it.addAll(subConfiguration2.sources)
                },
            )
        } just Runs

        every {
            subConfiguration1.additionalClasses!!.plus(subConfiguration2.additionalClasses!!)
        } returns aggregationClassFileTree

        every {
            additionalClassesDirectories.setFrom(aggregationClassFileTree)
        } just Runs

        every {
            additionalClassesDirectories.setFrom(
                subConfiguration2.additionalClasses,
            )
        } just Runs

        every {
            additionalSourceDirectories.setFrom(
                subConfiguration1.additionalSources.toMutableSet().also {
                    it.addAll(subConfiguration2.additionalSources)
                },
            )
        } just Runs

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 1) { jacocoTask.setDependsOn(setOf(subproject1Reporter, subproject2Reporter)) }

        verify(exactly = 1) { classDirectories.setFrom(setOf(classFiles)) }
        verify(exactly = 1) { sourceDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(any()) }
        verify(exactly = 1) { executionData.setFrom(setOf(executionFiles)) }

        verify(exactly = 1) { isHtmlUsed.set(configuration.reportSettings.useHtml) }
        verify(exactly = 1) { isXmlUsed.set(configuration.reportSettings.useXml) }
        verify(exactly = 1) { isCsvUsed.set(configuration.reportSettings.useCsv) }

        verify(exactly = 1) { outputDir.set(htmlDir) }
        verify(exactly = 1) { outputFile.set(xmlFile) }
        verify(exactly = 1) { outputFile.set(csvFile) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it aggregates the Configurations from its Subprojects while filtering additional Classes for Jvm`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration()

        val subConfiguration1 = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = null,
            verificationRules = emptySet(),
        )

        val subConfiguration2 = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = null,
            verificationRules = emptySet(),
        )

        val projectName: String = fixture()
        val projectDir: File = mockk()
        val buildDir: File = mockk()

        val subproject1: Project = mockk()
        val subproject1BuildDir: File = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject1Reporter: JacocoReport = mockk()

        val subproject2: Project = mockk()
        val subproject2BuildDir: File = mockk()
        val subproject2ProjectDir: File = mockk()
        val subproject2CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject2Reporter: JacocoReport = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk()
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val buildDirLayout: DirectoryProperty = mockk()

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val reports: JacocoReportsContainer = mockk()
        val html: DirectoryReport = mockk()
        val xml: SingleFileReport = mockk()
        val csv: SingleFileReport = mockk()

        val isHtmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isXmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isCsvUsed: Property<Boolean> = mockk(relaxed = true)
        val outputDir: DirectoryProperty = mockk(relaxUnitFun = true)
        val outputFile: RegularFileProperty = mockk()

        val htmlDir: File = mockk()
        val csvFile: File = mockk()
        val xmlFile: File = mockk()

        every { project.name } returns projectName
        every { project.projectDir } returns projectDir
        every { project.buildDir } returns buildDir
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject2CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir
        every { subproject2.projectDir } returns subproject2ProjectDir

        every { subproject1.buildDir } returns subproject1BuildDir
        every { subproject2.buildDir } returns subproject2BuildDir

        every { subproject1CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )
        every { subproject2CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration2),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // please note there is a bug in Mockk which prevents to simply verify the provided arguments
        val dependencies: CapturingSlot<Set<Task>> = slot()
        every { jacocoTask.setDependsOn(capture(dependencies)) } just Runs

        every { jacocoTask.sourceDirectories } returns sourceDirectories
        every { jacocoTask.classDirectories } returns classDirectories
        every { jacocoTask.additionalClassDirs } returns additionalClassesDirectories
        every { jacocoTask.additionalSourceDirs } returns additionalSourceDirectories
        every { jacocoTask.executionData } returns executionData

        invokeGradleAction(
            fileTreeClassFiles,
            classFiles,
        ) { probe ->
            subproject1.fileTree(subproject1ProjectDir, probe)
        }
        invokeGradleAction(
            fileTreeClassFiles,
            classFiles,
        ) { probe ->
            subproject2.fileTree(subproject2ProjectDir, probe)
        }

        every {
            fileTreeClassFiles.setIncludes(subConfiguration1.classPattern)
        } returns mockk()

        every {
            fileTreeClassFiles.setIncludes(subConfiguration2.classPattern)
        } returns mockk()

        every {
            fileTreeClassFiles.setExcludes(subConfiguration1.classFilter)
        } returns mockk()

        every {
            fileTreeClassFiles.setExcludes(subConfiguration2.classFilter)
        } returns mockk()

        invokeGradleAction(
            fileTreeExecutionFiles,
            executionFiles,
        ) { probe ->
            subproject1.fileTree(subproject1BuildDir, probe)
        }
        invokeGradleAction(
            fileTreeExecutionFiles,
            executionFiles,
        ) { probe ->
            subproject2.fileTree(subproject2BuildDir, probe)
        }

        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration1.testDependencies.map { name -> "jacoco/$name.exec" }.toSet(),
            )
        } returns mockk()
        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration2.testDependencies.map { name -> "jacoco/$name.exec" }.toSet(),
            )
        } returns mockk()

        every { subproject1.tasks.getByName("${contextId}Coverage") } returns subproject1Reporter
        every { subproject2.tasks.getByName("${contextId}Coverage") } returns subproject2Reporter

        invokeGradleAction(
            reports,
            reports,
        ) { probe ->
            jacocoTask.reports(probe)
        }

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

        every {
            sourceDirectories.setFrom(
                subConfiguration1.sources.toMutableSet().also {
                    it.addAll(subConfiguration2.sources)
                },
            )
        } just Runs

        every {
            additionalClassesDirectories.setFrom(emptySet<File>())
        } just Runs

        every {
            additionalClassesDirectories.setFrom(
                subConfiguration2.additionalClasses,
            )
        } just Runs

        every {
            additionalSourceDirectories.setFrom(
                subConfiguration1.additionalSources.toMutableSet().also {
                    it.addAll(subConfiguration2.additionalSources)
                },
            )
        } just Runs

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 1) { jacocoTask.setDependsOn(setOf(subproject1Reporter, subproject2Reporter)) }

        verify(exactly = 1) { classDirectories.setFrom(setOf(classFiles)) }
        verify(exactly = 1) { sourceDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(emptySet<File>()) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(any()) }
        verify(exactly = 1) { executionData.setFrom(setOf(executionFiles)) }

        verify(exactly = 1) { isHtmlUsed.set(configuration.reportSettings.useHtml) }
        verify(exactly = 1) { isXmlUsed.set(configuration.reportSettings.useXml) }
        verify(exactly = 1) { isCsvUsed.set(configuration.reportSettings.useCsv) }

        verify(exactly = 1) { outputDir.set(htmlDir) }
        verify(exactly = 1) { outputFile.set(xmlFile) }
        verify(exactly = 1) { outputFile.set(csvFile) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters non fitting Projects for Android`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = AndroidJacocoAggregationConfiguration(
            flavour = "",
            variant = "",
        )

        val subConfiguration1 = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
        )

        val subproject1: Project = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject2: Project = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)

        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null

        every { subproject1CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { project.tasks.create(any(), JacocoReport::class.java, any()) }
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 0) { jacocoTask.setDependsOn(any()) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters if Android Projects share not the same Flavour`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = AndroidJacocoAggregationConfiguration(
            flavour = "",
            variant = fixture(),
        )

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            variant = fixture(),
            flavour = "",
            instrumentedTestDependencies = emptySet(),
        )

        val subproject1: Project = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject2: Project = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)

        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null

        every { subproject1CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { project.tasks.create(any(), JacocoReport::class.java, any()) }
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 0) { jacocoTask.setDependsOn(any()) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters if Android Projects share not the same Variant`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = AndroidJacocoAggregationConfiguration(
            flavour = fixture(),
            variant = "debug",
        )

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            variant = "debug",
            flavour = fixture(),
            instrumentedTestDependencies = emptySet(),
        )

        val subproject1: Project = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject2: Project = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)

        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null

        every { subproject1CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { project.tasks.create(any(), JacocoReport::class.java, any()) }
        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 0) { jacocoTask.setDependsOn(any()) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it aggregates the Configurations from its Subprojects and adds a ReporterTask for Android`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()

        val variant: String = fixture()
        val flavour: String = fixture()
        val configuration = AndroidJacocoAggregationConfiguration(
            flavour = flavour,
            variant = variant,
        )

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            instrumentedTestDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            flavour = flavour,
            variant = variant,
        )

        val subConfiguration2 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            instrumentedTestDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            flavour = flavour,
            variant = variant,
        )

        val projectName: String = fixture()
        val projectDir: File = mockk()
        val buildDir: File = mockk()

        val subproject1: Project = mockk()
        val subproject1BuildDir: File = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject1Reporter: JacocoReport = mockk()

        val subproject2: Project = mockk()
        val subproject2BuildDir: File = mockk()
        val subproject2ProjectDir: File = mockk()
        val subproject2CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject2Reporter: JacocoReport = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk()
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = false)
        val aggregationClassFileTree: ConfigurableFileTree = mockk()
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val buildDirLayout: DirectoryProperty = mockk()

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val reports: JacocoReportsContainer = mockk()
        val html: DirectoryReport = mockk()
        val xml: SingleFileReport = mockk()
        val csv: SingleFileReport = mockk()

        val isHtmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isXmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isCsvUsed: Property<Boolean> = mockk(relaxed = true)
        val outputDir: DirectoryProperty = mockk(relaxUnitFun = true)
        val outputFile: RegularFileProperty = mockk()

        val htmlDir: File = mockk()
        val csvFile: File = mockk()
        val xmlFile: File = mockk()

        every { project.name } returns projectName
        every { project.projectDir } returns projectDir
        every { project.buildDir } returns buildDir
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject2CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir
        every { subproject2.projectDir } returns subproject2ProjectDir

        every { subproject1.buildDir } returns subproject1BuildDir
        every { subproject2.buildDir } returns subproject2BuildDir

        every { subproject1CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )
        every { subproject2CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration2),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // please note there is a bug in Mockk which prevents to simply verify the provided arguments
        val dependencies: CapturingSlot<Set<Task>> = slot()
        every { jacocoTask.setDependsOn(capture(dependencies)) } just Runs

        every { jacocoTask.sourceDirectories } returns sourceDirectories
        every { jacocoTask.classDirectories } returns classDirectories
        every { jacocoTask.additionalClassDirs } returns additionalClassesDirectories
        every { jacocoTask.additionalSourceDirs } returns additionalSourceDirectories
        every { jacocoTask.executionData } returns executionData

        invokeGradleAction(
            fileTreeClassFiles,
            classFiles,
        ) { probe ->
            subproject1.fileTree(subproject1ProjectDir, probe)
        }
        invokeGradleAction(
            fileTreeClassFiles,
            classFiles,
        ) { probe ->
            subproject2.fileTree(subproject2ProjectDir, probe)
        }

        every {
            fileTreeClassFiles.setIncludes(subConfiguration1.classPattern)
        } returns mockk()

        every {
            fileTreeClassFiles.setIncludes(subConfiguration2.classPattern)
        } returns mockk()

        every {
            fileTreeClassFiles.setExcludes(subConfiguration1.classFilter)
        } returns mockk()

        every {
            fileTreeClassFiles.setExcludes(subConfiguration2.classFilter)
        } returns mockk()

        invokeGradleAction(
            fileTreeExecutionFiles,
            executionFiles,
        ) { probe ->
            subproject1.fileTree(subproject1BuildDir, probe)
        }
        invokeGradleAction(
            fileTreeExecutionFiles,
            executionFiles,
        ) { probe ->
            subproject2.fileTree(subproject2BuildDir, probe)
        }

        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration1.testDependencies.map { name -> "jacoco${File.separator}$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.flavour}${configuration.variant.capitalize()}UnitTest/test${configuration.flavour.capitalize()}${configuration.variant.capitalize()}UnitTest.exec",
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.flavour}${configuration.variant.capitalize()}AndroidTest/**/*coverage.ec",
                        )
                        it.add("jacoco/${configuration.flavour}${configuration.variant.capitalize()}.exec")
                        it.add("jacoco/jacoco.exec")
                    },
            )
        } returns mockk()
        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration2.testDependencies.map { name -> "jacoco${File.separator}$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.flavour}${configuration.variant.capitalize()}UnitTest/test${configuration.flavour.capitalize()}${configuration.variant.capitalize()}UnitTest.exec",
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.flavour}${configuration.variant.capitalize()}AndroidTest/**/*coverage.ec",
                        )
                        it.add("jacoco/${configuration.flavour}${configuration.variant.capitalize()}.exec")
                        it.add("jacoco/jacoco.exec")
                    },
            )
        } returns mockk()

        every { subproject1.tasks.getByName("${contextId}Coverage") } returns subproject1Reporter
        every { subproject2.tasks.getByName("${contextId}Coverage") } returns subproject2Reporter

        invokeGradleAction(
            reports,
            reports,
        ) { probe ->
            jacocoTask.reports(probe)
        }

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

        every {
            sourceDirectories.setFrom(
                subConfiguration1.sources.toMutableSet().also {
                    it.addAll(subConfiguration2.sources)
                },
            )
        } just Runs

        every {
            subConfiguration1.additionalClasses!!.plus(subConfiguration2.additionalClasses!!)
        } returns aggregationClassFileTree

        every {
            additionalClassesDirectories.setFrom(aggregationClassFileTree)
        } just Runs

        every {
            additionalSourceDirectories.setFrom(
                subConfiguration1.additionalSources.toMutableSet().also {
                    it.addAll(subConfiguration2.additionalSources)
                },
            )
        } just Runs

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 1) { jacocoTask.setDependsOn(setOf(subproject1Reporter, subproject2Reporter)) }

        verify(exactly = 1) { classDirectories.setFrom(setOf(classFiles)) }
        verify(exactly = 1) { sourceDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(any()) }
        verify(exactly = 1) { executionData.setFrom(setOf(executionFiles)) }

        verify(exactly = 1) { isHtmlUsed.set(configuration.reportSettings.useHtml) }
        verify(exactly = 1) { isXmlUsed.set(configuration.reportSettings.useXml) }
        verify(exactly = 1) { isCsvUsed.set(configuration.reportSettings.useCsv) }

        verify(exactly = 1) { outputDir.set(htmlDir) }
        verify(exactly = 1) { outputFile.set(xmlFile) }
        verify(exactly = 1) { outputFile.set(csvFile) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it aggregates the Configurations from the Subprojects, while filtering additional Classes for Android`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()

        val variant: String = fixture()
        val flavour: String = fixture()
        val configuration = AndroidJacocoAggregationConfiguration(
            flavour = flavour,
            variant = variant,
        )

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            instrumentedTestDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = null,
            verificationRules = emptySet(),
            flavour = flavour,
            variant = variant,
        )

        val subConfiguration2 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            testDependencies = setOf(fixture(), fixture()),
            instrumentedTestDependencies = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = null,
            verificationRules = emptySet(),
            flavour = flavour,
            variant = variant,
        )

        val projectName: String = fixture()
        val projectDir: File = mockk()
        val buildDir: File = mockk()

        val subproject1: Project = mockk()
        val subproject1BuildDir: File = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject1Reporter: JacocoReport = mockk()

        val subproject2: Project = mockk()
        val subproject2BuildDir: File = mockk()
        val subproject2ProjectDir: File = mockk()
        val subproject2CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject2Reporter: JacocoReport = mockk()

        val jacocoTask: JacocoReport = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk()
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = false)
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val buildDirLayout: DirectoryProperty = mockk()

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val reports: JacocoReportsContainer = mockk()
        val html: DirectoryReport = mockk()
        val xml: SingleFileReport = mockk()
        val csv: SingleFileReport = mockk()

        val isHtmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isXmlUsed: Property<Boolean> = mockk(relaxed = true)
        val isCsvUsed: Property<Boolean> = mockk(relaxed = true)
        val outputDir: DirectoryProperty = mockk(relaxUnitFun = true)
        val outputFile: RegularFileProperty = mockk()

        val htmlDir: File = mockk()
        val csvFile: File = mockk()
        val xmlFile: File = mockk()

        every { project.name } returns projectName
        every { project.projectDir } returns projectDir
        every { project.buildDir } returns buildDir
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject2CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir
        every { subproject2.projectDir } returns subproject2ProjectDir

        every { subproject1.buildDir } returns subproject1BuildDir
        every { subproject2.buildDir } returns subproject2BuildDir

        every { subproject1CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )
        every { subproject2CoverageExtension.configurations } returns GradlePropertyBuilder.makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration2),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create("${contextId}CoverageAggregation", JacocoReport::class.java, probe)
        }

        // please note there is a bug in Mockk which prevents to simply verify the provided arguments
        val dependencies: CapturingSlot<Set<Task>> = slot()
        every { jacocoTask.setDependsOn(capture(dependencies)) } just Runs

        every { jacocoTask.sourceDirectories } returns sourceDirectories
        every { jacocoTask.classDirectories } returns classDirectories
        every { jacocoTask.additionalClassDirs } returns additionalClassesDirectories
        every { jacocoTask.additionalSourceDirs } returns additionalSourceDirectories
        every { jacocoTask.executionData } returns executionData

        invokeGradleAction(
            fileTreeClassFiles,
            classFiles,
        ) { probe ->
            subproject1.fileTree(subproject1ProjectDir, probe)
        }
        invokeGradleAction(
            fileTreeClassFiles,
            classFiles,
        ) { probe ->
            subproject2.fileTree(subproject2ProjectDir, probe)
        }

        every {
            fileTreeClassFiles.setIncludes(subConfiguration1.classPattern)
        } returns mockk()

        every {
            fileTreeClassFiles.setIncludes(subConfiguration2.classPattern)
        } returns mockk()

        every {
            fileTreeClassFiles.setExcludes(subConfiguration1.classFilter)
        } returns mockk()

        every {
            fileTreeClassFiles.setExcludes(subConfiguration2.classFilter)
        } returns mockk()

        invokeGradleAction(
            fileTreeExecutionFiles,
            executionFiles,
        ) { probe ->
            subproject1.fileTree(subproject1BuildDir, probe)
        }
        invokeGradleAction(
            fileTreeExecutionFiles,
            executionFiles,
        ) { probe ->
            subproject2.fileTree(subproject2BuildDir, probe)
        }

        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration1.testDependencies.map { name -> "jacoco${File.separator}$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.flavour}${configuration.variant.capitalize()}UnitTest/test${configuration.flavour.capitalize()}${configuration.variant.capitalize()}UnitTest.exec",
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.flavour}${configuration.variant.capitalize()}AndroidTest/**/*coverage.ec",
                        )
                        it.add("jacoco/${configuration.flavour}${configuration.variant.capitalize()}.exec")
                        it.add("jacoco/jacoco.exec")
                    },
            )
        } returns mockk()
        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration2.testDependencies.map { name -> "jacoco${File.separator}$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.flavour}${configuration.variant.capitalize()}UnitTest/test${configuration.flavour.capitalize()}${configuration.variant.capitalize()}UnitTest.exec",
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.flavour}${configuration.variant.capitalize()}AndroidTest/**/*coverage.ec",
                        )
                        it.add("jacoco/${configuration.flavour}${configuration.variant.capitalize()}.exec")
                        it.add("jacoco/jacoco.exec")
                    },
            )
        } returns mockk()

        every { subproject1.tasks.getByName("${contextId}Coverage") } returns subproject1Reporter
        every { subproject2.tasks.getByName("${contextId}Coverage") } returns subproject2Reporter

        invokeGradleAction(
            reports,
            reports,
        ) { probe ->
            jacocoTask.reports(probe)
        }

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

        every {
            sourceDirectories.setFrom(
                subConfiguration1.sources.toMutableSet().also {
                    it.addAll(subConfiguration2.sources)
                },
            )
        } just Runs

        every {
            additionalClassesDirectories.setFrom(emptySet<File>())
        } just Runs

        every {
            additionalSourceDirectories.setFrom(
                subConfiguration1.additionalSources.toMutableSet().also {
                    it.addAll(subConfiguration2.additionalSources)
                },
            )
        } just Runs

        // When
        val aggregator = JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = aggregator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Aggregates coverage reports for ${contextId.capitalize()}." }
        verify(exactly = 1) { jacocoTask.setDependsOn(setOf(subproject1Reporter, subproject2Reporter)) }

        verify(exactly = 1) { classDirectories.setFrom(setOf(classFiles)) }
        verify(exactly = 1) { sourceDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(emptySet<File>()) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(any()) }
        verify(exactly = 1) { executionData.setFrom(setOf(executionFiles)) }

        verify(exactly = 1) { isHtmlUsed.set(configuration.reportSettings.useHtml) }
        verify(exactly = 1) { isXmlUsed.set(configuration.reportSettings.useXml) }
        verify(exactly = 1) { isCsvUsed.set(configuration.reportSettings.useCsv) }

        verify(exactly = 1) { outputDir.set(htmlDir) }
        verify(exactly = 1) { outputFile.set(xmlFile) }
        verify(exactly = 1) { outputFile.set(csvFile) }
    }
}
