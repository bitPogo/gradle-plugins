/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco

    id("tech.antibytes.gradle.plugin.script.maven-package")
}

jacoco {
    version = "0.8.7"
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

object Version {
    const val kotlin = "1.5.31"
    const val junit = "5.8.1"
    const val publishing = "5.11.0.202103091610-r"
    const val versioning = "0.12.2" // see: https://github.com/palantir/gradle-git-version/issues/353
    const val mockk = "1.12.0"
    const val fixture = "1.2.0"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
    implementation("org.eclipse.jgit:org.eclipse.jgit:${Version.publishing}")
    implementation("com.palantir.gradle.gitversion:gradle-git-version:${Version.versioning}")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}")
    testImplementation(platform("org.junit:junit-bom:${Version.junit}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:${Version.mockk}")
    testImplementation("com.appmattus.fixture:fixture:${Version.fixture}")
    testImplementation(project(":antibytes-plugin-test"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

gradlePlugin {
    plugins.register("${LibraryConfig.group}.antibytes-publishing") {
        group = LibraryConfig.group
        id = "${LibraryConfig.group}.antibytes-publishing"
        displayName = "${id}.gradle.plugin"
        implementationClass = "tech.antibytes.gradle.publishing.AntiBytesPublishing"
        description = "Publishing tasks for Antibytes projects"
        version = "0.1.0"
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.named("test"))

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(true)

        html.outputLocation.set(
            layout.buildDirectory.dir("reports/jacoco/test/${project.name}").get().asFile
        )
        csv.outputLocation.set(
            layout.buildDirectory.file("reports/jacoco/test/${project.name}.csv").get().asFile
        )
        xml.outputLocation.set(
            layout.buildDirectory.file("reports/jacoco/test/${project.name}.xml").get().asFile
        )
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.named("jacocoTestReport"))
    violationRules {
        rule {
            enabled = true
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = BigDecimal(0.99)
            }
        }
        rule {
            enabled = true
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = BigDecimal( 0.95)
            }
        }
    }
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}
