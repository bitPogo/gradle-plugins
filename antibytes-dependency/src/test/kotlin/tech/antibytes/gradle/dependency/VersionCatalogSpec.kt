/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.junit.jupiter.api.Test

class VersionCatalogSpec {
    @Test
    fun `It contains versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } answers {
            println(args)
            "any"
        }

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("google-hilt-core", "2.43.2")
        }
    }
}
