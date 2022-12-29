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
    onWrongGroup: Function1<Function0<String>, Unit>,
    action: MinimalExternalModuleDependency.() -> T,
): T {
    val dependency = get()
    val group = dependency.module.group

    return dependency.action().also {
        if (group != guard) {
            onWrongGroup { "This is not a ${guard.substringAfterLast("-")} package." }
        }
    }
}

fun Provider<MinimalExternalModuleDependency>.asPythonPackage(): String {
    return guardDependency("python", ::error) {
        "${module.name}==$versionConstraint"
    }
}

fun KotlinDependencyHandler.nodeProductionPackage(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    val logger = project.logger

    return provider.guardDependency(
        "node-production",
        { message -> logger.warn(message()) },
    ) {
        npm(module.name, versionConstraint.toString())
    }
}

fun KotlinDependencyHandler.nodeDevelopmentPackage(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    val logger = project.logger

    return provider.guardDependency(
        "node-development",
        { message -> logger.warn(message()) },
    ) {
        devNpm(module.name, versionConstraint.toString())
    }
}

fun KotlinDependencyHandler.nodePeerPackage(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    val logger = project.logger

    return provider.guardDependency(
        "node-peer",
        { message -> logger.warn(message()) },
    ) {
        peerNpm(module.name, versionConstraint.toString())
    }
}

fun KotlinDependencyHandler.nodeOptionalPackage(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    val logger = project.logger

    return provider.guardDependency(
        "node-optional",
        { message -> logger.warn(message()) },
    ) {
        optionalNpm(module.name, versionConstraint.toString())
    }
}

fun KotlinDependencyHandler.nodePackage(provider: Provider<MinimalExternalModuleDependency>): Dependency {
    return when (provider.get().module.group) {
        "node-production" -> nodeProductionPackage(provider)
        "node-development" -> nodeDevelopmentPackage(provider)
        "node-peer" -> nodePeerPackage(provider)
        "node-optional" -> nodeOptionalPackage(provider)
        else -> throw IllegalArgumentException("Unknown package type.")
    }
}
