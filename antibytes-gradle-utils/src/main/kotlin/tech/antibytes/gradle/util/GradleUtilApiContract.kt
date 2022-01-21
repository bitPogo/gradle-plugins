/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.util

import org.gradle.api.Project

interface GradleUtilApiContract {
    enum class PlatformContext(val prefix: String) {
        ANDROID_APPLICATION("android"),
        ANDROID_APPLICATION_KMP("android"),
        ANDROID_LIBRARY("android"),
        ANDROID_LIBRARY_KMP("android"),
        JVM("jvm"),
        JVM_KMP("jvm")
    }

    fun interface PlatformContextResolver {
        fun getType(project: Project): Set<PlatformContext>
    }
}
