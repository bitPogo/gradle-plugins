/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.util

import org.gradle.api.Project

fun Project.isRoot(): Boolean = this == rootProject

fun Project.applyIfNotExists(vararg pluginNames: String) {
    pluginNames.forEach { pluginName ->
        if (!this.plugins.hasPlugin(pluginName)) {
            this.plugins.apply(pluginName)
        }
    }
}

fun Project.isKmp(): Boolean {
    return this.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")
}

fun Project.isAndroidLibrary(): Boolean {
    return this.plugins.hasPlugin("com.android.library")
}

fun Project.isAndroidApplication(): Boolean {
    return this.plugins.hasPlugin("com.android.application")
}
