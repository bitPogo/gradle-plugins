/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.junit.jupiter.api.Test

class VersionCatalogTransformerSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given asPythonPackage is called it fails if the groupId is not python`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()

        every { provider.get().module.group } returns fixture()

        // Then
        assertFailsWith<IllegalArgumentException> {
            // When
            provider.asPythonPackage()
        }
    }

    @Test
    fun `Given asPythonPackage is called it returns a string composed of the id and version`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "python"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version

        // When
        val actual = provider.asPythonPackage()

        // Then
        assertEquals(
            actual = actual,
            expected = "$artifactId:$version",
        )
    }

    private fun applyDependencyContext(
        dependencyHandler: KotlinDependencyHandler,
        action: KotlinDependencyHandler.() -> Unit,
    ) {
        action(dependencyHandler)
    }

    @Test
    fun `Given asNodeProdPackage is fails if it is not a NodeProductionPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk()

        every { provider.get().module.group } returns fixture()

        applyDependencyContext(kotlinDependencyHandler) {
            // Then
            assertFailsWith<IllegalArgumentException> {
                // When
                asNodeProdPackage(provider)
            }
        }
    }

    @Test
    fun `Given asNodeProdPackage is called it returns a string composed of the id and version`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk()
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-production"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version

        applyDependencyContext(kotlinDependencyHandler) {
            every { npm(any(), any<String>()) } returns dependency

            // When
            val actual = asNodeProdPackage(provider)

            // Then
            assertSame(
                actual = actual,
                expected = dependency,
            )

            verify(exactly = 1) {
                npm(artifactId, version)
            }
        }
    }

    @Test
    fun `Given asNodeDevPackage is fails if it is not a NodeDevelopmentPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk()

        every { provider.get().module.group } returns fixture()

        applyDependencyContext(kotlinDependencyHandler) {
            // Then
            assertFailsWith<IllegalArgumentException> {
                // When
                asNodeDevPackage(provider)
            }
        }
    }

    @Test
    fun `Given asNodeDevPackage is called it returns a string composed of the id and version`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk()
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-development"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version

        applyDependencyContext(kotlinDependencyHandler) {
            every { devNpm(any(), any<String>()) } returns dependency

            // When
            val actual = asNodeDevPackage(provider)

            // Then
            assertSame(
                actual = actual,
                expected = dependency,
            )

            verify(exactly = 1) {
                devNpm(artifactId, version)
            }
        }
    }

    @Test
    fun `Given asNodePeerPackage is fails if it is not a NodePeerPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk()

        every { provider.get().module.group } returns fixture()

        applyDependencyContext(kotlinDependencyHandler) {
            // Then
            assertFailsWith<IllegalArgumentException> {
                // When
                asNodePeerPackage(provider)
            }
        }
    }

    @Test
    fun `Given asNodePeerPackage is called it returns a string composed of the id and version`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk()
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-peer"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version

        applyDependencyContext(kotlinDependencyHandler) {
            every { peerNpm(any(), any()) } returns dependency

            // When
            val actual = asNodePeerPackage(provider)

            // Then
            assertSame(
                actual = actual,
                expected = dependency,
            )

            verify(exactly = 1) {
                peerNpm(artifactId, version)
            }
        }
    }

    @Test
    fun `Given asNodeOptionalPackage is fails if it is not a NodeOptionalPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk()

        every { provider.get().module.group } returns fixture()

        applyDependencyContext(kotlinDependencyHandler) {
            // Then
            assertFailsWith<IllegalArgumentException> {
                // When
                asNodeOptionalPackage(provider)
            }
        }
    }

    @Test
    fun `Given asNodeOptionalPackage is called it returns a string composed of the id and version`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk()
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-optional"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version

        applyDependencyContext(kotlinDependencyHandler) {
            every { optionalNpm(any(), any<String>()) } returns dependency

            // When
            val actual = asNodeOptionalPackage(provider)

            // Then
            assertSame(
                actual = actual,
                expected = dependency,
            )

            verify(exactly = 1) {
                optionalNpm(artifactId, version)
            }
        }
    }
}
