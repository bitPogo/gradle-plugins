/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.script

plugins {
    id("com.diffplug.spotless")
}

val ktlintVersion = "0.47.1"

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude(
            "buildSrc/build/",
            "**/buildSrc/build/",
            "**/src/test/resources/**/*.kt",
            "**/build/**/*.kt"
        )
        ktlint(ktlintVersion).editorConfigOverride(
            mapOf(
                "ktlint_disabled_rules" to "no-wildcard-imports",
                "ij_kotlin_imports_layout" to "*",
                "ij_kotlin_allow_trailing_comma" to "true",
                "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
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
