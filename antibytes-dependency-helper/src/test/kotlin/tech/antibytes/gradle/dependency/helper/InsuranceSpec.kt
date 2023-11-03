/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.DependencyResolveDetails
import org.gradle.api.artifacts.ResolutionStrategy
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty
import tech.antibytes.gradle.test.invokeGradleAction

class InsuranceSpec {
    @Test
    fun `Given a Project is called with ensureKotlinVersion it ignores non jetbrains dependencies`() {
        // Given
        val project: Project = mockk()
        val configurations: ConfigurationContainer = mockk()
        val configuration: Configuration = mockk()
        val resolutionStrategy: ResolutionStrategy = mockk()
        val selector: DependencyResolveDetails = mockk(relaxed = true)

        every { project.configurations } returns configurations
        every { configuration.resolutionStrategy } returns resolutionStrategy

        invokeGradleAction(configuration) { probe ->
            configurations.all(probe)
        }

        invokeGradleAction(
            selector,
            resolutionStrategy,
        ) { probe ->
            resolutionStrategy.eachDependency(probe)
        }

        // When
        project.ensureKotlinVersion()

        // Then
        verify(exactly = 0) { selector.useVersion(any()) }
    }

    @Test
    fun `Given a Project is called with ensureKotlinVersion it ignores jetbrains dependencies, which are not a specific module`() {
        // Given
        val project: Project = mockk()
        val configurations: ConfigurationContainer = mockk()
        val configuration: Configuration = mockk()
        val resolutionStrategy: ResolutionStrategy = mockk()
        val selector: DependencyResolveDetails = mockk(relaxed = true)

        every { project.configurations } returns configurations
        every { configuration.resolutionStrategy } returns resolutionStrategy
        every { selector.requested.group } returns "org.jetbrains.kotlin"
        every { selector.target.name } returns "any"

        invokeGradleAction(configuration) { probe ->
            configurations.all(probe)
        }

        invokeGradleAction(
            selector,
            resolutionStrategy,
        ) { probe ->
            resolutionStrategy.eachDependency(probe)
        }

        // When
        project.ensureKotlinVersion()

        // Then
        verify(exactly = 0) { selector.useVersion(any()) }
    }

    @Test
    fun `Given a Project is called with ensureKotlinVersion it enforces a Resolution Strategy if the dependency is jetbrains and a specific module`() {
        // Given
        val project: Project = mockk()
        val configurations: ConfigurationContainer = mockk()
        val configuration: Configuration = mockk()
        val resolutionStrategy: ResolutionStrategy = mockk()
        val selector: DependencyResolveDetails = mockk(relaxed = true)
        val toEnsure = listOf(
            "kotlin-stdlib-jdk7",
            "kotlin-stdlib-jdk8",
            "kotlin-stdlib",
            "kotlin-stdlib-common",
            "kotlin-stdlib-js",
            "kotlin-stdlib-jvm",
            "kotlin-stdlib-wasm",
            "kotlin-reflect",
        )

        every { project.configurations } returns configurations
        every { configuration.resolutionStrategy } returns resolutionStrategy
        every { selector.requested.group } returns "org.jetbrains.kotlin"
        every { selector.requested.name } returnsMany toEnsure
        every { selector.target.name } returns "any"

        invokeGradleAction(configuration) { probe ->
            configurations.all(probe)
        }

        invokeGradleAction(
            selector,
            resolutionStrategy,
        ) { probe ->
            resolutionStrategy.eachDependency(probe)
        }

        // When
        repeat(toEnsure.size) {
            project.ensureKotlinVersion()
        }
        // Then
        verify(exactly = toEnsure.size) { selector.useVersion("1.9.10") }
        verify(exactly = toEnsure.size) { selector.because("Avoid resolution conflicts") }
    }

    @Test
    fun `Given a Project is called with ensureKotlinVersion, which gets a custom version it enforces a Resolution Strategy if the dependency is jetbrains and a specific module`() {
        // Given
        val project: Project = mockk()
        val configurations: ConfigurationContainer = mockk()
        val configuration: Configuration = mockk()
        val resolutionStrategy: ResolutionStrategy = mockk()
        val selector: DependencyResolveDetails = mockk(relaxed = true)
        val version = "abc"
        val toEnsure = listOf(
            "kotlin-stdlib-jdk7",
            "kotlin-stdlib-jdk8",
            "kotlin-stdlib",
            "kotlin-stdlib-common",
            "kotlin-stdlib-js",
            "kotlin-stdlib-jvm",
            "kotlin-stdlib-wasm",
            "kotlin-reflect",
        )

        every { project.configurations } returns configurations
        every { configuration.resolutionStrategy } returns resolutionStrategy
        every { selector.requested.group } returns "org.jetbrains.kotlin"
        every { selector.requested.name } returnsMany toEnsure
        every { selector.target.name } returns "any"

        invokeGradleAction(configuration) { probe ->
            configurations.all(probe)
        }

        invokeGradleAction(
            selector,
            resolutionStrategy,
        ) { probe ->
            resolutionStrategy.eachDependency(probe)
        }

        // When
        repeat(toEnsure.size) {
            project.ensureKotlinVersion(version)
        }
        // Then
        verify(exactly = toEnsure.size) { selector.useVersion(version) }
        verify(exactly = toEnsure.size) { selector.because("Avoid resolution conflicts") }
    }

