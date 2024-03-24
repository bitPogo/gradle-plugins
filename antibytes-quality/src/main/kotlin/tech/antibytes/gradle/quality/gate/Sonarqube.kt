/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.quality.gate

import org.gradle.api.Project
import org.sonarqube.gradle.SonarExtension
import tech.antibytes.gradle.quality.Configurator
import tech.antibytes.gradle.quality.QualityApiContract.QualityGateConfiguration
import tech.antibytes.gradle.quality.QualityContract.Extension

internal object Sonarqube : Configurator() {
    private fun Project.configure(configuration: QualityGateConfiguration) {
        extensions.configure(SonarExtension::class.java) {
            properties {
                property("sonar.projectKey", configuration.projectKey)
                property("sonar.organization", configuration.organization)
                property("sonar.host.url", configuration.host)

                property("sonar.sourceEncoding", configuration.encoding)
                property("sonar.coverage.jacoco.xmlReportPaths", configuration.jacoco)
                property("sonar.junit.reportPaths", configuration.junit)
                property("sonar.kotlin.detekt.reportPaths", configuration.detekt)
            }
        }
    }

    private fun SonarExtension.excludeProject() {
        isSkipProject = true
    }

    private fun Project.excludeSubprojects(configuration: QualityGateConfiguration) {
        subprojects {
            if (name in configuration.exclude) {
                extensions.configure(SonarExtension::class.java) {
                    excludeProject()
                }
            }
        }
    }

    override fun configure(project: Project, configuration: Extension) {
        configuration.qualityGate.orNull.applyIfNotNull { qualityGateConfiguration ->
            project.plugins.apply("org.sonarqube")
            project.configure(qualityGateConfiguration)
            project.excludeSubprojects(qualityGateConfiguration)
        }
    }
}
