/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */


plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.gson)
    implementation(libs.kotlinPoet)

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.mockk)
    testImplementation(libs.jvmFixture)
    testImplementation(libs.jupiter)
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
    plugins.register("tech.antibytes.gradle.dependency.local") {
        id = "tech.antibytes.gradle.dependency.local"
        implementationClass = "tech.antibytes.gradle.dependency.DependencyPlugin"
    }
}

tasks.test {
    useJUnitPlatform()
}
