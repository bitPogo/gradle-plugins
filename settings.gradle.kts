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

includeBuild("runtime-configuration")
includeBuild("dependency-helper")
includeBuild("coverage")
includeBuild("publishing")
includeBuild("versioning")
includeBuild("dependency-bridge")
includeBuild("shared-version-catalog")

include(
    ":antibytes-configuration",
    ":antibytes-coverage",
    ":antibytes-gradle-test-utils",
    ":antibytes-dependency",
    ":antibytes-dependency-bridge",
    ":antibytes-dependency-catalog",
    ":antibytes-dependency-catalog-builder",
    ":antibytes-dependency-helper",
    ":antibytes-publishing",
    ":antibytes-versioning",
    ":antibytes-gradle-utils",
    ":antibytes-grammar-tools",
    ":antibytes-runtime-configuration",
    ":antibytes-quality"
)

buildCache {
    local {
        isEnabled = false
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "gradle-plugins"
