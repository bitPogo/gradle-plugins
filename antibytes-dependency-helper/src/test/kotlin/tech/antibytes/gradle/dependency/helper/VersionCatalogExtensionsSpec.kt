/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.PluginDependenciesSpecScope
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty

class VersionCatalogExtensionsSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given id is called in a PluginDependenciesSpecScope with a VersionCatalogProvider it delegates the call to the scope`() {
        // Given
        val id: String = fixture()
        val spec: PluginDependenciesSpecScope = mockk(relaxed = true)
        val pluginValue: PluginDependency = mockk {
            every { pluginId } returns id
        }
        val provider: Provider<PluginDependency> = makeProperty(PluginDependency::class.java, pluginValue)

        fun plugins(scope: PluginDependenciesSpecScope.() -> Unit) = scope.invoke(spec)

        // When
        plugins {
            id(provider)
        }

        // Then
        verify(exactly = 1) { spec.id(id) }
    }

    @Test
    fun `Given implementation is called in a KotlinDependencyHandler with a VersionCatalogProvider it delegates the call to the scope`() {
        // Given
        val module: String = fixture()
        val handler: KotlinDependencyHandler = mockk(relaxed = true)
        val implementationValue: MinimalExternalModuleDependency = mockk {
            every { this@mockk.toString() } returns module
        }
        val provider: Provider<MinimalExternalModuleDependency> = makeProperty(
            MinimalExternalModuleDependency::class.java,
            implementationValue
        )
        val configuration: Function1<ExternalModuleDependency, Unit> = mockk()

        fun dependencies(scope: KotlinDependencyHandler.() -> Unit) = scope.invoke(handler)

        // When
        dependencies {
            implementation(provider, configuration)
        }

        // Then
        verify(exactly = 1) { handler.implementation(module, configuration) }
    }
}
