/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.script

/**
 * Quality check to keep the code spotless using [Spotless](https://github.com/diffplug/spotless)
 *
 * It uses Ktlint to format and validate Kotlin code style.
 *
 * Install:
 *
 * You need to add following dependencies to the buildSrc/build.gradle.kts
 *
 * dependencies {
 *     implementation("com.diffplug.spotless:spotless-plugin-gradle:5.10.2")
 *     implementation("com.pinterest:ktlint:0.42.1") or higher
 * }
 *
 * and ensure that the gradlePluginPortal is available
 *
 * repositories {
 *     gradlePluginPortal()
 * }
 *
 * Now just add id("tech.antibytes.gradle.plugin.script.quality-spotless") to your rootProject build.gradle.kts plugins
 *
 * plugins {
 *     id("tech.antibytes.gradle.plugin.script.quality-spotless")
 * }
 */
plugins {
    id("com.diffplug.spotless")
}

val ktlintVersion = "0.43.2"

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("buildSrc/build/", "**/buildSrc/build/")
        ktlint(ktlintVersion).userData(
            mapOf(
                "disabled_rules" to "no-wildcard-imports",
                "ij_kotlin_imports_layout" to "*"
            )
        )
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(ktlintVersion)
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    format("misc") {
        target("**/*.adoc", "**/*.md", "**/.gitignore", ".java-version")

        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}
