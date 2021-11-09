/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.script

import tech.antibytes.gradle.plugin.config.LibraryConfig
import org.gradle.api.publish.maven.MavenPublication

/**
 * Install:
 *
 * Just add id("tech.antibytes.gradle.plugin.maven-package") to your project module build.gradle.kts plugins section
 *
 * plugins {
 *     id("tech.antibytes.gradle.plugin.maven-package")
 * }
 *
 * Usage:
 *
 * To publish to a variant just run:
 * - ./gradlew publishAllPublicationsTo**
 *
 * This requires tech.antibytes.gradle.plugin.LibraryConfig to be configured
 */
plugins {
    `maven-publish`
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            setUrl("https://maven.pkg.github.com/${LibraryConfig.githubOwner}/${LibraryConfig.githubRepository}")
            credentials {
                username = (project.findProperty("gpr.user")
                    ?: System.getenv("PACKAGE_REGISTRY_UPLOAD_USERNAME")).toString()
                password = (project.findProperty("gpr.key")
                    ?: System.getenv("PACKAGE_REGISTRY_UPLOAD_TOKEN")).toString()
            }
        }

        val target = "file://${project.rootProject.buildDir.absolutePath}/gitPublish"

        maven {
            name = "ReleasePackages"
            setUrl("$target/maven-releases/releases")
        }

        maven {
            name = "SnapshotPackages"
            setUrl("$target/maven-snapshots/snapshots")
        }

        maven {
            name = "DevPackages"
            setUrl("$target/maven-dev/dev")
        }
    }

    publications {
        withType<MavenPublication> {
            groupId = LibraryConfig.PublishConfig.groupId

            pom {
                description.set(LibraryConfig.PublishConfig.description)
                url.set(LibraryConfig.PublishConfig.url)
                inceptionYear.set(LibraryConfig.PublishConfig.year)

                licenses {
                    license {
                        name.set(LibraryConfig.PublishConfig.licenseName)
                        url.set(LibraryConfig.PublishConfig.licenseUrl)
                        distribution.set(LibraryConfig.PublishConfig.licenseDistribution)
                    }
                }

                developers {
                    developer {
                        id.set(LibraryConfig.PublishConfig.developerId)
                        name.set(LibraryConfig.PublishConfig.developerName)
                        email.set(LibraryConfig.PublishConfig.developerEmail)
                    }
                }

                scm {
                    connection.set(LibraryConfig.PublishConfig.scmConnection)
                    developerConnection.set(LibraryConfig.PublishConfig.scmDeveloperConnection)
                    url.set(LibraryConfig.PublishConfig.scmUrl)
                }
            }
        }
    }
}
