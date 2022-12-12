/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

private fun <T> Provider<MinimalExternalModuleDependency>.guardDependency(
    guard: String,
    action: MinimalExternalModuleDependency.() -> T,
): T {
    val dependency = get()
    val group = dependency.module.group

    return if (group != guard) {
        throw IllegalArgumentException("This is not a $group package.")
    } else {
        dependency.action()
    }
}

fun Provider<MinimalExternalModuleDependency>.asPythonPackage(): String {
    return guardDependency("python") {
        "${module.name}==$versionConstraint"
    }
}

fun KotlinDependencyHandler.asNodeProdPackage(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    return provider.guardDependency("node-production") {
        npm(module.name, versionConstraint.toString())
    }
}

fun KotlinDependencyHandler.asNodeDevPackage(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    return provider.guardDependency("node-development") {
        devNpm(module.name, versionConstraint.toString())
    }
}

fun KotlinDependencyHandler.asNodePeerPackage(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    return provider.guardDependency("node-peer") {
        peerNpm(module.name, versionConstraint.toString())
    }
}

fun KotlinDependencyHandler.asNodeOptionalPackage(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    return provider.guardDependency("node-optional") {
        optionalNpm(module.name, versionConstraint.toString())
    }
}
