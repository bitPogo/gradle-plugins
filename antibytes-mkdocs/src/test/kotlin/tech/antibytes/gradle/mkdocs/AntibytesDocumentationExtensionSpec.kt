/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.mkdocs

import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension

class AntibytesDocumentationExtensionSpec {
    @Test
    fun `It fulfils Extension`() {
        val extension: Any = createExtension<AntibytesDocumentationExtension>()

        assertTrue(extension is MkdocsContract.Extension)
    }
}
