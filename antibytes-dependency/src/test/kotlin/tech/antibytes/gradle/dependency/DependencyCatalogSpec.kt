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

class DependencyCatalogSpec {
    @Test
    fun `It contains PythonDependencies`() {
        // Given
        val module: VersionCatalogBuilder.LibraryAliasBuilder = mockk()
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.library(any(), any()) } just Runs
        every { catalog.library(any(), any(), any()) } returns module
        every { module.version(any<String>()) } just Runs

        // When
        catalog.addDependencies()

        // Then
        verify(exactly = 1) {
            catalog.library("mkDocs-includeMarkdown", "python", "mkdocs-include-markdown-plugin")
        }

        verify(exactly = 1) {
            module.version("mkDocs-includeMarkdown")
        }
    }
}
