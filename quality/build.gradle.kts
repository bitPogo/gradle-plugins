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

dependencies {
    implementation(libs.kotlin)
    implementation(libs.spotless)
    implementation(libs.ktlint)
    implementation(libs.detekt)
    implementation(libs.sonarqube)
    implementation(libs.stableApi)
    implementation(project(":utils"))
    implementation(project(":helper"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.quality.local") {
        id = "tech.antibytes.gradle.quality.local"
        implementationClass = "tech.antibytes.gradle.quality.AntiBytesQualityLocal"
    }
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "src-plugin/main/kotlin",
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
            "remoteDetektConfig" to antibytes.gradle.antibytes.detektConfiguration.get().toString(),
        )
    )
}

tasks.withType<KotlinCompile> {
    dependsOn(provideConfig)
}
