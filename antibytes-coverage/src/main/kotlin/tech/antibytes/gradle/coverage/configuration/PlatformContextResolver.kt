/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract.PlatformContext

internal object PlatformContextResolver : ConfigurationContract.PlatformContextResolver {
    private fun isKmp(project: Project): Boolean {
        return project.plugins.findPlugin("org.jetbrains.kotlin.multiplatform") is Plugin<*>
    }

    private fun isAndroidApplication(project: Project): Boolean {
        return project.plugins.findPlugin("com.android.application") is Plugin<*>
    }

    private fun isAndroidLibrary(project: Project): Boolean {
        return project.plugins.findPlugin("com.android.library") is Plugin<*>
    }

    private fun determineContext(project: Project): Set<PlatformContext> {
        val context = when {
            isAndroidApplication(project) -> PlatformContext.ANDROID_APPLICATION
            isAndroidLibrary(project) -> PlatformContext.ANDROID_LIBRARY
            else -> PlatformContext.JVM
        }

        return setOf(context)
    }

    private fun isJvmKmp(project: Project): Boolean {
        val kotlin = project.extensions.getByName("kotlin") as KotlinMultiplatformExtension
        return kotlin.targets.asMap.containsKey(PlatformContext.JVM_KMP.prefix)
    }

    private fun determineKmpContext(project: Project): Set<PlatformContext> {
        val contexts: MutableSet<PlatformContext> = mutableSetOf()

        if (isJvmKmp(project)) {
            contexts.add(PlatformContext.JVM_KMP)
        }

        if (isAndroidApplication(project)) {
            contexts.add(PlatformContext.ANDROID_APPLICATION_KMP)
        }

        if (isAndroidLibrary(project)) {
            contexts.add(PlatformContext.ANDROID_LIBRARY_KMP)
        }

        return contexts
    }

    override fun getType(project: Project): Set<PlatformContext> {
        return if (isKmp(project)) {
            determineKmpContext(project)
        } else {
            determineContext(project)
        }
    }

    override fun isKmp(context: PlatformContext): Boolean {
        return when (context) {
            PlatformContext.ANDROID_APPLICATION_KMP -> true
            PlatformContext.ANDROID_LIBRARY_KMP -> true
            PlatformContext.JVM_KMP -> true
            else -> false
        }
    }
}
