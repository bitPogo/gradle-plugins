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
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.DirectoryProperty
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRulesContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.AntibytesCoveragePluginExtension
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.api.AndroidJacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.api.AndroidJacocoConfiguration
import tech.antibytes.gradle.coverage.api.JacocoReporterSettings
import tech.antibytes.gradle.coverage.api.JacocoVerificationRule
import tech.antibytes.gradle.coverage.api.JvmJacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.task.TaskContract
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeMapProperty
import tech.antibytes.gradle.test.invokeGradleAction
import tech.antibytes.gradle.util.capitalize

class JacocoAggregationVerificationTaskConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils VerificationTaskConfigurator`() {
        val verificator: Any = JacocoAggregationVerificationTaskConfigurator()

        assertTrue(verificator is TaskContract.VerificationTaskConfigurator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it does nothing if no VerificationRules had been given`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration(
            verificationRules = emptySet(),
        )

        val subConfiguration1 = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
        )

        val subproject1: Project = mockk()
        val subproject1BuildDir: DirectoryProperty = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        every { project.subprojects } returns setOf(subproject1)

        every { subproject1.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir

        every { subproject1.layout.buildDirectory } returns subproject1BuildDir

        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )

        every { subproject1.fileTree(any<File>(), any<Action<ConfigurableFileTree>>()) } returns mockk()
        every { subproject1.tasks.getByName(any()) } returns mockk<JacocoReport>()

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator().configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
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
        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
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
        val verificator = JacocoAggregationVerificationTaskConfigurator().configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration, it filters invalid VerificationRules`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration(
            verificationRules = setOf(JacocoVerificationRule()),
        )

        val subConfiguration1 = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
        )

        val subproject1: Project = mockk()
        val subproject1BuildDir: DirectoryProperty = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        every { project.subprojects } returns setOf(subproject1)

        every { subproject1.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir

        every { subproject1.layout.buildDirectory } returns subproject1BuildDir

        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )

        every { subproject1.fileTree(any<File>(), any<Action<ConfigurableFileTree>>()) } returns mockk()
        every { subproject1.tasks.getByName(any()) } returns mockk<JacocoReport>()

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator().configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters non covered Projects`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration(
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = BigDecimal(fixture<Int>()),
                ),
            ),
        )

        val subproject1: Project = mockk()
        val subproject2: Project = mockk()

        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator().configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters excluded Projects`() {
        // Given
        val project: Project = mockk()
        val subprojectName: String = fixture()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration(
            exclude = setOf(subprojectName),
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = BigDecimal(fixture<Int>()),
                ),
            ),
        )

        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject1: Project = mockk()

        every { project.subprojects } returns setOf(subproject1)

        every { subproject1.name } returns subprojectName
        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator().configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters non fitting Projects for Jvm`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration(
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = BigDecimal(fixture<Int>()),
                ),
            ),
        )

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            variant = "debug",
            flavour = "",
            instrumentedTest = emptySet(),
        )

        val subproject1: Project = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject2: Project = mockk()

        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension

        every { subproject2.name } returns fixture()
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null

        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator().configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it aggregates the Configurations from its Subprojects and adds a VerificationTask for Jvm`() {
        // Given
        val mapper: JacocoVerificationRuleMapper = mockk()
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration(
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = BigDecimal(fixture<Int>()),
                ),
            ),
        )

        val subConfiguration1 = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
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
            test = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
        )

        val projectName: String = fixture()
        val projectDir: File = mockk()
        val buildDir: DirectoryProperty = mockk()

        val subproject1: Project = mockk()
        val subproject1BuildDir: DirectoryProperty = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject1Reporter: JacocoReport = mockk()

        val subproject2: Project = mockk()
        val subproject2BuildDir: DirectoryProperty = mockk()
        val subproject2ProjectDir: File = mockk()
        val subproject2CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject2Reporter: JacocoReport = mockk()

        val jacocoTask: JacocoCoverageVerification = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk()
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val aggregationClassFileTree: ConfigurableFileTree = mockk()
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val violationRules: JacocoViolationRulesContainer = mockk(relaxed = true)

        every { project.name } returns projectName
        every { project.projectDir } returns projectDir
        every { project.layout.buildDirectory } returns buildDir
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject2CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir
        every { subproject2.projectDir } returns subproject2ProjectDir

        every { subproject1.layout.buildDirectory } returns subproject1BuildDir
        every { subproject2.layout.buildDirectory } returns subproject2BuildDir

        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )
        every { subproject2CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration2),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create(
                "${contextId}AggregationVerification",
                JacocoCoverageVerification::class.java,
                probe,
            )
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

        every { subproject1.tasks.getByName("${contextId}Coverage") } returns subproject1Reporter
        every { subproject2.tasks.getByName("${contextId}Coverage") } returns subproject2Reporter

        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration1.test.map { name -> "jacoco/$name.exec" }.toSet(),
            )
        } returns mockk()
        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration2.test.map { name -> "jacoco/$name.exec" }.toSet(),
            )
        } returns mockk()

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

        invokeGradleAction(
            violationRules,
            mockk(),
        ) { probe ->
            jacocoTask.violationRules(probe)
        }

        every { mapper.map(violationRules, configuration.verificationRules) } just Runs

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator(mapper).configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = verificator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Verifies the aggregated coverage reports against a given set of rules for ${contextId.capitalize()}." }
        assertEquals(
            actual = dependencies.captured,
            expected = setOf(subproject1Reporter, subproject2Reporter),
        )
        verify(exactly = 1) { jacocoTask.setDependsOn(any()) }

        verify(exactly = 1) { classDirectories.setFrom(setOf(classFiles)) }
        verify(exactly = 1) { sourceDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(any()) }
        verify(exactly = 1) { executionData.setFrom(setOf(executionFiles)) }

        verify(exactly = 1) { violationRules.isFailOnViolation = true }
        verify(exactly = 1) { mapper.map(violationRules, configuration.verificationRules) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it aggregates the Configurations from its Subprojects, while filtering additional Classes for Jvm`() {
        // Given
        val mapper: JacocoVerificationRuleMapper = mockk()
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = JvmJacocoAggregationConfiguration(
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = BigDecimal(fixture<Int>()),
                ),
            ),
        )

        val subConfiguration1 = JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
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
            test = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = null,
            verificationRules = emptySet(),
        )

        val projectName: String = fixture()
        val projectDir: File = mockk()
        val buildDir: DirectoryProperty = mockk()

        val subproject1: Project = mockk()
        val subproject1BuildDir: DirectoryProperty = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject1Reporter: JacocoReport = mockk()

        val subproject2: Project = mockk()
        val subproject2BuildDir: DirectoryProperty = mockk()
        val subproject2ProjectDir: File = mockk()
        val subproject2CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject2Reporter: JacocoReport = mockk()

        val jacocoTask: JacocoCoverageVerification = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk()
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val aggregationClassFileTree: ConfigurableFileTree = mockk()
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val violationRules: JacocoViolationRulesContainer = mockk(relaxed = true)

        every { project.name } returns projectName
        every { project.projectDir } returns projectDir
        every { project.layout.buildDirectory } returns buildDir
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject2CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir
        every { subproject2.projectDir } returns subproject2ProjectDir

        every { subproject1.layout.buildDirectory } returns subproject1BuildDir
        every { subproject2.layout.buildDirectory } returns subproject2BuildDir

        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )
        every { subproject2CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration2),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create(
                "${contextId}AggregationVerification",
                JacocoCoverageVerification::class.java,
                probe,
            )
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

        every { subproject1.tasks.getByName("${contextId}Coverage") } returns subproject1Reporter
        every { subproject2.tasks.getByName("${contextId}Coverage") } returns subproject2Reporter

        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration1.test.map { name -> "jacoco/$name.exec" }.toSet(),
            )
        } returns mockk()
        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration2.test.map { name -> "jacoco/$name.exec" }.toSet(),
            )
        } returns mockk()

        every {
            sourceDirectories.setFrom(
                subConfiguration1.sources.toMutableSet().also {
                    it.addAll(subConfiguration2.sources)
                },
            )
        } just Runs

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

        invokeGradleAction(
            violationRules,
            mockk(),
        ) { probe ->
            jacocoTask.violationRules(probe)
        }

        every { mapper.map(violationRules, configuration.verificationRules) } just Runs

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator(mapper).configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = verificator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Verifies the aggregated coverage reports against a given set of rules for ${contextId.capitalize()}." }
        assertEquals(
            actual = dependencies.captured,
            expected = setOf(subproject1Reporter, subproject2Reporter),
        )
        verify(exactly = 1) { jacocoTask.setDependsOn(any()) }

        verify(exactly = 1) { classDirectories.setFrom(setOf(classFiles)) }
        verify(exactly = 1) { sourceDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(emptySet<File>()) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(any()) }
        verify(exactly = 1) { executionData.setFrom(setOf(executionFiles)) }

        verify(exactly = 1) { violationRules.isFailOnViolation = true }
        verify(exactly = 1) { mapper.map(violationRules, configuration.verificationRules) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters if Android Projects share not the same Flavour`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = AndroidJacocoAggregationConfiguration(
            flavour = "",
            variant = fixture(),
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = BigDecimal(fixture<Int>()),
                ),
            ),
        )

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            variant = fixture(),
            flavour = "",
            instrumentedTest = emptySet(),
        )

        val subproject1: Project = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject2: Project = mockk()

        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null

        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator().configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it filters if Android Projects share not the same Variant`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val configuration = AndroidJacocoAggregationConfiguration(
            flavour = "",
            variant = fixture(),
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = BigDecimal(fixture<Int>()),
                ),
            ),
        )

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            variant = fixture(),
            flavour = "",
            instrumentedTest = emptySet(),
        )

        val subproject1: Project = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()

        val subproject2: Project = mockk()

        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns null

        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator().configure(project, contextId, configuration)

        // Then
        assertNull(verificator)
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it aggregates the Configurations from its Subprojects and adds a VerificationTask for Android`() {
        // Given
        val project: Project = mockk()
        val contextId: String = fixture()
        val variant: String = fixture()
        val configuration = AndroidJacocoAggregationConfiguration(
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = BigDecimal(fixture<Int>()),
                ),
            ),
            flavour = "",
            variant = variant,
        )

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
            instrumentedTest = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            variant = variant,
            flavour = "",
        )

        val subConfiguration2 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
            instrumentedTest = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = mockk(),
            verificationRules = emptySet(),
            variant = variant,
            flavour = "",
        )

        val projectName: String = fixture()
        val projectDir: File = mockk()
        val buildDir: DirectoryProperty = mockk()

        val subproject1: Project = mockk()
        val subproject1BuildDir: DirectoryProperty = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject1Reporter: JacocoReport = mockk()

        val subproject2: Project = mockk()
        val subproject2BuildDir: DirectoryProperty = mockk()
        val subproject2ProjectDir: File = mockk()
        val subproject2CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject2Reporter: JacocoReport = mockk()

        val jacocoTask: JacocoCoverageVerification = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk()
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val aggregationClassFileTree: ConfigurableFileTree = mockk()
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val violationRules: JacocoViolationRulesContainer = mockk(relaxed = true)

        every { project.name } returns projectName
        every { project.projectDir } returns projectDir
        every { project.layout.buildDirectory } returns buildDir
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject2CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir
        every { subproject2.projectDir } returns subproject2ProjectDir

        every { subproject1.layout.buildDirectory } returns subproject1BuildDir
        every { subproject2.layout.buildDirectory } returns subproject2BuildDir

        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )
        every { subproject2CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration2),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create(
                "${contextId}AggregationVerification",
                JacocoCoverageVerification::class.java,
                probe,
            )
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

        every { subproject1.tasks.getByName("${contextId}Coverage") } returns subproject1Reporter
        every { subproject2.tasks.getByName("${contextId}Coverage") } returns subproject2Reporter

        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration1.test.map { name -> "jacoco${File.separator}$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.variant}UnitTest/test${configuration.variant.capitalize()}UnitTest.exec",
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.variant}AndroidTest/**/*coverage.ec",
                        )
                        it.add("jacoco/${configuration.variant}.exec")
                        it.add("jacoco/jacoco.exec")
                    },
            )
        } returns mockk()
        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration2.test.map { name -> "jacoco${File.separator}$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.variant}UnitTest/test${configuration.variant.capitalize()}UnitTest.exec",
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.variant}AndroidTest/**/*coverage.ec",
                        )
                        it.add("jacoco/${configuration.variant}.exec")
                        it.add("jacoco/jacoco.exec")
                    },
            )
        } returns mockk()

        every {
            sourceDirectories.setFrom(
                subConfiguration1.sources.toMutableSet().also {
                    it.addAll(subConfiguration2.sources)
                },
            )
        } just Runs

        every {
            additionalClassesDirectories.setFrom(subConfiguration1.additionalClasses)
        } just Runs

        every {
            additionalClassesDirectories.setFrom(subConfiguration2.additionalClasses)
        } just Runs

        every {
            subConfiguration1.additionalClasses!!.plus(subConfiguration2.additionalClasses!!)
        } returns aggregationClassFileTree

        every {
            additionalClassesDirectories.setFrom(aggregationClassFileTree)
        } just Runs

        invokeGradleAction(
            violationRules,
            mockk(),
        ) { probe ->
            jacocoTask.violationRules(probe)
        }

        val mapper: JacocoContract.JacocoVerificationRuleMapper = mockk {
            every {
                map(
                    violationRules,
                    configuration.verificationRules,
                )
            } just Runs
        }

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator(mapper).configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = verificator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Verifies the aggregated coverage reports against a given set of rules for ${contextId.capitalize()}." }
        assertEquals(
            actual = dependencies.captured,
            expected = setOf(subproject1Reporter, subproject2Reporter),
        )
        verify(exactly = 1) { jacocoTask.setDependsOn(any()) }

        verify(exactly = 1) { classDirectories.setFrom(setOf(classFiles)) }
        verify(exactly = 1) { sourceDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(any()) }
        verify(exactly = 1) { executionData.setFrom(setOf(executionFiles)) }

        verify(exactly = 1) { violationRules.isFailOnViolation = true }
        verify(exactly = 1) { mapper.map(violationRules, configuration.verificationRules) }
    }

    @Test
    fun `Given configure is called with a Project, ContextId and Configuration it aggregates the Configurations while filtering additional Classes for Android`() {
        // Given
        val mapper: JacocoVerificationRuleMapper = mockk()
        val project: Project = mockk()
        val contextId: String = fixture()
        val variant: String = fixture()
        val configuration = AndroidJacocoAggregationConfiguration(
            verificationRules = setOf(
                JacocoVerificationRule(
                    minimum = BigDecimal(fixture<Int>()),
                ),
            ),
            flavour = "",
            variant = variant,
        )

        val subConfiguration1 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
            instrumentedTest = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = null,
            verificationRules = emptySet(),
            variant = variant,
            flavour = "",
        )

        val subConfiguration2 = AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(
                useHtml = fixture(),
                useXml = fixture(),
                useCsv = fixture(),
            ),
            test = setOf(fixture(), fixture()),
            instrumentedTest = setOf(fixture(), fixture()),
            classPattern = fixture(),
            classFilter = fixture(),
            sources = setOf(mockk()),
            additionalSources = setOf(mockk()),
            additionalClasses = null,
            verificationRules = emptySet(),
            variant = variant,
            flavour = "",
        )

        val projectName: String = fixture()
        val projectDir: File = mockk()
        val buildDir: DirectoryProperty = mockk()

        val subproject1: Project = mockk()
        val subproject1BuildDir: DirectoryProperty = mockk()
        val subproject1ProjectDir: File = mockk()
        val subproject1CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject1Reporter: JacocoReport = mockk()

        val subproject2: Project = mockk()
        val subproject2BuildDir: DirectoryProperty = mockk()
        val subproject2ProjectDir: File = mockk()
        val subproject2CoverageExtension: AntibytesCoveragePluginExtension = mockk()
        val subproject2Reporter: JacocoReport = mockk()

        val jacocoTask: JacocoCoverageVerification = mockk(relaxed = true)
        val sourceDirectories: ConfigurableFileCollection = mockk()
        val classDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalSourceDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val additionalClassesDirectories: ConfigurableFileCollection = mockk(relaxUnitFun = true)
        val aggregationClassFileTree: ConfigurableFileTree = mockk()
        val executionData: ConfigurableFileCollection = mockk(relaxUnitFun = true)

        val fileTreeClassFiles: ConfigurableFileTree = mockk()
        val fileTreeExecutionFiles: ConfigurableFileTree = mockk()

        val classFiles: ConfigurableFileTree = mockk()
        val executionFiles: ConfigurableFileTree = mockk()

        val violationRules: JacocoViolationRulesContainer = mockk(relaxed = true)

        every { project.name } returns projectName
        every { project.projectDir } returns projectDir
        every { project.layout.buildDirectory } returns buildDir
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.name } returns fixture()
        every { subproject2.name } returns fixture()

        every { subproject1.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject1CoverageExtension
        every { subproject2.extensions.findByType(AntibytesCoveragePluginExtension::class.java) } returns subproject2CoverageExtension

        every { subproject1.projectDir } returns subproject1ProjectDir
        every { subproject2.projectDir } returns subproject2ProjectDir

        every { subproject1.layout.buildDirectory } returns subproject1BuildDir
        every { subproject2.layout.buildDirectory } returns subproject2BuildDir

        every { subproject1CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration1),
        )
        every { subproject2CoverageExtension.configurations } returns makeMapProperty(
            String::class.java,
            CoverageApiContract.CoverageConfiguration::class.java,
            mutableMapOf(contextId to subConfiguration2),
        )

        invokeGradleAction(
            jacocoTask,
            jacocoTask,
        ) { probe ->
            project.tasks.create(
                "${contextId}AggregationVerification",
                JacocoCoverageVerification::class.java,
                probe,
            )
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

        every { subproject1.tasks.getByName("${contextId}Coverage") } returns subproject1Reporter
        every { subproject2.tasks.getByName("${contextId}Coverage") } returns subproject2Reporter

        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration1.test.map { name -> "jacoco${File.separator}$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.variant}UnitTest/test${configuration.variant.capitalize()}UnitTest.exec",
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.variant}AndroidTest/**/*coverage.ec",
                        )
                        it.add("jacoco/${configuration.variant}.exec")
                        it.add("jacoco/jacoco.exec")
                    },
            )
        } returns mockk()
        every {
            fileTreeExecutionFiles.setIncludes(
                subConfiguration2.test.map { name -> "jacoco${File.separator}$name.exec" }
                    .toMutableSet()
                    .also {
                        it.add(
                            "outputs/unit_test_code_coverage/${configuration.variant}UnitTest/test${configuration.variant.capitalize()}UnitTest.exec",
                        )
                        it.add(
                            "outputs/code_coverage/${configuration.variant}AndroidTest/**/*coverage.ec",
                        )
                        it.add("jacoco/${configuration.variant}.exec")
                        it.add("jacoco/jacoco.exec")
                    },
            )
        } returns mockk()

        every {
            sourceDirectories.setFrom(
                subConfiguration1.sources.toMutableSet().also {
                    it.addAll(subConfiguration2.sources)
                },
            )
        } just Runs

        every {
            additionalClassesDirectories.setFrom(subConfiguration1.additionalClasses)
        } just Runs

        every {
            additionalClassesDirectories.setFrom(subConfiguration2.additionalClasses)
        } just Runs

        every {
            additionalClassesDirectories.setFrom(aggregationClassFileTree)
        } just Runs

        invokeGradleAction(
            violationRules,
            mockk(),
        ) { probe ->
            jacocoTask.violationRules(probe)
        }

        every { mapper.map(violationRules, configuration.verificationRules) } just Runs

        // When
        val verificator = JacocoAggregationVerificationTaskConfigurator(mapper).configure(project, contextId, configuration)

        // Then
        assertSame(
            actual = verificator,
            expected = jacocoTask,
        )

        verify(exactly = 1) { jacocoTask.group = "Verification" }
        verify(exactly = 1) { jacocoTask.description = "Verifies the aggregated coverage reports against a given set of rules for ${contextId.capitalize()}." }
        assertEquals(
            actual = dependencies.captured,
            expected = setOf(subproject1Reporter, subproject2Reporter),
        )
        verify(exactly = 1) { jacocoTask.setDependsOn(any()) }

        verify(exactly = 1) { classDirectories.setFrom(setOf(classFiles)) }
        verify(exactly = 1) { sourceDirectories.setFrom(any()) }
        verify(exactly = 1) { additionalClassesDirectories.setFrom(emptySet<File>()) }
        verify(exactly = 1) { additionalSourceDirectories.setFrom(any()) }
        verify(exactly = 1) { executionData.setFrom(setOf(executionFiles)) }

        verify(exactly = 1) { violationRules.isFailOnViolation = true }
        verify(exactly = 1) { mapper.map(violationRules, configuration.verificationRules) }
    }
}
