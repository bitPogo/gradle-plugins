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
    implementation(libs.kotlin)
    implementation(libs.dependencyUpdate)
    implementation(libs.owasp)
    implementation(libs.mavenArtifacts)
    implementation(project(":utils"))
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

gradlePlugin {
    plugins.register("tech.antibytes.gradle.dependency.helper.local") {
        id = "tech.antibytes.gradle.dependency.helper.local"
        implementationClass = "tech.antibytes.gradle.dependency.helper.AntibytesDependencyHelper"
    }
}

tasks.withType<KotlinCompile> {
    dependsOn(provideConfig)
}
