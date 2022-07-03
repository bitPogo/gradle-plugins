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
    toolVersion = Version.gradle.jacoco
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(Dependency.gradle.atomicFu) {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    implementation(Dependency.gradle.android)
    implementation(Dependency.gradle.dependencyUpdate)
    implementation(Dependency.gradle.owasp)
    implementation(Dependency.gradle.ktlint) {
        exclude(group = "ch.qos.logback", module = "logback-classic")
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    implementation(Dependency.gradle.spotless) {
        exclude(group = "org.codehaus.groovy", module = "groovy-xml")
    }
    implementation(project(":antibytes-gradle-utils"))

    testImplementation(Dependency.test.kotlinTest)
    testImplementation(platform(Dependency.test.junit))
    testImplementation(Dependency.test.jupiter)
    testImplementation(Dependency.test.mockk)
    testImplementation(Dependency.test.fixture)
    testImplementation(project(":antibytes-gradle-test-utils"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins.register("${LibraryConfig.group}.gradle.dependency") {
        group = LibraryConfig.group
        id = "${LibraryConfig.group}.gradle.dependency"
        implementationClass = "tech.antibytes.gradle.dependency.AntiBytesDependency"
        displayName = "${id}.gradle.plugin"
        description = "General dependencies for Antibytes projects"
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
        rule { // TODO -> Add Integration Tests
            enabled = false
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = BigDecimal(0.38)
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
