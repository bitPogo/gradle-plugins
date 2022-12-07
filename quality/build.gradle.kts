/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
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

dependencies {
    implementation(libs.kotlin)
    implementation(libs.spotless)
    implementation(libs.ktlint)
    implementation(libs.detekt)
    implementation(libs.sonarqube)
    implementation(libs.stableApi)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.quality.local") {
        id = "tech.antibytes.gradle.quality.local"
        implementationClass = "tech.antibytes.gradle.quality.AntiBytesQuality"
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

    packageName.set("tech.antibytes.gradle.quality.config")
    stringFields.set(
        mapOf(
            "detektVersion" to libs.versions.detekt.get(),
            "ktlintVersion" to libs.versions.ktlint.get(),
        )
    )
}

tasks.withType<KotlinCompile> {
    dependsOn(provideConfig)
}
