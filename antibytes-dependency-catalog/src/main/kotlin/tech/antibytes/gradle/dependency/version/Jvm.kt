/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

internal object Jvm {
    /**
     * [Log4J](http://www.slf4j.org/)
     */
    const val slf4j = "1.7.36"

    val test = Test

    object Test {
        /**
         * [JUnit](https://github.com/junit-team/junit5/)
         */
        const val junit = "5.9.0"

        /**
         * [JUnit](https://github.com/junit-team/junit4/)
         */
        const val junit4 = "4.13.2"
    }
}
