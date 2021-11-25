/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

includeBuild("localPlugin/dependency")

plugins {
    id("com.gradle.enterprise") version("3.7")
}

include(
    ":antibytes-coverage",
    ":antibytes-plugin-test",
    ":antibytes-dependency",
    ":antibytes-publishing",
    ":antibytes-configuration",
    ":antibytes-gradle-utils"
)

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "gradle-plugins"
