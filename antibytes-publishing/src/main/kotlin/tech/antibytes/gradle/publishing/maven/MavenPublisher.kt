/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.maven

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomContributor
import org.gradle.api.publish.maven.MavenPomContributorSpec
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPomLicense
import org.gradle.api.publish.maven.MavenPomScm
import org.gradle.api.publish.maven.MavenPublication
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingApiContract.CustomArtifact
import tech.antibytes.gradle.publishing.PublishingApiContract.Type
import tech.antibytes.gradle.publishing.publisher.PublisherContract

internal object MavenPublisher : PublisherContract.MavenPublisher {
    private fun setPublicationProperties(
        publication: MavenPublication,
        configuration: PublishingApiContract.PackageConfiguration,
        docs: Task?,
        version: String,
    ) {
        if (configuration.groupId is String) {
            publication.groupId = configuration.groupId
        }

        if (configuration.artifactId is String) {
            publication.artifactId = configuration.artifactId
        }

        if (docs != null && configuration.type != Type.CUSTOM_ARTIFACT) {
            publication.artifact(docs)
        }

        publication.version = version
    }

    private fun setPomProperties(
        pom: MavenPom,
        configuration: PublishingApiContract.PomConfiguration,
    ) {
        pom.name.set(configuration.name)
        if (configuration.packageing != null) {
            pom.packaging = configuration.packageing
        }
        pom.description.set(configuration.description)
        pom.inceptionYear.set(configuration.year.toString())
        pom.url.set(configuration.url)
        pom.properties.set(configuration.additionalInformation)
    }

    private fun setDevelopers(
        developers: MavenPomDeveloperSpec,
        configurations: List<PublishingApiContract.DeveloperConfiguration>,
    ) {
        configurations.forEach { configuration ->
            developers.developer { setDeveloper(this, configuration) }
        }
    }

    private fun setDeveloper(
        developer: MavenPomDeveloper,
        configuration: PublishingApiContract.DeveloperConfiguration,
    ) {
        developer.id.set(configuration.id)
        developer.name.set(configuration.name)
        developer.email.set(configuration.email)
        if (configuration.url is String) {
            developer.url.set(configuration.url)
        }
        developer.properties.set(configuration.additionalInformation)
    }

    private fun setContributors(
        contributors: MavenPomContributorSpec,
        configurations: List<PublishingApiContract.ContributorConfiguration>,
    ) {
        configurations.forEach { configuration ->
            contributors.contributor { setContributor(this, configuration) }
        }
    }

    private fun setContributor(
        contributor: MavenPomContributor,
        configuration: PublishingApiContract.ContributorConfiguration,
    ) {
        contributor.name.set(configuration.name)
        contributor.email.set(configuration.email)
        if (configuration.url is String) {
            contributor.url.set(configuration.url)
        }
        contributor.properties.set(configuration.additionalInformation)
    }

    private fun setLicense(
        license: MavenPomLicense,
        configuration: PublishingApiContract.LicenseConfiguration,
    ) {
        license.name.set(configuration.name)
        license.url.set(configuration.url)
        license.distribution.set(configuration.distribution)
    }

    private fun setSourceControlManagement(
        sourceControlManagement: MavenPomScm,
        configuration: PublishingApiContract.SourceControlConfiguration,
    ) {
        sourceControlManagement.connection.set(configuration.connection)
        sourceControlManagement.developerConnection.set(configuration.developerConnection)
        sourceControlManagement.url.set(configuration.url)
    }

    private fun PublicationContainer.configureComponent(project: Project, component: String) {
        val publication = create(project.name, MavenPublication::class.java)
        publication.from(project.components.asMap[component])
    }

    private fun PublicationContainer.configureArtifact(
        project: Project,
        customArtifacts: List<CustomArtifact<out Any>>,
    ) {
        val publication = create(project.name, MavenPublication::class.java)
        customArtifacts.forEach { customArtifact ->
            publication.artifact(customArtifact.handle) {
                extension = customArtifact.extension
                classifier = customArtifact.classifier
            }
        }
    }

    override fun configure(
        project: Project,
        configuration: PublishingApiContract.PackageConfiguration,
        docs: Task?,
        version: String,
    ) {
        project.extensions.configure(PublishingExtension::class.java) {
            publications {
                @Suppress("UNCHECKED_CAST")
                when (configuration.type) {
                    Type.PURE_JAVA -> configureComponent(project, "java")
                    Type.VERSION_CATALOG -> configureComponent(project, "versionCatalog")
                    Type.CUSTOM_COMPONENT -> configureComponent(project, configuration.custom as String)
                    Type.CUSTOM_ARTIFACT -> configureArtifact(
                        project,
                        configuration.custom as List<CustomArtifact<out Any>>,
                    )
                    Type.DEFAULT -> { /* Do nothing */ }
                }

                withType(MavenPublication::class.java) {
                    setPublicationProperties(
                        this,
                        configuration,
                        docs,
                        version,
                    )

                    pom {
                        setPomProperties(
                            this,
                            configuration.pom,
                        )

                        developers {
                            setDevelopers(
                                this,
                                configuration.developers,
                            )
                        }

                        contributors {
                            setContributors(
                                this,
                                configuration.contributors,
                            )
                        }

                        licenses {
                            license {
                                setLicense(
                                    this,
                                    configuration.license,
                                )
                            }
                        }

                        scm {
                            setSourceControlManagement(
                                this,
                                configuration.scm,
                            )
                        }
                    }
                }
            }
        }
    }
}
