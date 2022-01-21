/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.util

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext

object PlatformContextResolver : GradleUtilApiContract.PlatformContextResolver {
    private fun isAndroidApplication(project: Project): Boolean {
        return project.plugins.hasPlugin("com.android.application")
    }

    private fun determineContext(project: Project): Set<PlatformContext> {
        val context = when {
            isAndroidApplication(project) -> PlatformContext.ANDROID_APPLICATION
            project.isAndroidLibrary() -> PlatformContext.ANDROID_LIBRARY
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

        if (project.isAndroidLibrary()) {
            contexts.add(PlatformContext.ANDROID_LIBRARY_KMP)
        }

        return contexts
    }

    override fun getType(project: Project): Set<PlatformContext> {
        return if (project.isKmp()) {
            determineKmpContext(project)
        } else {
            determineContext(project)
        }
    }
}
