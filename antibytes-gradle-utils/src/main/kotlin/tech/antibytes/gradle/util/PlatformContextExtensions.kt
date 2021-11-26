/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.util

import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext

fun PlatformContext.isAndroidLibrary(): Boolean {
    return this == PlatformContext.ANDROID_LIBRARY || this == PlatformContext.ANDROID_LIBRARY_KMP
}

fun PlatformContext.isKmp(): Boolean {
    return when (this) {
        PlatformContext.ANDROID_APPLICATION_KMP -> true
        PlatformContext.ANDROID_LIBRARY_KMP -> true
        PlatformContext.JVM_KMP -> true
        else -> false
    }
}

fun Set<PlatformContext>.hasAndroidLibrary(): Boolean {
    return this.any { context -> context.isAndroidLibrary() }
}
