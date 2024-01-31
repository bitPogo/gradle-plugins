/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.node

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import java.io.File
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.dependency.node.catalog.VersionCatalogBridge
import tech.antibytes.gradle.dependency.node.reader.NodeReader

class NodeToDependencyCatalogSpec {
    private val project = ProjectBuilder.builder().build()

    @BeforeEach
    fun setup() {
        mockkObject(NodeReader, VersionCatalogBridge)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(NodeReader, VersionCatalogBridge)
    }

    @Test
    fun `Given nodeToDependencyCatalog it adds dependencies to a given VersionCatalog`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        val dependencies: ConfigurableFileCollection = project.files(File("."), File(".."))
        val extractedDependencies: NodeDependencyTransformerContract.NodeDependencies = mockk()

        every { NodeReader.extractPackages(any()) } returns extractedDependencies
        every { VersionCatalogBridge.addNodeDependencies(any(), any()) } just Runs

        // When
        catalog.nodeToDependencyCatalog(dependencies)

        // Then
        verify(atLeast = 1) { NodeReader.extractPackages(dependencies.files.toList()[0]) }
        verify(atLeast = 1) { NodeReader.extractPackages(dependencies.files.toList()[1]) }
        verify(exactly = 2) { VersionCatalogBridge.addNodeDependencies(catalog, extractedDependencies) }
    }
}
