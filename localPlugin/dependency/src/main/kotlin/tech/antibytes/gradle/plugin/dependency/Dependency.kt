/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.dependency

object Dependency {
    val gradle = GradlePlugin

    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:${Version.gradle.android}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.gradle.kotlin}"
        const val kotlinReflection = "org.jetbrains.kotlin:kotlin-reflect:${Version.gradle.kotlin}"
        const val owasp = "org.owasp:dependency-check-gradle:${Version.gradle.owasp}"
        const val dependencyUpdate = "com.github.ben-manes:gradle-versions-plugin:${Version.gradle.dependencyUpdate}"
        const val jacoco = "org.jacoco:org.jacoco.core:${Version.gradle.jacoco}"
        const val publishing = "org.eclipse.jgit:org.eclipse.jgit:${Version.gradle.publishing}"
        const val versioning = "com.palantir.gradle.gitversion:gradle-git-version:${Version.gradle.versioning}"
        const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:${Version.gradle.spotless}"
        const val ktlint = "com.pinterest:ktlint:${Version.gradle.ktlint}"
    }

    val library = Library

    object Library {
        const val jflex = "de.jflex:jflex:${Version.library.jflex}"
        const val turtle = "com.lordcodes.turtle:turtle:${Version.library.turtle}"
    }

    val square = Square

    object Square {
        val kotlinPoet = KotlinPoet

        object KotlinPoet {
            val core = "com.squareup:kotlinpoet:${Version.square.kotlinPoet}"
        }
    }

    val test = Test

    object Test {
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}"
        const val junit = "org.junit:junit-bom:${Version.test.junit}"
        const val jupiter = "org.junit.jupiter:junit-jupiter"
        const val mockk = "io.mockk:mockk:${Version.test.mockk}"
        const val fixture = "com.appmattus.fixture:fixture:${Version.test.fixture}"
    }
}
