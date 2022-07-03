/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.dependency.ensureKotlinVersion

plugins {
    id("tech.antibytes.gradle.plugin.dependency")
    id("tech.antibytes.gradle.plugin.script.dependency-update")
    id("tech.antibytes.gradle.plugin.script.quality-spotless")
    id("tech.antibytes.gradle.plugin.script.versioning")
    id("tech.antibytes.gradle.plugin.script.publishing")
    id("org.owasp.dependencycheck")
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

ensureKotlinVersion()

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.5-rc-3"
    distributionType = Wrapper.DistributionType.ALL
}
