/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
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
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("antibytes") {
            from(files("../gradle/antibytes.catalog.toml"))
        }
    }
}

rootProject.name = "quality-local"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

include("utils-quality")
include("helper-quality")
