/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

import tech.antibytes.gradle.dependency.GradleBundleVersion

internal object Vendor {
    /**
     * [mockk](http://mockk.io)
     */
    const val mockk = "1.12.8"

    /**
     * [KotlinNodeJS](https://github.com/Kotlin/kotlinx-nodejs)
     */
    const val nodeJs = "0.0.7"

    /**
     * [NodeJs](https://nodejs.org/en/)
     */
    const val node = "18.12.0"

    val slf4j = SLF4J

    /**
     * [UUID](https://github.com/benasher44/uuid)
     */
    const val uuid = "0.5.0"

    /**
     * [Paparazzi](https://github.com/cashapp/paparazzi)
     */
    val paparazzi = GradleBundleVersion("1.0.0")
}
