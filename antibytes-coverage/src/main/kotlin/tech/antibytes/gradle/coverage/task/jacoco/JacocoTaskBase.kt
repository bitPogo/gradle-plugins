/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import org.gradle.api.Project
import org.gradle.testing.jacoco.tasks.JacocoReportBase
import tech.antibytes.gradle.coverage.CoverageApiContract
import java.io.File

internal abstract class JacocoTaskBase {
    private fun determineJvmExecutionFiles(
        contextName: Set<String>
    ): Set<String> = contextName.map { name -> "jacoco${File.separator}$name.exec" }.toSet()

    private fun determineAndroidExecutionsFiles(
        executionFiles: Set<String>,
        configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration
    ): Set<String> {
        val execs = executionFiles.toMutableSet()
        val infix = "${configuration.flavour}${configuration.variant.capitalize()}AndroidTest".decapitalize()

        execs.add("outputs${File.separator}code_coverage${File.separator}$infix${File.separator}**${File.separator}*coverage.ec")
        execs.add("jacoco${File.separator}jacoco.exec")

        return execs
    }

    protected fun determineExecutionsFiles(
        configuration: CoverageApiContract.JacocoCoverageConfiguration
    ): Set<String> {
        val executionFiles = determineJvmExecutionFiles(configuration.testDependencies)

        return if (configuration is CoverageApiContract.AndroidJacocoCoverageConfiguration) {
            determineAndroidExecutionsFiles(executionFiles, configuration)
        } else {
            executionFiles
        }
    }

    protected fun configureJacocoCoverageBase(
        configuration: CoverageApiContract.JacocoCoverageConfiguration,
        executionFiles: Set<String>,
        project: Project,
        task: JacocoReportBase
    ) {
        task.sourceDirectories.setFrom(configuration.sources)
        task.classDirectories.setFrom(
            project.fileTree(project.projectDir) {
                setIncludes(configuration.classPattern)
                setExcludes(configuration.classFilter)
            }
        )

        task.additionalSourceDirs.setFrom(configuration.additionalSources)
        task.additionalClassDirs.setFrom(configuration.additionalClasses)
        task.executionData.setFrom(
            project.fileTree(project.buildDir) {
                setIncludes(executionFiles)
            }
        )
    }
}
