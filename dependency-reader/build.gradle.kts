/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */


plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.gson)

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.local") {
        id = "tech.antibytes.gradle.local"
        implementationClass = "tech.antibytes.gradle.local.DependencyPlugin"
    }
}

tasks.test {
    useJUnitPlatform()
}
