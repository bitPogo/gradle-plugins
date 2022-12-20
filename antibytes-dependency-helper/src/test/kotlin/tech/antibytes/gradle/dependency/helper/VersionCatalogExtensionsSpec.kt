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
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty

class VersionCatalogExtensionsSpec {
    private val fixture = kotlinFixture()

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
            implementationValue,
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
