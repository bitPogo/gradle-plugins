/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.junit.Test
import kotlin.test.assertTrue

class VersioningSpec {
    @Test
    fun `It fulfils Versioning`() {
        val versioning: Any = Versioning()

        assertTrue(versioning is PublishingContract.Versioning)
    }
}
