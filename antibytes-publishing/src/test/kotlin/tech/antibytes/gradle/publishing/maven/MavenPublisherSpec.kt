/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.maven

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomContributor
import org.gradle.api.publish.maven.MavenPomContributorSpec
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPomLicense
import org.gradle.api.publish.maven.MavenPomLicenseSpec
import org.gradle.api.publish.maven.MavenPomScm
import org.gradle.api.publish.maven.MavenPublication
import org.junit.Test
import tech.antibytes.gradle.publishing.invokeGradleAction
import tech.antibytes.gradle.publishing.publicApi.ContributorConfiguration
import tech.antibytes.gradle.publishing.publicApi.DeveloperConfiguration
import tech.antibytes.gradle.publishing.publicApi.LicenseConfiguration
import tech.antibytes.gradle.publishing.publicApi.PackageConfiguration
import tech.antibytes.gradle.publishing.publicApi.PomConfiguration
import tech.antibytes.gradle.publishing.publicApi.SourceControlConfiguration
import kotlin.test.assertTrue

class MavenPublisherSpec {
    private val fixture = kotlinFixture()

    private val registryTestConfig = PackageConfiguration(
        artifactId = null,
        groupId = null,
        pom = mockk(relaxed = true),
        developers = mockk(),
        contributors = mockk(),
        license = mockk(),
        scm = mockk(),
    )

