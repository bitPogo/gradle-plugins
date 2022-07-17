/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.util

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class PlatformContextExtensionSpec {
    @Test
    fun `Given isAndroidLibrary is called, it returns false, if it is not an AndroidLibraryContext`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.JVM

        // When
        val result = context.isAndroidLibrary()

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isAndroidLibrary is called, it returns false, if it is an AndroidLibraryContext`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY

        // When
        val result = context.isAndroidLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given isAndroidLibrary is called, it returns false, if it is an AndroidLibraryContext for KMP`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP

        // When
        val result = context.isAndroidLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given isKmp is called with ANDROID_APPLICATION it return false`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION

        // When
        val result = context.isKmp()

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isKmp is called with ANDROID_LIBRARY it return false`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY

        // When
        val result = context.isKmp()

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isKmp is called with JVM it return false`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.JVM

        // When
        val result = context.isKmp()

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isKmp is called with ANDROID_APPLICATION_KMP it return false`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP

        // When
        val result = context.isKmp()

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given isKmp is called with ANDROID_LIBRARY_KMP it return false`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP

        // When
        val result = context.isKmp()

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given isKmp is called with JVM_KMP it return false`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.JVM_KMP

        // When
        val result = context.isKmp()

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given hasAndroidLibrary is called, it returns false, if the Set is empty`() {
        // Given
        val set = setOf(
            GradleUtilApiContract.PlatformContext.JVM_KMP,
        )

        // When
        val result = set.hasAndroidLibrary()

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given hasAndroidLibrary is called, it returns false, if the Set contains not an Android Library Variant`() {
        // Given
        val set = setOf<GradleUtilApiContract.PlatformContext>()

        // When
        val result = set.hasAndroidLibrary()

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given hasAndroidLibrary is called, it returns tre, if the Set contains a ANDROID_LIBRARY`() {
        // Given
        val set = setOf(GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY)

        // When
        val result = set.hasAndroidLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given hasAndroidLibrary is called, it returns true, if the Set contains a ANDROID_LIBRARY_KMP`() {
        // Given
        val set = setOf(GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP)

        // When
        val result = set.hasAndroidLibrary()

        // Then
        assertTrue(result)
    }
}
