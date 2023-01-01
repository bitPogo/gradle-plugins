/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.gradle.api.Project
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
        every { provider.get().module.name } returns fixture()
        every { provider.get().versionConstraint.toStr() } returns fixture()

        // Then
        assertFailsWith<IllegalStateException> {
            // When
            provider.asPythonPackage()
        }
    }

    @Test
    fun `Given asPythonPackage is called it adds the transformed NodeVersion to Production`() {
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
    fun `Given nodeProductionPackage is called it adds the transformed NodeVersion to Production`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-production"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns mockk {
            every { logger } returns mockk()
        }

        applyDependencyContext(kotlinDependencyHandler) {
            every { npm(any(), any<String>()) } returns dependency

            // When
            nodeProductionPackage(provider)

            // Then
            verify(exactly = 1) {
                npm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }

    @Test
    fun `Given nodeProductionPackage is called it logs an error is not a NodeProductionPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val project: Project = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns fixture()
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns project

        applyDependencyContext(kotlinDependencyHandler) {
            every { npm(any(), any<String>()) } returns dependency

            // When
            nodeProductionPackage(provider)

            // Then
            verify(exactly = 1) {
                npm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
            verify(atLeast = 1) {
                project.logger.warn("This is not a production package.")
            }
        }
    }

    @Test
    fun `Given nodeDevelopmentPackage is called it adds the transformed NodeVersion to Development`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-development"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns mockk {
            every { logger } returns mockk()
        }

        applyDependencyContext(kotlinDependencyHandler) {
            every { devNpm(any(), any<String>()) } returns dependency

            // When
            nodeDevelopmentPackage(provider)

            // Then
            verify(exactly = 1) {
                devNpm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }

    @Test
    fun `Given nodeDevelopmentPackage is called it logs an error is not a NodeDevelopmentPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val project: Project = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns fixture()
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns project

        applyDependencyContext(kotlinDependencyHandler) {
            every { devNpm(any(), any<String>()) } returns dependency

            // When
            nodeDevelopmentPackage(provider)

            // Then
            verify(exactly = 1) {
                devNpm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
            verify(atLeast = 1) {
                project.logger.warn("This is not a development package.")
            }
        }
    }

    @Test
    fun `Given nodePeerPackage is called it adds the transformed NodeVersion as PeerPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-peer"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns mockk {
            every { logger } returns mockk()
        }

        applyDependencyContext(kotlinDependencyHandler) {
            every { peerNpm(any(), any()) } returns dependency

            // When
            nodePeerPackage(provider)

            // Then
            verify(exactly = 1) {
                peerNpm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }

    @Test
    fun `Given nodePeerPackage is called it logs an error is not a NodePeerPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val project: Project = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns fixture()
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns project

        applyDependencyContext(kotlinDependencyHandler) {
            every { peerNpm(any(), any()) } returns dependency

            // When
            nodePeerPackage(provider)

            // Then
            verify(exactly = 1) {
                peerNpm(artifactId, version)
            }
            verify(atLeast = 1) {
                project.logger.warn("This is not a peer package.")
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }

    @Test
    fun `Given nodeOptionalPackage is called it adds the transformed NodeVersion as OptionalPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-optional"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns mockk {
            every { logger } returns mockk()
        }

        applyDependencyContext(kotlinDependencyHandler) {
            every { optionalNpm(any(), any<String>()) } returns dependency

            // When
            nodeOptionalPackage(provider)

            // Then
            verify(exactly = 1) {
                optionalNpm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }

    @Test
    fun `Given nodeOptionalPackage is called it logs an error is not a NodeOptionalPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val project: Project = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns fixture()
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns project

        applyDependencyContext(kotlinDependencyHandler) {
            every { optionalNpm(any(), any<String>()) } returns dependency

            // When
            nodeOptionalPackage(provider)

            // Then
            verify(exactly = 1) {
                optionalNpm(artifactId, version)
            }
            verify(atLeast = 1) {
                project.logger.warn("This is not a optional package.")
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }

    @Test
    fun `Given nodePackage is called it fails due to a unknown package`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val project: Project = mockk(relaxed = true)
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns fixture()
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns project

        applyDependencyContext(kotlinDependencyHandler) {
            assertFailsWith<IllegalArgumentException> {
                // When
                nodePackage(provider)
            }
        }
    }

    @Test
    fun `Given nodePackage is called it adds the transformed NodeVersion to Production as NodeProductionPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-production"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns mockk {
            every { logger } returns mockk()
        }

        applyDependencyContext(kotlinDependencyHandler) {
            every { npm(any(), any<String>()) } returns dependency

            // When
            nodePackage(provider)

            // Then
            verify(exactly = 1) {
                npm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }

    @Test
    fun `Given nodePackage is called it adds the transformed NodeVersion to Production as NodeDevelopmentPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-development"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns mockk {
            every { logger } returns mockk()
        }

        applyDependencyContext(kotlinDependencyHandler) {
            every { devNpm(any(), any<String>()) } returns dependency

            // When
            nodePackage(provider)

            // Then
            verify(exactly = 1) {
                devNpm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }

    @Test
    fun `Given nodePeerPackage is called it adds the transformed NodeVersion to Production as NodePeerPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-peer"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns mockk {
            every { logger } returns mockk()
        }

        applyDependencyContext(kotlinDependencyHandler) {
            every { peerNpm(any(), any()) } returns dependency

            // When
            nodePackage(provider)

            // Then
            verify(exactly = 1) {
                peerNpm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }

    @Test
    fun `Given nodeOptionalPackage is called it adds the transformed NodeVersion to Production as NodeOptionalPackage`() {
        // Given
        val provider: Provider<MinimalExternalModuleDependency> = mockk()
        val kotlinDependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val dependency: Dependency = mockk()
        val artifactId: String = fixture()
        val version: String = fixture()

        every { provider.get().module.group } returns "node-optional"
        every { provider.get().module.name } returns artifactId
        every { provider.get().versionConstraint.toStr() } returns version
        every { kotlinDependencyHandler.project } returns mockk {
            every { logger } returns mockk()
        }

        applyDependencyContext(kotlinDependencyHandler) {
            every { optionalNpm(any(), any<String>()) } returns dependency

            // When
            nodePackage(provider)

            // Then
            verify(exactly = 1) {
                optionalNpm(artifactId, version)
            }
            verify(exactly = 1) {
                kotlinDependencyHandler.implementation(dependency)
            }
        }
    }
}
