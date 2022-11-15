/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco
}

// To make it available as direct dependency
group = "tech.antibytes.gradle.dependency"

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.atomicFu) {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    implementation(libs.agp)

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

configure<SourceSetContainer> {
    main {
        java.srcDirs("src/main/kotlin", "src-gen/main/kotlin")
    }
}

val templatesPath = "${projectDir}/src/templates"
val configPath = "${projectDir}/src-gen/main/kotlin/tech/antibytes/gradle/dependency/config"

fun String.replaceContent(replacements: Map<String, String>): String {
    var text = this

    replacements.forEach { (pattern, replacement) ->
        text = text.replace(pattern, replacement)
    }

    return text
}

val provideConfig: Task by tasks.creating {
    doFirst {
        val templates = File(templatesPath)
        val configs = File(configPath)

        val config = File(templates, "DependencyConfig.tmpl")
            .readText()
            .replaceContent(
                mapOf(
                    "ANDROID" to libs.versions.agp.get(),
                    "KOTLIN" to libs.versions.kotlin.get(),
                    "OWASP" to libs.versions.owasp.get(),
                )
            )

        if (!configs.exists()) {
            if (!configs.mkdir()) {
                System.err.println("The script not able to create the config directory")
            }
        }
        File(configPath, "DependencyConfig.kt").writeText(config)
    }
}

tasks.withType<KotlinCompile> {
    dependsOn(provideConfig)
}


gradlePlugin {
    plugins.register("tech.antibytes.gradle.dependency.catalog") {
        id = "tech.antibytes.gradle.dependency.catalog"
        implementationClass = "tech.antibytes.gradle.dependency.DependencyPlugin"
    }
}

tasks.test {
    useJUnitPlatform()
}
