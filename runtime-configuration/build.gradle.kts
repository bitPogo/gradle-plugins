/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

plugins {
    `kotlin-dsl`
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencies {
    implementation(libs.kotlin)
    implementation(project(":utils"))
    implementation(libs.kotlinPoet)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "src-plugin/main/kotlin",
        )
    }
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.runtime.local") {
        id = "tech.antibytes.gradle.runtime.local"
        implementationClass = "tech.antibytes.gradle.runtime.local.DependencyPlugin"
    }
}
