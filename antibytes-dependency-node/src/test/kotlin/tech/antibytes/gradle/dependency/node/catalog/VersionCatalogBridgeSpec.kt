/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.node.catalog

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.dependency.node.NodeDependencyTransformerContract.Bridge
import tech.antibytes.gradle.dependency.node.NodeDependencyTransformerContract.NodeDependencies

class VersionCatalogBridgeSpec {
    @Test
    fun `It fulfils Bridge`() {
        val bridge: Any = VersionCatalogBridge

        assertTrue(bridge is Bridge)
    }

    @Test
    fun `Given addNodeDependencies it adds ProductionDependencies`() {
        // Given
        val dependencies = NodeDependencies(
            production = mapOf(
                "eslint" to "^5.9.0",
                "copy-webpack-plugin" to "11.0.0",
                "sql.js" to "1.8.0",
            ),
        )
        val version: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk {
            every { library(any(), any(), any()) } returns version
        }

        // When
        VersionCatalogBridge.addNodeDependencies(
            builder = catalog,
            dependencies = dependencies,
        )

        // Then
        verify(exactly = 1) {
            catalog.library(
                "node-eslint",
                "node-production",
                "eslint",
            )
        }
        verify(exactly = 1) {
            version.version("^5.9.0")
        }

        verify(exactly = 1) {
            catalog.library(
                "node-copyWebpackPlugin",
                "node-production",
                "copy-webpack-plugin",
            )
        }
        verify(exactly = 1) {
            version.version("11.0.0")
        }

        verify(exactly = 1) {
            catalog.library(
                "node-sqlJs",
                "node-production",
                "sql.js",
            )
        }
        verify(exactly = 1) {
            version.version("1.8.0")
        }
    }

    @Test
    fun `Given addNodeDependencies it adds DevelopmentDependencies`() {
        // Given
        val dependencies = NodeDependencies(
            development = mapOf(
                "eslint" to "^5.9.0",
                "copy-webpack-plugin" to "11.0.0",
                "sql.js" to "1.8.0",
            ),
        )
        val version: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk {
            every { library(any(), any(), any()) } returns version
        }

        // When
        VersionCatalogBridge.addNodeDependencies(
            builder = catalog,
            dependencies = dependencies,
        )

        // Then
        verify(exactly = 1) {
            catalog.library(
                "node-eslint",
                "node-development",
                "eslint",
            )
        }
        verify(exactly = 1) {
            version.version("^5.9.0")
        }

        verify(exactly = 1) {
            catalog.library(
                "node-copyWebpackPlugin",
                "node-development",
                "copy-webpack-plugin",
            )
        }
        verify(exactly = 1) {
            version.version("11.0.0")
        }

        verify(exactly = 1) {
            catalog.library(
                "node-sqlJs",
                "node-development",
                "sql.js",
            )
        }
        verify(exactly = 1) {
            version.version("1.8.0")
        }
    }

    @Test
    fun `Given addNodeDependencies it adds PeerDependencies`() {
        // Given
        val dependencies = NodeDependencies(
            peer = mapOf(
                "eslint" to "^5.9.0",
                "copy@webpack@plugin" to "11.0.0",
                "sql.js" to "1.8.0",
            ),
        )
        val version: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk {
            every { library(any(), any(), any()) } returns version
        }

        // When
        VersionCatalogBridge.addNodeDependencies(
            builder = catalog,
            dependencies = dependencies,
        )

        // Then
        verify(exactly = 1) {
            catalog.library(
                "node-eslint",
                "node-peer",
                "eslint",
            )
        }
        verify(exactly = 1) {
            version.version("^5.9.0")
        }

        verify(exactly = 1) {
            catalog.library(
                "node-copyWebpackPlugin",
                "node-peer",
                "copy@webpack@plugin",
            )
        }
        verify(exactly = 1) {
            version.version("11.0.0")
        }

        verify(exactly = 1) {
            catalog.library(
                "node-sqlJs",
                "node-peer",
                "sql.js",
            )
        }
        verify(exactly = 1) {
            version.version("1.8.0")
        }
    }

    @Test
    fun `Given addNodeDependencies it adds OptionalDependencies`() {
        // Given
        val dependencies = NodeDependencies(
            optional = mapOf(
                "eslint" to "^5.9.0",
                "copy/webpack/plugin" to "11.0.0",
                "sql.js" to "1.8.0",
            ),
        )
        val version: VersionCatalogBuilder.LibraryAliasBuilder = mockk(relaxed = true)
        val catalog: VersionCatalogBuilder = mockk {
            every { library(any(), any(), any()) } returns version
        }

        // When
        VersionCatalogBridge.addNodeDependencies(
            builder = catalog,
            dependencies = dependencies,
        )

        // Then
        verify(exactly = 1) {
            catalog.library(
                "node-eslint",
                "node-optional",
                "eslint",
            )
        }
        verify(exactly = 1) {
            version.version("^5.9.0")
        }

        verify(exactly = 1) {
            catalog.library(
                "node-copyWebpackPlugin",
                "node-optional",
                "copy/webpack/plugin",
            )
        }
        verify(exactly = 1) {
            version.version("11.0.0")
        }

        verify(exactly = 1) {
            catalog.library(
                "node-sqlJs",
                "node-optional",
                "sql.js",
            )
        }
        verify(exactly = 1) {
            version.version("1.8.0")
        }
    }
}