    @Test
    fun `It fulfils MavenPublisher`() {
        val publisher: Any = MavenPublisher

        assertTrue(publisher is MavenContract.MavenPublisher)
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it ignores the Publication properties if they are null`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )

        val configuration = registryTestConfig.copy(
            artifactId = null,
            groupId = null
        )

        // When
        MavenPublisher.configure(project, configuration, fixture())

        // Then
        verify(exactly = 0) { publication.artifactId = any() }
        verify(exactly = 0) { publication.groupId = any() }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the Publication`() {
        // Given
        val artifactId: String = fixture()
        val groupId: String = fixture()
        val version: String = fixture()

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            groupId = groupId,
        )

        // When
        MavenPublisher.configure(project, configuration, version)

        // Then
        verify(exactly = 1) { publication.artifactId = artifactId }
        verify(exactly = 1) { publication.groupId = groupId }
        verify(exactly = 1) { publication.version = version }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the POM`() {
        // Given
        val artifactId: String = fixture()
        val name: String = fixture()
        val groupId: String = fixture()
        val description: String = fixture()
        val year: Int = fixture()
        val url = "http://example.org/${fixture<String>()}"
        val additionalProperties = fixture<Map<String, String>>()

        val pomConfiguration = PomConfiguration(
            name = name,
            description = description,
            year = year,
            url = url,
            additionalInformation = additionalProperties
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val pom: MavenPom = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )
        invokeGradleAction(
            { probe -> publication.pom(probe) },
            pom,
            mockk()
        )

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            groupId = groupId,
            pom = pomConfiguration
        )

        // When
        MavenPublisher.configure(project, configuration, fixture())

        // Then
        verify(exactly = 1) { pom.name.set(name) }
        verify(exactly = 1) { pom.description.set(description) }
        verify(exactly = 1) { pom.inceptionYear.set(year.toString()) }
        verify(exactly = 1) { pom.url.set(url) }
        verify(exactly = 1) { pom.properties.set(additionalProperties) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the Developer configuration`() {
        // Given
        val devConfiguration = DeveloperConfiguration(
            id = fixture(),
            name = fixture(),
            email = fixture(),
            url = fixture<String>(),
            additionalInformation = fixture()
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val pom: MavenPom = mockk(relaxed = true)
        val developerSpec: MavenPomDeveloperSpec = mockk()
        val developer: MavenPomDeveloper = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )
        invokeGradleAction(
            { probe -> publication.pom(probe) },
            pom,
            mockk()
        )
        invokeGradleAction(
            { probe -> pom.developers(probe) },
            developerSpec,
            mockk()
        )
        invokeGradleAction(
            { probe -> developerSpec.developer(probe) },
            developer,
            mockk()
        )

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(
                developers = listOf(devConfiguration, devConfiguration)
            ),
            fixture()
        )

        // Then
        verify(exactly = 2) { developer.id.set(devConfiguration.id) }
        verify(exactly = 2) { developer.name.set(devConfiguration.name) }
        verify(exactly = 2) { developer.email.set(devConfiguration.email) }
        verify(exactly = 2) { developer.url.set(devConfiguration.url) }
        verify(exactly = 2) { developer.properties.set(devConfiguration.additionalInformation) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it ignores optional Developer configuration, if they are not set`() {
        // Given
        val devConfiguration = DeveloperConfiguration(
            id = fixture(),
            name = fixture(),
            email = fixture(),
            url = null,
            additionalInformation = fixture()
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val pom: MavenPom = mockk(relaxed = true)
        val developerSpec: MavenPomDeveloperSpec = mockk()
        val developer: MavenPomDeveloper = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )
        invokeGradleAction(
            { probe -> publication.pom(probe) },
            pom,
            mockk()
        )
        invokeGradleAction(
            { probe -> pom.developers(probe) },
            developerSpec,
            mockk()
        )
        invokeGradleAction(
            { probe -> developerSpec.developer(probe) },
            developer,
            mockk()
        )

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(
                developers = listOf(devConfiguration, devConfiguration)
            ),
            fixture()
        )

        // Then
        verify(exactly = 0) { developer.url.set(devConfiguration.url) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the Contributor configuration`() {
        // Given
        val contribConfiguration = ContributorConfiguration(
            name = fixture(),
            email = fixture(),
            url = fixture<String>(),
            additionalInformation = fixture(),
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val pom: MavenPom = mockk(relaxed = true)
        val contributorSpec: MavenPomContributorSpec = mockk()
        val contributor: MavenPomContributor = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )
        invokeGradleAction(
            { probe -> publication.pom(probe) },
            pom,
            mockk()
        )
        invokeGradleAction(
            { probe -> pom.contributors(probe) },
            contributorSpec,
            mockk()
        )
        invokeGradleAction(
            { probe -> contributorSpec.contributor(probe) },
            contributor,
            mockk()
        )

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(
                contributors = listOf(contribConfiguration, contribConfiguration)
            ),
            fixture()
        )

        // Then
        verify(exactly = 2) { contributor.name.set(contribConfiguration.name) }
        verify(exactly = 2) { contributor.email.set(contribConfiguration.email) }
        verify(exactly = 2) { contributor.url.set(contribConfiguration.url) }
        verify(exactly = 2) { contributor.properties.set(contribConfiguration.additionalInformation) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it ignores optional Contributor configuration, if they are not set`() {
        // Given
        val contribConfiguration = ContributorConfiguration(
            name = fixture(),
            email = fixture(),
            url = null,
            additionalInformation = fixture(),
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val pom: MavenPom = mockk(relaxed = true)
        val contributorSpec: MavenPomContributorSpec = mockk()
        val contributor: MavenPomContributor = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )
        invokeGradleAction(
            { probe -> publication.pom(probe) },
            pom,
            mockk()
        )
        invokeGradleAction(
            { probe -> pom.contributors(probe) },
            contributorSpec,
            mockk()
        )
        invokeGradleAction(
            { probe -> contributorSpec.contributor(probe) },
            contributor,
            mockk()
        )

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(
                contributors = listOf(contribConfiguration, contribConfiguration)
            ),
            fixture()
        )

        // Then
        verify(exactly = 0) { contributor.url.set(contribConfiguration.url) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the License configuration`() {
        // Given
        val licenseConfiguration = LicenseConfiguration(
            name = fixture(),
            url = fixture(),
            distribution = fixture()
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val pom: MavenPom = mockk(relaxed = true)
        val licenseSpec: MavenPomLicenseSpec = mockk()
        val license: MavenPomLicense = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )
        invokeGradleAction(
            { probe -> publication.pom(probe) },
            pom,
            mockk()
        )
        invokeGradleAction(
            { probe -> pom.licenses(probe) },
            licenseSpec,
            mockk()
        )
        invokeGradleAction(
            { probe -> licenseSpec.license(probe) },
            license,
            mockk()
        )

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(license = licenseConfiguration),
            fixture()
        )

        // Then
        verify(exactly = 1) { license.name.set(licenseConfiguration.name) }
        verify(exactly = 1) { license.distribution.set(licenseConfiguration.distribution) }
        verify(exactly = 1) { license.url.set(licenseConfiguration.url) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the Source Control configuration`() {
        // Given
        val sourceControlConfiguration = SourceControlConfiguration(
            connection = fixture(),
            url = fixture(),
            developerConnection = fixture(),
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val pom: MavenPom = mockk(relaxed = true)
        val scm: MavenPomScm = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )
        invokeGradleAction(
            { probe -> publication.pom(probe) },
            pom,
            mockk()
        )
        invokeGradleAction(
            { probe -> pom.scm(probe) },
            scm,
            mockk()
        )

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(scm = sourceControlConfiguration),
            fixture()
        )

        // Then
        verify(exactly = 1) { scm.connection.set(sourceControlConfiguration.connection) }
        verify(exactly = 1) { scm.developerConnection.set(sourceControlConfiguration.developerConnection) }
        verify(exactly = 1) { scm.url.set(sourceControlConfiguration.url) }
    }
}
