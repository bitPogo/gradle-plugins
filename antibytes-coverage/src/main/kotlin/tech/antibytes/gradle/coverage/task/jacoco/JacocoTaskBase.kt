/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import java.io.File
import org.gradle.api.Project
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportBase
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.util.capitalize
import tech.antibytes.gradle.util.decapitalize

internal abstract class JacocoTaskBase {
    private fun determineJvmExecutionFiles(
        contextId: Set<String>,
    ): Set<String> = contextId.map { name -> "jacoco${File.separator}$name.exec" }.toSet()

    private fun determineAndroidExecutionsFiles(
        executionFiles: Set<String>,
        configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration,
    ): Set<String> {
        val execs = executionFiles.toMutableSet()
        val infix = "${configuration.flavour}${configuration.variant.capitalize()}".decapitalize()

        execs.add("outputs${File.separator}unit_test_code_coverage${File.separator}${infix}UnitTest${File.separator}test${infix.capitalize()}UnitTest.exec")
        execs.add("outputs${File.separator}code_coverage${File.separator}${infix}AndroidTest${File.separator}**${File.separator}*coverage.ec")
        execs.add("jacoco${File.separator}$infix.exec")
        execs.add("jacoco${File.separator}jacoco.exec")

        return execs
    }

    protected fun determineExecutionsFiles(
        configuration: CoverageApiContract.JacocoCoverageConfiguration,
    ): Set<String> {
        val executionFiles = determineJvmExecutionFiles(configuration.test)

        return if (configuration is CoverageApiContract.AndroidJacocoCoverageConfiguration) {
            determineAndroidExecutionsFiles(executionFiles, configuration)
        } else {
            executionFiles
        }
    }

    protected fun configureJacocoCoverageBase(
        project: Project,
        task: JacocoReportBase,
        configuration: CoverageApiContract.JacocoCoverageConfiguration,
        executionFiles: Set<String>,
    ) {
        task.sourceDirectories.setFrom(configuration.sources)
        task.classDirectories.setFrom(
            project.fileTree(project.projectDir) {
                setIncludes(configuration.classPattern)
                setExcludes(configuration.classFilter)
            },
        )

        task.additionalSourceDirs.setFrom(configuration.additionalSources)
        task.additionalClassDirs.setFrom(configuration.additionalClasses)
        task.executionData.setFrom(
            project.fileTree(project.layout.buildDirectory) {
                setIncludes(executionFiles)
            },
        )
    }

    protected fun configureOutput(
        project: Project,
        contextId: String,
        reportSettings: CoverageApiContract.JacocoReporterSettings,
        task: JacocoReport,
    ) {
        val buildDir = project.layout.buildDirectory.get()

        task.reports {
            html.required.set(reportSettings.useHtml)
            xml.required.set(reportSettings.useXml)
            csv.required.set(reportSettings.useCsv)

            html.outputLocation.set(
                buildDir.dir(
                    "reports/jacoco/$contextId/${project.name}",
                ).asFile,
            )
            csv.outputLocation.set(
                buildDir.file(
                    "reports/jacoco/$contextId/${project.name}.csv",
                ).asFile,
            )
            xml.outputLocation.set(
                buildDir.file(
                    "reports/jacoco/$contextId/${project.name}.xml",
                ).asFile,
            )
        }
    }
}
