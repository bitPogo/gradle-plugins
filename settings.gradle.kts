/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("antibytes") {
            from(files("./gradle/antibytes.catalog.toml"))
        }
    }
}

includeBuild("runtime-configuration")
includeBuild("dependency-helper")
includeBuild("coverage")
includeBuild("publishing")
includeBuild("versioning")
includeBuild("dependency-bridge")
includeBuild("shared-version-catalog")
includeBuild("quality")
includeBuild("custom-component")

include(
    ":antibytes-android-configuration",
    ":antibytes-android-application-configuration",
    ":antibytes-android-library-configuration",
    ":antibytes-dokka-configuration",
    ":antibytes-kmp-configuration",
    ":antibytes-coverage",
    ":antibytes-gradle-utils",
    ":antibytes-gradle-test-utils",
    ":antibytes-dependency",
    ":antibytes-dependency-bridge",
    ":antibytes-dependency-catalog",
    ":antibytes-dependency-catalog-builder",
    ":antibytes-dependency-helper",
    ":antibytes-publishing",
    ":antibytes-versioning",
    ":antibytes-grammar-tools",
    ":antibytes-runtime-configuration",
    ":antibytes-quality",
    ":antibytes-detekt-configuration",
    ":antibytes-custom-component",
    ":antibytes-mkdocs",
    ":antibytes-dependency-settings"
)

buildCache {
    local {
        isEnabled = false
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "gradle-plugins"
