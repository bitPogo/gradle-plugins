/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.dependency.ensureKotlinVersion

plugins {
    id("tech.antibytes.gradle.plugin.script.quality-spotless")
    id("tech.antibytes.gradle.plugin.script.versioning")
    id("tech.antibytes.gradle.plugin.script.publishing")

    id("tech.antibytes.gradle.dependency.catalog")
    id("tech.antibytes.gradle.dependency.helper.local")
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    ensureKotlinVersion(libs.versions.kotlin.get())
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = libs.versions.gradle.get()
    distributionType = Wrapper.DistributionType.ALL
}
