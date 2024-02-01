/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.toString()))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlin)
    implementation(projects.utils)
    implementation(libs.kotlinPoet)
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
