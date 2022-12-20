/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

fun KotlinDependencyHandler.implementation(
    provider: Provider<MinimalExternalModuleDependency>,
    configuration: ExternalModuleDependency.() -> Unit,
): ExternalModuleDependency {
    return implementation(
        provider.get().toString(),
        configuration,
    )
}
