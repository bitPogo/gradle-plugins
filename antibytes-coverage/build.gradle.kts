/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
import tech.antibytes.gradle.plugin.dependency.Version
import tech.antibytes.gradle.plugin.dependency.Dependency

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco

    id("tech.antibytes.gradle.plugin.script.maven-package")
}

jacoco {
    version = Version.gradle.jacoco
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(Dependency.gradle.kotlin)
    implementation(Dependency.gradle.android)
    implementation(Dependency.gradle.jacoco)
    implementation(project(":antibytes-gradle-utils"))

    testImplementation(Dependency.test.kotlinTest)
    testImplementation(platform(Dependency.test.junit))
    testImplementation(Dependency.test.jupiter)
    testImplementation(Dependency.test.mockk)
    testImplementation(Dependency.test.fixture)
    testImplementation(project(":antibytes-test-utils"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

gradlePlugin {
    plugins.register("${LibraryConfig.group}.coverage") {
        group = LibraryConfig.group
        id = "${LibraryConfig.group}.gradle.coverage"
        displayName = "${id}.gradle.plugin"
        implementationClass = "tech.antibytes.gradle.coverage.AntiBytesCoverage"
        description = "Coverage Plugin for Kotlin Multiplatform project of Antibytes"
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
