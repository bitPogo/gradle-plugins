/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
import tech.antibytes.gradle.plugin.dependency.Dependency
import tech.antibytes.gradle.plugin.dependency.Version

plugins {
    `kotlin-dsl`
    `java-library`
    jacoco

    id("tech.antibytes.gradle.plugin.script.maven-package")
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

jacoco {
    toolVersion = Version.gradle.jacoco
}

dependencies {
    implementation(Dependency.gradle.kotlin)
    implementation(Dependency.test.mockk)
    implementation(Dependency.test.fixture)

    testImplementation(Dependency.test.kotlinTest)
    testImplementation(platform(Dependency.test.junit))
    testImplementation(Dependency.test.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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
                minimum = BigDecimal( 0.45)
            }
        }
    }
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}
