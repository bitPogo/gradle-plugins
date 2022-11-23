/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco

    id("tech.antibytes.gradle.runtime.local")
    id("tech.antibytes.gradle.plugin.script.maven-package")
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(libs.kotlin)
    implementation(libs.dependencyUpdate)
    implementation(libs.owasp)
    implementation(project(":antibytes-gradle-utils"))

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jvmFixture)
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
                minimum = BigDecimal(0.85)
            }
        }
    }
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "build/generated/antibytes/main/kotlin"
        )
    }
}

val provideConfig: AntiBytesMainConfigurationTask by tasks.creating(AntiBytesMainConfigurationTask::class.java) {
    mustRunAfter("clean")

    packageName.set("tech.antibytes.gradle.dependency.config")
    stringFields.set(
        mapOf(
            "kotlin" to libs.versions.kotlin.get(),
        )
    )
}

tasks.withType<KotlinCompile> {
    dependsOn(provideConfig)
}

tasks.test {
    useJUnitPlatform()
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}
