/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.plugin.dependency

object Version {
    const val kotlin = "1.6.10"

    val gradle = GradlePlugin
    val library = Library
    val square = Square
    val test = Test

    object GradlePlugin {
        /**
         * [Gradle Versions](https://github.com/ben-manes/gradle-versions-plugin)
         */
        const val dependencyUpdate = "0.42.0"

        /**
         * [OWASP](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)
         */
        const val owasp = "6.5.3"

        /**
         * [Jacoco](https://github.com/jacoco/jacoco/releases)
         */
        const val jacoco = "0.8.7"

        /**
         * [JGit Core](https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit)
         */
        const val publishing = "6.0.0.202111291000-r"

        /**
         * [gradle-git-version](https://github.com/palantir/gradle-git-version/releases)
         */
        const val versioning = "0.12.2" // see: https://github.com/palantir/gradle-git-version/issues/353

        /**
         * [Kotlin](https://kotlinlang.org/docs/releases.html)
         */
        const val kotlin = "1.6.10"

        /**
         * [AtomicFu](https://kotlinlang.org/docs/releases.html)
         */
        const val atomicFu = "0.17.1"

        /**
         * [AGP](https://developer.android.com/studio/releases/gradle-plugin)
         */
        const val android = "7.1.1"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "6.3.0"

        /**
         * [KTLint](https://github.com/pinterest/ktlint)
         */
        const val ktlint = "0.44.0"
    }

    object Library {
        /**
         * [JFlex](https://jflex.de/)
         */
        const val jflex = "1.8.2"

        /**
         * [Turtle](https://github.com/lordcodes/turtle)
         */
        const val turtle = "0.6.0"
    }

    object Square {
        /**
         * [Kotlin Poet](https://square.github.io/kotlinpoet/)
         */
        val kotlinPoet = "1.10.2"
    }

    object Test {
        /**
         * [JUnit](https://github.com/junit-team/junit5/)
         */
        const val junit = "5.8.2"

        /**
         * [mockk](http://mockk.io)
         */
        const val mockk = "1.12.2"

        /**
         * [kotlinFixture](https://github.com/appmattus/kotlinfixture)
         */
        const val fixture = "1.2.0"
    }
}
