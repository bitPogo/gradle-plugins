/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning

import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension

class AntiBytesVersioningPluginExtensionSpec {
    @Test
    fun `It fulfils Extension`() {
        val extension: Any = createExtension<AntiBytesVersioningPluginExtension>()

        assertTrue(extension is VersioningInternalContract.Extension)
    }

    @Test
    fun `It has no default Configuration`() {
        assertNull(createExtension<AntiBytesVersioningPluginExtension>().configuration)
    }
}
