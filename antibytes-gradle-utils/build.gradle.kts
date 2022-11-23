/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig

plugins {
    `kotlin-dsl`
    `java-library`
    jacoco

    id("tech.antibytes.gradle.plugin.script.maven-package")
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

dependencies {
    implementation(libs.kotlin)

    testImplementation(libs.mockk)
    testImplementation(libs.jvmFixture)
    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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
                minimum = BigDecimal( 0.99)
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}
