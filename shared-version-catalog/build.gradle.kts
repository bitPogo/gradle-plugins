/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.local.AntibytesDependencyVersionTask
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.api.VersioningConfiguration


plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco

    id("tech.antibytes.gradle.dependency.local")
    id("tech.antibytes.gradle.versioning.local")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
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
        java.srcDirs(
            "src/main/kotlin",
            "build/generated/antibytes/main/kotlin",
            "src-plugin/main/kotlin",
        )
    }
}

val templatesPath = "${projectDir}/src/templates"
val configPath = "${projectDir}/build/generated/antibytes/main/kotlin/tech/antibytes/gradle/dependency/config"

fun String.replaceContent(replacements: Map<String, String>): String {
    var text = this

    replacements.forEach { (pattern, replacement) ->
        text = text.replace(pattern, replacement)
    }

    return text
}

val provideVersions: AntibytesDependencyVersionTask by tasks.creating(AntibytesDependencyVersionTask::class.java) {
    packageName.set("tech.antibytes.gradle.dependency.config")
    val externalDependencies = listOf(File("${projectDir.absolutePath.trimEnd('/')}/../shared-dependencies"))
    val internalDependencies = listOf(File("${projectDir.absolutePath.trimEnd('/')}/../gradle"))

    pythonDirectory.set(externalDependencies)
    nodeDirectory.set(externalDependencies)
    gradleDirectory.set(
        listOf(
            externalDependencies,
            internalDependencies,
        ).flatten()
    )
}

val provideConfig: Task by tasks.creating {
    mustRunAfter(provideVersions)
    doLast {
        val templates = File(templatesPath)
        val configDir = File(configPath)

        val config = File(templates, "DependencyConfig.tmpl")
            .readText()
            .replaceContent(
                mapOf(
                    "ANTIBYTES" to Versioning.getInstance(
                        project = project,
                        configuration = VersioningConfiguration(
                            featurePrefixes = listOf("feature"),
                        )
                    ).versionName(),
                )
            )

        if (!configDir.exists()) {
            if (!configDir.mkdirs()) {
                throw StopExecutionException("The script not able to create the config directory")
            }
        }
        val configFile = File(configDir, "DependencyConfig.kt")
        if (!configFile.exists()) {
            configFile.createNewFile()
        }
        configFile.writeText(config)
    }
}

tasks.withType<KotlinCompile> {
    dependsOn(
        provideVersions,
        provideConfig,
    )
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.dependency.catalog") {
        id = "tech.antibytes.gradle.dependency.catalog"
        implementationClass = "tech.antibytes.gradle.dependency.catalog.DependencyPlugin"
    }
}

tasks.test {
    useJUnitPlatform()
}
