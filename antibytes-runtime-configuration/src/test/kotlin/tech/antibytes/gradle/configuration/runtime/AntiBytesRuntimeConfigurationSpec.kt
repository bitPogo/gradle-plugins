/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.runtime

import org.gradle.api.Plugin
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class AntiBytesRuntimeConfigurationSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesRuntimeConfiguration()

        assertTrue(plugin is Plugin<*>)
    }
}
