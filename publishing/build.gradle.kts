/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */


plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
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
    implementation(libs.publishing)
    implementation(libs.versioning)
    implementation(project(":utils"))
    implementation(project(":versioning"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.publishing.local") {
        id = "tech.antibytes.gradle.publishing.local"
        implementationClass = "tech.antibytes.gradle.publishing.AntiBytesPublishing"
    }
}
