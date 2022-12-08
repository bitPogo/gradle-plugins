/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.kotlin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.component.local") {
        id = "tech.antibytes.gradle.component.local"
        implementationClass = "tech.antibytes.gradle.component.AntiBytesCustomComponent"
        displayName = "${id}.gradle.plugin"
        description = "Custom Components for Antibytes projects"
        version = "0.1.0"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.check {

}
