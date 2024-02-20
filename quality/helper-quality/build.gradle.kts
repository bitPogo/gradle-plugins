/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.runtime.local")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(libs.versions.java.get()).toString()))
    }

    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

dependencies {
    implementation(libs.kotlin)
    implementation(libs.dependencyUpdate)
    implementation(libs.owasp)
    implementation(libs.mavenArtifacts)
    implementation(projects.utilsQuality)
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
