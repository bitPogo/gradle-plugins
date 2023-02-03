/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class VersionCatalogSpec {
    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `It contains Android Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-desugar", any<String>())
        }
    }

    @Test
    fun `It contains Android Ktx Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-ktx-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Ktx Activity Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-ktx-activity-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Ktx Navigation Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-ktx-navigation-ui", any<String>())
        }
    }

    @Test
    fun `It contains Android Ktx Paging Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-ktx-paging-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Ktx Viewmodel Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-ktx-viewmodel-saver", any<String>())
        }
    }

    @Test
    fun `It contains Android AppCompact Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-appCompact-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Coil Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-coil-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Compose Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-compose-runtime", any<String>())
        }
    }

    @Test
    fun `It contains Android Compose Animation Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-compose-animation-runtime", any<String>())
        }
    }

    @Test
    fun `It contains Android Compose Foundation Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-compose-foundation-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Compose UI Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-compose-ui-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Compose Tooling Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-compose-ui-tooling-core", any<String>())
        }
    }

    @Test
    fun `It contains Android ConstraintLayout Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-constraintLayout-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Material Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-material-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Material Compose Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-material-compose-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Material3 Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-material3-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Hilt Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-hilt-gradle-dependency", any<String>())
        }
    }

    @Test
    fun `It contains Android Test Compose Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-test-compose-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Test Espresso Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-test-espresso-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Test Junit Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-test-junit-core", any<String>())
        }
    }

    @Test
    fun `It contains Android Test Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("android-test-robolectric", any<String>())
        }
    }

    @Test
    fun `It contains Kotlin Stdlib Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-stdlib-common", any<String>())
        }
    }

    @Test
    fun `It contains Kotlin Test Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-test-annotations", any<String>())
        }
    }

    @Test
    fun `It contains Kotlin Scripting Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-scripting-core", any<String>())
        }
    }

    @Test
    fun `It contains Kotlin Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-kotlin-plugin", any<String>())
        }
    }

    @Test
    fun `It contains Kotlinx AtomicFu Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlinx-atomicfu-core", any<String>())
        }
    }

    @Test
    fun `It contains Kotlinx Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlinx-coroutines-core", any<String>())
        }
    }

    @Test
    fun `It contains MkDocs Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("python-mkdocs-includeMarkdown", any<String>())
        }
    }

    @Test
    fun `It contains Node Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("node-axios", any<String>())
        }
    }

    @Test
    fun `It contains Squares Okio Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-okio-core", any<String>())
        }
    }

    @Test
    fun `It contains Squares SqlDelight Driver Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-sqldelight-driver-android", any<String>())
        }
    }

    @Test
    fun `It contains Squares SqlDelight Coroutine Extension Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-sqldelight-coroutines", any<String>())
        }
    }

    @Test
    fun `It contains Squares OkHttp Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-okhttp-bom", any<String>())
        }
    }

    @Test
    fun `It contains Squares OkHttp Mockserver Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-okhttp-mockserver-core", any<String>())
        }
    }

    @Test
    fun `It contains Squares KotlinPoet Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("square-kotlinPoet-core", any<String>())
        }
    }

    @Test
    fun `It contains Ktor Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("ktor-events", any<String>())
        }
    }

    @Test
    fun `It contains Ktor Client Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("ktor-client-core", any<String>())
        }
    }

    @Test
    fun `It contains Ktor Serialisation Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("ktor-serialization-core", any<String>())
        }
    }

    @Test
    fun `It contains Ktor Server Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("ktor-server-core", any<String>())
        }
    }

    @Test
    fun `It contains SLF4J Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("slf4j-noop", any<String>())
        }
    }

    @Test
    fun `It contains Koin Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("koin-core", any<String>())
        }
    }

    @Test
    fun `It contains Stately Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("stately-collections", any<String>())
        }
    }

    @Test
    fun `It contains Vendor Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("uuid", any<String>())
        }
    }

    @Test
    fun `It contains Gradle Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("gradle-versioning-dependency", any<String>())
        }
    }

    @Test
    fun `It contains Gradle Publishing Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("gradle-publishing", any<String>())
        }
    }

    @Test
    fun `It contains Vendor Test Junit Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("test-junit-core", any<String>())
        }
    }

    @Test
    fun `It contains Vendor Test Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("test-mockk", any<String>())
        }
    }

    @Test
    fun `It contains Vendor Test Compiler Test Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("test-compiler-core", any<String>())
        }
    }

    @Test
    fun `It contains Antibytes Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("gradle-antibytes-runtimeConfig", any<String>())
        }
    }

    @Test
    fun `It contains KSP Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("gradle-ksp-runtime", any<String>())
        }
    }

    @Test
    fun `It contains Wrappers Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-wrappers-bom", any<String>())
        }
    }

    @Test
    fun `It contains Wrappers react Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-wrappers-react-main", any<String>())
        }
    }

    @Test
    fun `It contains Wrappers tanstack Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-wrappers-tanstack-virtualCore", any<String>())
        }
    }

    @Test
    fun `It contains Wrappers tanstack react Versions`() {
        // Given
        val catalog: VersionCatalogBuilder = mockk()
        every { catalog.version(any(), any<String>()) } returns "any"

        // When
        catalog.addVersions()

        // Then
        verify(exactly = 1) {
            catalog.version("kotlin-wrappers-tanstack-react-query", any<String>())
        }
    }
}
