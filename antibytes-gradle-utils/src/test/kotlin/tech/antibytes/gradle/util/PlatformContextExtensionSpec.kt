/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.util

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
}
