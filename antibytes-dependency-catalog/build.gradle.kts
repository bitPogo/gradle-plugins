/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
import tech.antibytes.gradle.plugin.dependency.Version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco

    id("tech.antibytes.gradle.plugin.script.maven-package")
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(tech.antibytes.gradle.plugin.dependency.Dependency.gradle.atomicFu) {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    implementation(tech.antibytes.gradle.plugin.dependency.Dependency.gradle.android)

    testImplementation(tech.antibytes.gradle.plugin.dependency.Dependency.test.kotlinTest)
    testImplementation(platform(tech.antibytes.gradle.plugin.dependency.Dependency.test.junit))
    testImplementation(tech.antibytes.gradle.plugin.dependency.Dependency.test.mockk)
    testImplementation(tech.antibytes.gradle.plugin.dependency.Dependency.test.jupiter)
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
                    "ANDROID" to Version.gradle.android,
                    "KOTLIN" to Version.gradle.kotlin,
                    "OWASP" to Version.gradle.owasp,
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

tasks.test {
    useJUnitPlatform()
}
