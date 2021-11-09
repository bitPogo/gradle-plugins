/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration

import org.gradle.api.Project

internal object PlatformContextResolver :
    ConfigurationContract.PlatformContextResolver {
    override fun getType(project: Project): ConfigurationContract.PlatformContext {
        TODO("Not yet implemented")
    }

    override fun isKmp(context: ConfigurationContract.PlatformContext): Boolean {
        return when (context) {
            ConfigurationContract.PlatformContext.ANDROID_APPLICATION_KMP -> true
            ConfigurationContract.PlatformContext.ANDROID_LIBRARY_KMP -> true
            ConfigurationContract.PlatformContext.JVM_KMP -> true
            else -> false
        }
    }
}
