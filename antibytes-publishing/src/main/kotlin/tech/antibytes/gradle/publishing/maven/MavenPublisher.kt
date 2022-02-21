/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.maven

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomContributor
import org.gradle.api.publish.maven.MavenPomContributorSpec
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPomLicense
import org.gradle.api.publish.maven.MavenPomScm
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.publisher.PublisherContract

internal object MavenPublisher : PublisherContract.MavenPublisher {
    private fun setPublicationProperties(
        publication: MavenPublication,
        configuration: PublishingApiContract.PackageConfiguration,
        version: String
    ) {
        if (configuration.groupId is String) {
            publication.groupId = configuration.groupId
        }

        if (configuration.artifactId is String) {
            publication.artifactId = configuration.artifactId
        }

        publication.version = version
    }

    private fun setPomProperties(
        pom: MavenPom,
        configuration: PublishingApiContract.PomConfiguration
    ) {
        pom.name.set(configuration.name)
        pom.description.set(configuration.description)
        pom.inceptionYear.set(configuration.year.toString())
        pom.url.set(configuration.url)
        pom.properties.set(configuration.additionalInformation)
    }

    private fun setDevelopers(
        developers: MavenPomDeveloperSpec,
        configurations: List<PublishingApiContract.DeveloperConfiguration>
    ) {
        configurations.forEach { configuration ->
            developers.developer { setDeveloper(this, configuration) }
        }
    }

    private fun setDeveloper(
        developer: MavenPomDeveloper,
        configuration: PublishingApiContract.DeveloperConfiguration
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
        configurations: List<PublishingApiContract.ContributorConfiguration>
    ) {
        configurations.forEach { configuration ->
            contributors.contributor { setContributor(this, configuration) }
        }
    }

    private fun setContributor(
        contributor: MavenPomContributor,
        configuration: PublishingApiContract.ContributorConfiguration
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
        configuration: PublishingApiContract.LicenseConfiguration
    ) {
        license.name.set(configuration.name)
        license.url.set(configuration.url)
        license.distribution.set(configuration.distribution)
    }

    private fun setSourceControlManagement(
        sourceControlManagement: MavenPomScm,
        configuration: PublishingApiContract.SourceControlConfiguration
    ) {
        sourceControlManagement.connection.set(configuration.connection)
        sourceControlManagement.developerConnection.set(configuration.developerConnection)
        sourceControlManagement.url.set(configuration.url)
    }

    override fun configure(
        project: Project,
        configuration: PublishingApiContract.PackageConfiguration,
        version: String
    ) {
        project.extensions.configure(PublishingExtension::class.java) {
            publications {
                if (configuration.isJavaLibrary) {
                    val publication = create(project.name, MavenPublication::class.java)
                    publication.from(project.components.asMap["java"])
                }

                withType(MavenPublication::class.java) {
                    setPublicationProperties(
                        this,
                        configuration,
                        version
                    )

                    pom {
                        setPomProperties(
                            this,
                            configuration.pom
                        )

                        developers {
                            setDevelopers(
                                this,
                                configuration.developers
                            )
                        }

                        contributors {
                            setContributors(
                                this,
                                configuration.contributors
                            )
                        }

                        licenses {
                            license {
                                setLicense(
                                    this,
                                    configuration.license
                                )
                            }
                        }

                        scm {
                            setSourceControlManagement(
                                this,
                                configuration.scm
                            )
                        }
                    }
                }
            }
        }
    }
}
