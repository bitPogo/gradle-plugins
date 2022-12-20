/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.PluginDependenciesSpecScope
import org.gradle.plugin.use.PluginDependency
import org.gradle.plugin.use.PluginDependencySpec
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

fun PluginDependenciesSpecScope.id(
    catalogIg: Provider<PluginDependency>
): PluginDependencySpec = id(catalogIg.get().pluginId)

fun KotlinDependencyHandler.implementation(
    provider: Provider<MinimalExternalModuleDependency>,
    configuration: ExternalModuleDependency.() -> Unit
): ExternalModuleDependency {
    return implementation(
        provider.get().toString(),
        configuration
    )
}
