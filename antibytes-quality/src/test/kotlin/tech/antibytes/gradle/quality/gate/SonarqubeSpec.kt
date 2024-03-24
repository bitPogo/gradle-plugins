/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.quality.gate

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Project
import org.junit.jupiter.api.Test
import org.sonarqube.gradle.SonarExtension
import org.sonarqube.gradle.SonarProperties
import tech.antibytes.gradle.quality.AntibytesQualityExtension
import tech.antibytes.gradle.quality.QualityContract
import tech.antibytes.gradle.quality.api.QualityGateConfiguration
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.test.invokeGradleAction

class SonarqubeSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Configurator`() {
        val configurator: Any = Sonarqube

        assertTrue(configurator is QualityContract.Configurator)
    }

    @Test
    fun `Given configure is called it does nothing if no QualityGate configuration was given`() {
        // Given
        val project: Project = mockk()
        val extension = createExtension<AntibytesQualityExtension>()
        extension.qualityGate.convention(null)

        // When
        Sonarqube.configure(project, extension)
    }

    @Test
    fun `Given configure is called with an analysis Config it configures the SonarQube extension`() {
        // Given
        val project: Project = mockk()
        val extension = createExtension<AntibytesQualityExtension>()

        val config = QualityGateConfiguration(
            projectKey = fixture(),
            organization = fixture(),
            host = fixture(),
            encoding = fixture(),
            jacoco = fixture(),
            junit = fixture(),
            detekt = fixture(),
        )
        val sonarExtension: SonarExtension = mockk(relaxed = true)
        val sonarProperties: SonarProperties = mockk(relaxed = true)

        extension.qualityGate.set(config)

        every { project.plugins.apply(any()) } returns mockk()
        every { project.subprojects(any<Action<Any>>()) } just Runs
        invokeGradleAction(
            sonarExtension,
            sonarExtension,
        ) { probe ->
            project.extensions.configure(SonarExtension::class.java, probe)
        }
        invokeGradleAction(
            sonarProperties,
            sonarProperties,
        ) { probe ->
            sonarExtension.properties(probe)
        }

        // When
        Sonarqube.configure(project, extension)
        verify(exactly = 1) { sonarProperties.property("sonar.projectKey", config.projectKey) }
        verify(exactly = 1) { sonarProperties.property("sonar.organization", config.organization) }
        verify(exactly = 1) { sonarProperties.property("sonar.host.url", config.host) }

        verify(exactly = 1) { sonarProperties.property("sonar.sourceEncoding", config.encoding) }
        verify(exactly = 1) { sonarProperties.property("sonar.coverage.jacoco.xmlReportPaths", config.jacoco) }
        verify(exactly = 1) { sonarProperties.property("sonar.junit.reportPaths", config.junit) }
        verify(exactly = 1) { sonarProperties.property("sonar.kotlin.detekt.reportPaths", config.detekt) }
    }

    @Test
    fun `Given configure is called with an analysis Config it will not exclude subprojects`() {
        // Given
        val project: Project = mockk()
        val subProject: Project = mockk()
        val subprojectName: String = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val config = QualityGateConfiguration(
            projectKey = fixture(),
            organization = fixture(),
            host = fixture(),
            encoding = fixture(),
            jacoco = fixture(),
            junit = fixture(),
            detekt = fixture(),
            exclude = fixture(),
        )
        val sonarExtension: SonarExtension = mockk(relaxed = true)

        extension.qualityGate.set(config)

        every { project.plugins.apply(any()) } returns mockk()
        every { project.extensions.configure(SonarExtension::class.java, any()) } returns mockk()
        every { subProject.name } returns subprojectName

        invokeGradleAction(
            subProject,
        ) { probe ->
            project.subprojects(probe)
        }

        invokeGradleAction(
            sonarExtension,
            sonarExtension,
        ) { probe ->
            subProject.extensions.configure(SonarExtension::class.java, probe)
        }

        // When
        Sonarqube.configure(project, extension)

        // Then
        verify(exactly = 0) { sonarExtension.isSkipProject = true }
    }

    @Test
    fun `Given configure is called with an analysis Config it excludes subprojects`() {
        // Given
        val project: Project = mockk()
        val subProject: Project = mockk()
        val subprojectName: String = fixture()
        val extension = createExtension<AntibytesQualityExtension>()

        val config = QualityGateConfiguration(
            projectKey = fixture(),
            organization = fixture(),
            host = fixture(),
            encoding = fixture(),
            jacoco = fixture(),
            junit = fixture(),
            detekt = fixture(),
            exclude = setOf(subprojectName),
        )
        val sonarExtension: SonarExtension = mockk(relaxed = true)

        extension.qualityGate.set(config)

        every { project.plugins.apply(any()) } returns mockk()
        every { project.extensions.configure(SonarExtension::class.java, any()) } returns mockk()
        every { subProject.name } returns subprojectName

        invokeGradleAction(
            subProject,
        ) { probe ->
            project.subprojects(probe)
        }

        invokeGradleAction(
            sonarExtension,
            sonarExtension,
        ) { probe ->
            subProject.extensions.configure(SonarExtension::class.java, probe)
        }

        // When
        Sonarqube.configure(project, extension)

        // Then
        verify(exactly = 1) { sonarExtension.isSkipProject = true }
    }
}
