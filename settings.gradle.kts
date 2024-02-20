/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

includeBuild("runtime-configuration")
includeBuild("dependency-helper")
includeBuild("dependency-bridge")
includeBuild("custom-component")
includeBuild("shared-version-catalog")
includeBuild("coverage")
includeBuild("publishing")
includeBuild("versioning")
includeBuild("quality")

include(
    ":antibytes-android-configuration",
    ":antibytes-android-application-configuration",
    ":antibytes-android-library-configuration",
    ":antibytes-dokka-configuration",
    ":antibytes-kmp-configuration",
    ":antibytes-java-configuration",
    ":antibytes-coverage",
    ":antibytes-gradle-utils",
    ":antibytes-gradle-test-utils",
    ":antibytes-dependency-bridge",
    ":antibytes-dependency-catalog",
    ":antibytes-dependency-catalog-builder",
    ":antibytes-dependency-helper",
    ":antibytes-dependency-node",
    ":antibytes-publishing",
    ":antibytes-publishing-configuration",
    ":antibytes-versioning",
    ":antibytes-grammar-tools",
    ":antibytes-runtime-configuration",
    ":antibytes-quality",
    ":antibytes-detekt-configuration",
    ":antibytes-custom-component",
    ":antibytes-mkdocs",
    ":antibytes-dependency-settings",
)

buildCache {
    val isGithub = System.getenv("GITHUB")?.let { true } ?: false
    local {
        isEnabled = !isGithub
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 10
    }

    local {
        isEnabled = isGithub
        directory = File(rootDir, ".gradle/build-cache")
        removeUnusedEntriesAfterDays = 10
    }
}

rootProject.name = "gradle-plugins"
