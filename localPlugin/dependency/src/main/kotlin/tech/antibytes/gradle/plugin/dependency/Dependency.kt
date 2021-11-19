/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.dependency

object Dependency {
    val gradle = GradlePlugin

    object GradlePlugin {
        val android = "com.android.tools.build:gradle:${Version.android}"
        val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
        val dependencyUpdate = "com.github.ben-manes:gradle-versions-plugin:${Version.gradle.dependencyUpdate}"
        val jacoco = "org.jacoco:org.jacoco.core:${Version.gradle.jacoco}"
        val publishing = "org.eclipse.jgit:org.eclipse.jgit:${Version.gradle.publishing}"
        val versioning = "com.palantir.gradle.gitversion:gradle-git-version:${Version.gradle.versioning}"
    }

    val test = Test

    object Test {
        val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}"
        val junit = "org.junit:junit-bom:${Version.test.junit}"
        val jupiter = "org.junit.jupiter:junit-jupiter"
        val mockk = "io.mockk:mockk:${Version.test.mockk}"
        val fixture = "com.appmattus.fixture:fixture:${Version.test.fixture}"
    }
}
