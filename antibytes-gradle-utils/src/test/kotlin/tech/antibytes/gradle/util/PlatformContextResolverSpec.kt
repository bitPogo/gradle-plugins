/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.util

import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext

class PlatformContextResolverSpec {
    @Test
    fun `It fulfils PlatformTypeResolver`() {
        val types: Any = PlatformContextResolver

        assertTrue(types is GradleUtilApiContract.PlatformContextResolver)
    }

    @Test
    fun `Given getType is called with a Project, it returns a Set which contains only JVM context if the project is vanilla Java`() {
        // Given
        val project: Project = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns false

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.JVM),
            actual = result,
        )
    }

    @Test
    fun `Given getType is called with a Project, it returns a Set which contains JVM KMP context if the project has a JVM target and KMP is in use`() {
        // Given
        val project: Project = mockk()

        val kotlinExtension: KotlinMultiplatformExtension = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false

        every { project.extensions.getByName("kotlin") } returns kotlinExtension
        every { kotlinExtension.targets.asMap } returns sortedMapOf("jvm" to mockk())

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.JVM_KMP),
            actual = result,
        )
    }

    @Test
    fun `Given getType is called with a Project, it returns a Set which contains only a AndroidLibrary context if the project is a AndroidLibrary`() {
        // Given
        val project: Project = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns true

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.ANDROID_LIBRARY),
            actual = result,
        )
    }

    @Test
    fun `Given getType is called with a Project, it returns a Set which contains a AndroidLibrary context for KMP if the project is a AndroidLibrary and KMP is in use`() {
        // Given
        val project: Project = mockk()

        val kotlinExtension: KotlinMultiplatformExtension = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns true

        every { project.extensions.getByName("kotlin") } returns kotlinExtension
        every { kotlinExtension.targets.asMap } returns sortedMapOf("android" to mockk())

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.ANDROID_LIBRARY_KMP),
            actual = result,
        )
    }

    @Test
    fun `Given getType is called with a Project, it returns a Set which contains only a AndroidApplication context if the project is a AndroidApplication`() {
        // Given
        val project: Project = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns true
        every { project.plugins.hasPlugin("com.android.library") } returns false

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.ANDROID_APPLICATION),
            actual = result,
        )
    }

    @Test
    fun `Given getType is called with a Project, it returns a Set which contains a AndroidApplication context for KMP if the project is a AndroidApplication and KMP is in use`() {
        // Given
        val project: Project = mockk()

        val kotlinExtension: KotlinMultiplatformExtension = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.plugins.hasPlugin("com.android.application") } returns true
        every { project.plugins.hasPlugin("com.android.library") } returns false

        every { project.extensions.getByName("kotlin") } returns kotlinExtension
        every { kotlinExtension.targets.asMap } returns sortedMapOf("android" to mockk())

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.ANDROID_APPLICATION_KMP),
            actual = result,
        )
    }

    @Test
    fun `Given getType is called with a Project, it returns a Set which contains a multiple contexts for KMP if the project uses KMP and has multiple targets`() {
        // Given
        val project: Project = mockk()

        val kotlinExtension: KotlinMultiplatformExtension = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.plugins.hasPlugin("com.android.application") } returns true
        every { project.plugins.hasPlugin("com.android.library") } returns false

        every { project.extensions.getByName("kotlin") } returns kotlinExtension
        every { kotlinExtension.targets.asMap } returns sortedMapOf(
            "android" to mockk(),
            "common" to mockk(),
            "jvm" to mockk(),
        )

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(
                PlatformContext.ANDROID_APPLICATION_KMP,
                PlatformContext.JVM_KMP,
            ),
            actual = result,
        )
    }

    @Test
    fun `Given getType is called with a Project, it returns a empty Set which contains a multiple contexts for KMP if the project uses KMP and no target is known`() {
        // Given
        val project: Project = mockk()

        val kotlinExtension: KotlinMultiplatformExtension = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns false

        every { project.extensions.getByName("kotlin") } returns kotlinExtension
        every { kotlinExtension.targets.asMap } returns sortedMapOf(
            "common" to mockk(),
            "iOS" to mockk(),
        )

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = emptySet(),
            actual = result,
        )
    }
}
