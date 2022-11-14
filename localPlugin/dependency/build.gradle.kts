/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

plugins {
    `kotlin-dsl`
    java
    //`java-gradle-plugin`
}

group = "tech.antibytes.gradle.plugin.dependency"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.plugin.dependency") {
        id = "tech.antibytes.gradle.plugin.dependency"
        implementationClass = "tech.antibytes.gradle.plugin.dependency.DependencyPlugin"
    }
}

with(extensions.getByType<JavaPluginExtension>()) {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
