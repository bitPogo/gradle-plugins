/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

import tech.antibytes.gradle.dependency.GradleBundleVersion

internal object Vendor {
    /**
     * [NodeJs](https://nodejs.org/en/)
     */
    const val node = "18.12.0"

    val slf4j = SLF4J
    /**
     * [SLF4J](http://www.slf4j.org/)
     */
    internal object SLF4J {
        private const val version = "1.7.36"

        const val noop = version
        const val api = version
    }

    /**
     * [UUID](https://github.com/benasher44/uuid)
     */
    const val uuid = "0.5.0"

    val test = Test

    internal object Test {
        /**
         * [Paparazzi](https://github.com/cashapp/paparazzi)
         */
        val paparazzi = GradleBundleVersion("1.0.0")

        /**
         * [mockk](http://mockk.io)
         */
        const val mockk = "1.12.8"

        val junit = JUnit

        internal object JUnit {
            /**
             * [JUnit5](https://github.com/junit-team/junit5/)
             */
            private const val version = "5.9.0"
            const val bom = version
            const val core = version
            const val parameterized = version
            const val legacyEngineJunit4 = version

            /**
             * [JUnit4](https://github.com/junit-team/junit4/)
             */
            const val junit4 = "4.13.2"
        }
    }
}
