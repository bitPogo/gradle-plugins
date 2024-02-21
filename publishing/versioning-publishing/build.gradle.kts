/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
plugins {
    `kotlin-dsl`
    `java-library`
    jacoco

    id("tech.antibytes.gradle.configuration.java.local")
}

dependencies {
    implementation(libs.versioning)
}
