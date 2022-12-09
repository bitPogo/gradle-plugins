/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.component

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension

class AntibytesCustomComponentExtensionSpec {
    @Test
    fun `It fulfils Extension`() {
        val extension: Any = createExtension<AntibytesCustomComponentExtension>()

        assertTrue(extension is CustomComponentContract.Extension)
    }

    @Test
    fun `It has a empty Set as default Artifacts`() {
        val extension = createExtension<AntibytesCustomComponentExtension>()

        assertEquals(
            actual = extension.customArtifacts.get(),
            expected = emptySet(),
        )
    }

    @Test
    fun `It has a empty Map as default Attributes`() {
        val extension = createExtension<AntibytesCustomComponentExtension>()

        assertEquals(
            actual = extension.attributes.get(),
            expected = emptyMap(),
        )
    }

    @Test
    fun `Its default Scope is compile`() {
        val extension = createExtension<AntibytesCustomComponentExtension>()

        assertEquals(
            actual = extension.scope.get(),
            expected = "compile",
        )
    }
}
