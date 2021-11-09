/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration

import org.junit.Test
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract.PlatformContext
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlatformContextResolverSpec {
    @Test
    fun `It fulfils PlatformTypeResolver`() {
        val types: Any = PlatformContextResolver

        assertTrue(types is ConfigurationContract.PlatformContextResolver)
    }

    @Test
    fun `Given isKmp is called with ANDROID_APPLICATION it return false`() {
        assertFalse(
            PlatformContextResolver.isKmp(
                PlatformContext.ANDROID_APPLICATION
            )
        )
    }

    @Test
    fun `Given isKmp is called with ANDROID_LIBRARY it return false`() {
        assertFalse(
            PlatformContextResolver.isKmp(
                PlatformContext.ANDROID_LIBRARY
            )
        )
    }

    @Test
    fun `Given isKmp is called with ANDROID_JVM it return false`() {
        assertFalse(
            PlatformContextResolver.isKmp(
                PlatformContext.JVM
            )
        )
    }

    @Test
    fun `Given isKmp is called with ANDROID_APPLICATION_KMP it return false`() {
        assertTrue(
            PlatformContextResolver.isKmp(
                PlatformContext.ANDROID_APPLICATION_KMP
            )
        )
    }

    @Test
    fun `Given isKmp is called with ANDROID_LIBRARY_KMP it return false`() {
        assertTrue(
            PlatformContextResolver.isKmp(
                PlatformContext.ANDROID_LIBRARY_KMP
            )
        )
    }

    @Test
    fun `Given isKmp is called with JVM_KMP it return false`() {
        assertTrue(
            PlatformContextResolver.isKmp(
                PlatformContext.JVM_KMP
            )
        )
    }

    @Test
    fun `Given isKmp is called with UNKNOWN it return false`() {
        assertFalse(
            PlatformContextResolver.isKmp(
                PlatformContext.UNKNOWN
            )
        )
    }
}
