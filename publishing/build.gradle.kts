/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */


plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.configuration.java.local")
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
    implementation(projects.utilsPublishing)
    implementation(projects.versioningPublishing)
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.publishing.local") {
        id = "tech.antibytes.gradle.publishing.local"
        implementationClass = "tech.antibytes.gradle.publishing.AntibytesPublishing"
    }
}
