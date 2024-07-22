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

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.versioning)
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.versioning.local") {
        id = "tech.antibytes.gradle.versioning.local"
        implementationClass = "tech.antibytes.gradle.versioning.AntibytesVersioning"
    }
}
