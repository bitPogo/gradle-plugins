/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.dependency.Version

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

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-stdlib-jdk7") {
                useVersion(Version.kotlin)
                because("Avoid resolution conflicts")
            }

            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-stdlib-jdk8") {
                useVersion(Version.kotlin)
                because("Avoid resolution conflicts")
            }

            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-stdlib") {
                useVersion(Version.kotlin)
                because("Avoid resolution conflicts")
            }

            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-stdlib-common") {
                useVersion(Version.kotlin)
                because("Avoid resolution conflicts")
            }

            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-reflect") {
                useVersion(Version.kotlin)
                because("Avoid resolution conflicts")
            }
        }
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.4.1"
    distributionType = Wrapper.DistributionType.ALL
}
