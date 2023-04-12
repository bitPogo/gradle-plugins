/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.runtime.local")
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.toString()))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.agp)
    implementation(libs.jacoco)
    implementation(project(":utils"))
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "build/generated/antibytes/main/kotlin",
        )
    }
}

val provideConfig: AntiBytesMainConfigurationTask by tasks.creating(AntiBytesMainConfigurationTask::class.java) {
    mustRunAfter("clean")

    packageName.set("tech.antibytes.gradle.coverage.config")
    stringFields.set(
        mapOf(
            "jacoco" to libs.versions.jacoco.get(),
        )
    )
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.coverage.local") {
        id = "tech.antibytes.gradle.coverage.local"
        implementationClass = "tech.antibytes.gradle.coverage.AntibytesCoverage"
    }
}

tasks.withType<KotlinCompile> {
    dependsOn(
        provideConfig,
    )
}
