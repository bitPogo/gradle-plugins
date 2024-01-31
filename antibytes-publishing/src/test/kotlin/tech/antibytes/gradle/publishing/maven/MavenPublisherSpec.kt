/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.maven

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.io.File
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.component.SoftwareComponent
import org.gradle.api.component.SoftwareComponentContainer
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenArtifact
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomContributor
import org.gradle.api.publish.maven.MavenPomContributorSpec
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPomLicense
import org.gradle.api.publish.maven.MavenPomLicenseSpec
import org.gradle.api.publish.maven.MavenPomScm
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.api.ContributorConfiguration
import tech.antibytes.gradle.publishing.api.CustomFileArtifact
import tech.antibytes.gradle.publishing.api.DeveloperConfiguration
import tech.antibytes.gradle.publishing.api.LicenseConfiguration
import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration
import tech.antibytes.gradle.publishing.api.SourceControlConfiguration
import tech.antibytes.gradle.publishing.publisher.PublisherContract
import tech.antibytes.gradle.test.invokeGradleAction

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

        assertTrue(publisher is PublisherContract.MavenPublisher)
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

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        val configuration = registryTestConfig.copy(
            artifactId = null,
            groupId = null,
        )

        // When
        MavenPublisher.configure(project, configuration, null, fixture())

        // Then
        verify(exactly = 0) { publication.artifactId = any() }
        verify(exactly = 0) { publication.groupId = any() }
        verify(exactly = 0) { publication.artifact(any()) }
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

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }

        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            groupId = groupId,
        )

        // When
        MavenPublisher.configure(project, configuration, null, version)

        // Then
        verify(exactly = 1) { publication.artifactId = artifactId }
        verify(exactly = 1) { publication.groupId = groupId }
        verify(exactly = 1) { publication.version = version }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the Publication, while creating a Publishing Task`() {
        // Given
        val artifactId: String = fixture()
        val groupId: String = fixture()
        val version: String = fixture()
        val projectName: String = fixture()
        val components: SoftwareComponentContainer = mockk()
        val java: SoftwareComponent = mockk()

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { project.name } returns projectName
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }

        every {
            hint(String::class, 0)
            hint(MavenPublication::class, 1)
            publicationContainer.create(projectName, MavenPublication::class.java)
        } returns publication

        every { project.components } returns components
        every { project.components.asMap["java"] } returns java
        every { publication.from(any()) } just Runs

        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            groupId = groupId,
            type = PublishingApiContract.Type.PURE_JAVA,
        )

        // When
        MavenPublisher.configure(project, configuration, null, version)

        // Then
        verify(exactly = 1) { publication.from(java) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project, it adds the Documentation Artifact if given`() {
        // Given
        val artifactId: String = fixture()
        val groupId: String = fixture()
        val version: String = fixture()
        val projectName: String = fixture()
        val components: SoftwareComponentContainer = mockk()
        val java: SoftwareComponent = mockk()

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val documentation: Jar = mockk()

        every { project.extensions } returns extensions
        every { project.name } returns projectName
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }

        every {
            hint(String::class, 0)
            hint(MavenPublication::class, 1)
            publicationContainer.create(projectName, MavenPublication::class.java)
        } returns publication

        every { project.components } returns components
        every { project.components.asMap["java"] } returns java
        every { publication.from(any()) } just Runs

        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            groupId = groupId,
            type = PublishingApiContract.Type.PURE_JAVA,
        )

        // When
        MavenPublisher.configure(project, configuration, documentation, version)

        // Then
        verify(exactly = 1) { publication.from(java) }
        verify(exactly = 1) { publication.artifact(documentation) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the Publication, while creating a Publishing Task for VersionCatalogs`() {
        // Given
        val artifactId: String = fixture()
        val groupId: String = fixture()
        val version: String = fixture()
        val projectName: String = fixture()
        val components: SoftwareComponentContainer = mockk()
        val versionCatalog: SoftwareComponent = mockk()

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { project.name } returns projectName
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }

        every {
            hint(String::class, 0)
            hint(MavenPublication::class, 1)
            publicationContainer.create(projectName, MavenPublication::class.java)
        } returns publication

        every { project.components } returns components
        every { project.components.asMap["versionCatalog"] } returns versionCatalog
        every { publication.from(any()) } just Runs

        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            groupId = groupId,
            type = PublishingApiContract.Type.VERSION_CATALOG,
        )

        // When
        MavenPublisher.configure(project, configuration, null, version)

        // Then
        verify(exactly = 1) { publication.from(versionCatalog) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project, it adds the Documentation Artifact if given for VersionCatalogs`() {
        // Given
        val artifactId: String = fixture()
        val groupId: String = fixture()
        val version: String = fixture()
        val projectName: String = fixture()
        val components: SoftwareComponentContainer = mockk()
        val versionCatalog: SoftwareComponent = mockk()

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val documentation: Jar = mockk()

        every { project.extensions } returns extensions
        every { project.name } returns projectName
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }

        every {
            hint(String::class, 0)
            hint(MavenPublication::class, 1)
            publicationContainer.create(projectName, MavenPublication::class.java)
        } returns publication

        every { project.components } returns components
        every { project.components.asMap["versionCatalog"] } returns versionCatalog
        every { publication.from(any()) } just Runs

        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            groupId = groupId,
            type = PublishingApiContract.Type.VERSION_CATALOG,
        )

        // When
        MavenPublisher.configure(project, configuration, documentation, version)

        // Then
        verify(exactly = 1) { publication.from(versionCatalog) }
        verify(exactly = 1) { publication.artifact(documentation) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the Publication, while creating a Publishing Task for an Custom Component`() {
        // Given
        val artifactId: String = fixture()
        val groupId: String = fixture()
        val version: String = fixture()
        val projectName: String = fixture()
        val components: SoftwareComponentContainer = mockk()
        val componentKey: String = fixture()
        val component: SoftwareComponent = mockk()

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { project.name } returns projectName
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }

        every {
            hint(String::class, 0)
            hint(MavenPublication::class, 1)
            publicationContainer.create(projectName, MavenPublication::class.java)
        } returns publication

        every { project.components } returns components
        every { project.components.asMap[componentKey] } returns component
        every { publication.from(any()) } just Runs

        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        val configuration = registryTestConfig.copy(
            custom = componentKey,
            artifactId = artifactId,
            groupId = groupId,
            type = PublishingApiContract.Type.CUSTOM_COMPONENT,
        )

        // When
        MavenPublisher.configure(project, configuration, null, version)

        // Then
        verify(exactly = 1) { publication.from(component) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project, it adds the Documentation Artifact if given for an Custom Component`() {
        // Given
        val artifactId: String = fixture()
        val groupId: String = fixture()
        val version: String = fixture()
        val projectName: String = fixture()
        val components: SoftwareComponentContainer = mockk()
        val componentKey: String = fixture()
        val component: SoftwareComponent = mockk()

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val documentation: Jar = mockk()

        every { project.extensions } returns extensions
        every { project.name } returns projectName
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }

        every {
            hint(String::class, 0)
            hint(MavenPublication::class, 1)
            publicationContainer.create(projectName, MavenPublication::class.java)
        } returns publication

        every { project.components } returns components
        every { project.components.asMap[componentKey] } returns component
        every { publication.from(any()) } just Runs

        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        val configuration = registryTestConfig.copy(
            custom = componentKey,
            artifactId = artifactId,
            groupId = groupId,
            type = PublishingApiContract.Type.CUSTOM_COMPONENT,
        )

        // When
        MavenPublisher.configure(project, configuration, documentation, version)

        // Then
        verify(exactly = 1) { publication.from(component) }
        verify(exactly = 1) { publication.artifact(documentation) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the Publication, while creating a Publishing Task for a CustomArtifact`() {
        // Given
        val artifactId: String = fixture()
        val groupId: String = fixture()
        val version: String = fixture()
        val projectName: String = fixture()

        val artifact: MavenArtifact = mockk(relaxed = true)
        val artifactHandle: File = mockk()
        val artifactExtension: String = fixture()
        val artifactClassifier: String = fixture()

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { project.name } returns projectName
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }

        every {
            hint(String::class, 0)
            hint(MavenPublication::class, 1)
            publicationContainer.create(projectName, MavenPublication::class.java)
        } returns publication

        invokeGradleAction(
            artifact,
            artifact,
        ) { probe ->
            publication.artifact(any(), probe)
        }

        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            custom = listOf(
                CustomFileArtifact(
                    handle = artifactHandle,
                    classifier = artifactClassifier,
                    extension = artifactExtension,
                ),
            ),
            groupId = groupId,
            type = PublishingApiContract.Type.CUSTOM_ARTIFACT,
        )

        // When
        MavenPublisher.configure(project, configuration, null, version)

        // Then
        verify(exactly = 1) { publication.artifact(artifactHandle, any()) }
        verify(exactly = 1) { artifact.classifier = artifactClassifier }
        verify(exactly = 1) { artifact.extension = artifactExtension }
    }

    @Test
    fun `Given configureMavenTask is called with a Project, it ignores the Documentation Artifact if a Custom Artifact was given`() {
        // Given
        val artifactId: String = fixture()
        val groupId: String = fixture()
        val version: String = fixture()
        val projectName: String = fixture()

        val artifact: MavenArtifact = mockk(relaxed = true)
        val artifactHandle: File = mockk()
        val artifactExtension: String = fixture()
        val artifactClassifier: String = fixture()

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val documentation: Jar = mockk()

        every { project.extensions } returns extensions
        every { project.name } returns projectName
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }

        every {
            hint(String::class, 0)
            hint(MavenPublication::class, 1)
            publicationContainer.create(projectName, MavenPublication::class.java)
        } returns publication

        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }

        invokeGradleAction(
            artifact,
            artifact,
        ) { probe ->
            publication.artifact(any(), probe)
        }

        val configuration = registryTestConfig.copy(
            custom = listOf(
                CustomFileArtifact(
                    handle = artifactHandle,
                    classifier = artifactClassifier,
                    extension = artifactExtension,
                ),
            ),
            artifactId = artifactId,
            groupId = groupId,
            type = PublishingApiContract.Type.CUSTOM_ARTIFACT,
        )

        // When
        MavenPublisher.configure(project, configuration, documentation, version)

        // Then
        verify(exactly = 1) { publication.artifact(artifactHandle, any()) }
        verify(exactly = 1) { artifact.classifier = artifactClassifier }
        verify(exactly = 1) { artifact.extension = artifactExtension }
        verify(exactly = 0) { publication.artifact(documentation) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the POM without packageing`() {
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
            additionalInformation = additionalProperties,
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val pom: MavenPom = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }
        invokeGradleAction(
            pom,
            mockk(),
        ) { probe ->
            publication.pom(probe)
        }

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            groupId = groupId,
            pom = pomConfiguration,
        )

        // When
        MavenPublisher.configure(project, configuration, null, fixture())

        // Then
        verify(exactly = 1) { pom.name.set(name) }
        verify(exactly = 1) { pom.description.set(description) }
        verify(exactly = 1) { pom.inceptionYear.set(year.toString()) }
        verify(exactly = 1) { pom.url.set(url) }
        verify(exactly = 1) { pom.properties.set(additionalProperties) }
    }

    @Test
    fun `Given configureMavenTask is called with a Project and a PackageRegistry, it sets up the POM with packageing`() {
        // Given
        val artifactId: String = fixture()
        val name: String = fixture()
        val groupId: String = fixture()
        val description: String = fixture()
        val year: Int = fixture()
        val url = "http://example.org/${fixture<String>()}"
        val additionalProperties = fixture<Map<String, String>>()
        val packageing: String = fixture()

        val pomConfiguration = PomConfiguration(
            name = name,
            description = description,
            year = year,
            url = url,
            packageing = packageing,
            additionalInformation = additionalProperties,
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)
        val pom: MavenPom = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }
        invokeGradleAction(
            pom,
            mockk(),
        ) { probe ->
            publication.pom(probe)
        }

        val configuration = registryTestConfig.copy(
            artifactId = artifactId,
            groupId = groupId,
            pom = pomConfiguration,
        )

        // When
        MavenPublisher.configure(project, configuration, null, fixture())

        // Then
        verify(exactly = 1) { pom.name.set(name) }
        verify(exactly = 1) { pom.description.set(description) }
        verify(exactly = 1) { pom.inceptionYear.set(year.toString()) }
        verify(exactly = 1) { pom.packaging = packageing }
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
            additionalInformation = fixture(),
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

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }
        invokeGradleAction(
            pom,
            mockk(),
        ) { probe ->
            publication.pom(probe)
        }
        invokeGradleAction(
            developerSpec,
            mockk(),
        ) { probe ->
            pom.developers(probe)
        }
        invokeGradleAction(
            developer,
            mockk(),
        ) { probe ->
            developerSpec.developer(probe)
        }

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(
                developers = listOf(devConfiguration, devConfiguration),
            ),
            null,
            fixture(),
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
            additionalInformation = fixture(),
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

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }
        invokeGradleAction(
            pom,
            mockk(),
        ) { probe ->
            publication.pom(probe)
        }
        invokeGradleAction(
            developerSpec,
            mockk(),
        ) { probe ->
            pom.developers(probe)
        }
        invokeGradleAction(
            developer,
            mockk(),
        ) { probe ->
            developerSpec.developer(probe)
        }

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(
                developers = listOf(devConfiguration, devConfiguration),
            ),
            null,
            fixture(),
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

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }
        invokeGradleAction(
            pom,
            mockk(),
        ) { probe ->
            publication.pom(probe)
        }
        invokeGradleAction(
            contributorSpec,
            mockk(),
        ) { probe ->
            pom.contributors(probe)
        }
        invokeGradleAction(
            contributor,
            mockk(),
        ) { probe ->
            contributorSpec.contributor(probe)
        }

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(
                contributors = listOf(contribConfiguration, contribConfiguration),
            ),
            null,
            fixture(),
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

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }
        invokeGradleAction(
            pom,
            mockk(),
        ) { probe ->
            publication.pom(probe)
        }
        invokeGradleAction(
            contributorSpec,
            mockk(),
        ) { probe ->
            pom.contributors(probe)
        }
        invokeGradleAction(
            contributor,
            mockk(),
        ) { probe ->
            contributorSpec.contributor(probe)
        }

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(
                contributors = listOf(contribConfiguration, contribConfiguration),
            ),
            null,
            fixture(),
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
            distribution = fixture(),
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

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }
        invokeGradleAction(
            pom,
            mockk(),
        ) { probe ->
            publication.pom(probe)
        }
        invokeGradleAction(
            licenseSpec,
            mockk(),
        ) { probe ->
            pom.licenses(probe)
        }
        invokeGradleAction(
            license,
            mockk(),
        ) { probe ->
            licenseSpec.license(probe)
        }

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(license = licenseConfiguration),
            null,
            fixture(),
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

        invokeGradleAction(publishingExtension) { probe ->
            extensions.configure(PublishingExtension::class.java, probe)
        }
        invokeGradleAction(
            publicationContainer,
            mockk(),
        ) { probe ->
            publishingExtension.publications(probe)
        }
        invokeGradleAction(
            publication,
            mockk(),
        ) { probe ->
            publicationContainer.withType(MavenPublication::class.java, probe)
        }
        invokeGradleAction(
            pom,
            mockk(),
        ) { probe ->
            publication.pom(probe)
        }
        invokeGradleAction(
            scm,
            mockk(),
        ) { probe ->
            pom.scm(probe)
        }

        // When
        MavenPublisher.configure(
            project,
            registryTestConfig.copy(scm = sourceControlConfiguration),
            null,
            fixture(),
        )

        // Then
        verify(exactly = 1) { scm.connection.set(sourceControlConfiguration.connection) }
        verify(exactly = 1) { scm.developerConnection.set(sourceControlConfiguration.developerConnection) }
        verify(exactly = 1) { scm.url.set(sourceControlConfiguration.url) }
    }
}