    @Test
    fun `Given a Project is called with ensureKotlinVersion, it ignored targets which had been excluded`() {
        // Given
        val project: Project = mockk()
        val configurations: ConfigurationContainer = mockk()
        val configuration: Configuration = mockk()
        val resolutionStrategy: ResolutionStrategy = mockk()
        val selector: DependencyResolveDetails = mockk(relaxed = true)
        val excludes = listOf("some")
        val toEnsure = listOf(
            "kotlin-stdlib-jdk7",
            "kotlin-stdlib-jdk8",
            "kotlin-stdlib",
            "kotlin-stdlib-common",
            "kotlin-stdlib-js",
            "kotlin-stdlib-jvm",
            "kotlin-stdlib-wasm",
            "kotlin-reflect",
        )

        every { project.configurations } returns configurations
        every { configuration.resolutionStrategy } returns resolutionStrategy
        every { selector.requested.group } returns "org.jetbrains.kotlin"
        every { selector.requested.name } returnsMany toEnsure
        every { selector.target.name } returns "someThing"

        invokeGradleAction(configuration) { probe ->
            configurations.all(probe)
        }

        invokeGradleAction(
            selector,
            resolutionStrategy,
        ) { probe ->
            resolutionStrategy.eachDependency(probe)
        }

        // When
        repeat(toEnsure.size) {
            project.ensureKotlinVersion(excludes = excludes)
        }
        // Then
        verify(exactly = 0) { selector.useVersion(any()) }
        verify(exactly = 0) { selector.because("Avoid resolution conflicts") }
    }

    @Test
    fun `Given a Project is called with ensureKotlinVersion, which gets a custom version provider it enforces a Resolution Strategy if the dependency is jetbrains and a specific module`() {
        // Given
        val project: Project = mockk()
        val configurations: ConfigurationContainer = mockk()
        val configuration: Configuration = mockk()
        val resolutionStrategy: ResolutionStrategy = mockk()
        val selector: DependencyResolveDetails = mockk(relaxed = true)
        val version = "abc"
        val provider = makeProperty(String::class.java, version)
        val toEnsure = listOf(
            "kotlin-stdlib-jdk7",
            "kotlin-stdlib-jdk8",
            "kotlin-stdlib",
            "kotlin-stdlib-common",
            "kotlin-stdlib-js",
            "kotlin-stdlib-jvm",
            "kotlin-stdlib-wasm",
            "kotlin-reflect",
        )

        every { project.configurations } returns configurations
        every { configuration.resolutionStrategy } returns resolutionStrategy
        every { selector.requested.group } returns "org.jetbrains.kotlin"
        every { selector.requested.name } returnsMany toEnsure
        every { selector.target.name } returns "any"

        invokeGradleAction(configuration) { probe ->
            configurations.all(probe)
        }

        invokeGradleAction(
            selector,
            resolutionStrategy,
        ) { probe ->
            resolutionStrategy.eachDependency(probe)
        }

        // When
        repeat(toEnsure.size) {
            project.ensureKotlinVersion(provider)
        }
        // Then
        verify(exactly = toEnsure.size) { selector.useVersion(version) }
        verify(exactly = toEnsure.size) { selector.because("Avoid resolution conflicts") }
    }

    @Test
    fun `Given a Project is called with ensureKotlinVersion which gets a custom version provider, it ignored targets which had been excluded`() {
        // Given
        val project: Project = mockk()
        val configurations: ConfigurationContainer = mockk()
        val configuration: Configuration = mockk()
        val resolutionStrategy: ResolutionStrategy = mockk()
        val selector: DependencyResolveDetails = mockk(relaxed = true)
        val version = "abc"
        val provider = makeProperty(String::class.java, version)
        val excludes = listOf("some")
        val toEnsure = listOf(
            "kotlin-stdlib-jdk7",
            "kotlin-stdlib-jdk8",
            "kotlin-stdlib",
            "kotlin-stdlib-common",
            "kotlin-stdlib-js",
            "kotlin-stdlib-jvm",
            "kotlin-stdlib-wasm",
            "kotlin-reflect",
        )

        every { project.configurations } returns configurations
        every { configuration.resolutionStrategy } returns resolutionStrategy
        every { selector.requested.group } returns "org.jetbrains.kotlin"
        every { selector.requested.name } returnsMany toEnsure
        every { selector.target.name } returns "someThing"

        invokeGradleAction(configuration) { probe ->
            configurations.all(probe)
        }

        invokeGradleAction(
            selector,
            resolutionStrategy,
        ) { probe ->
            resolutionStrategy.eachDependency(probe)
        }

        // When
        repeat(toEnsure.size) {
            project.ensureKotlinVersion(provider, excludes)
        }
        // Then
        verify(exactly = 0) { selector.useVersion(any()) }
        verify(exactly = 0) { selector.because("Avoid resolution conflicts") }
    }
}
