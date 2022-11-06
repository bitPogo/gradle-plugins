/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import tech.antibytes.gradle.dependency.module.Kotlinx
import tech.antibytes.gradle.dependency.module.Ktor
import tech.antibytes.gradle.dependency.module.MkDocs
import tech.antibytes.gradle.dependency.module.Node

private fun String.extractNodeNamespace(): String = "node-${split('-', limit = 3)[1]}"

private fun String.extractNodeVersion(): String = "node-${split('-', limit = 3)[2]}"

private val Platform.platformId: String
    get() = platform.toLowerCase()

private fun String.injectPlatform(platform: Platform): String {
    return "${platform.platformId}-$this"
}

private fun String.toPlatformDependency(platform: Platform): String {
    return "$this-${platform.platformId}"
}

private fun VersionCatalogBuilder.addDependencies(
    aliasName: String,
    artifact: MavenKmpArtifact,
) {
    artifact.platforms.forEach { platform ->
        val name = aliasName.injectPlatform(platform)

        if (platform == Platform.COMMON) {
            library(
                name,
                artifact.group,
                artifact.id,
            ).version(aliasName)
        } else {
            library(
                name,
                artifact.group,
                artifact.id.toPlatformDependency(platform),
            ).version(aliasName)
        }
    }
}

private fun String.removeDoubles(double: String): String {
    return if (this.endsWith("-$double")) {
        this.substringBeforeLast('-')
    } else {
        this
    }
}

private fun VersionCatalogBuilder.addDependencies(
    catalog: Any,
    prefix: List<String> = emptyList(),
) {
    val aliasName = prefix.toMutableList().apply {
        add(catalog.toDependencyName())
    }
    catalog::class.memberProperties.forEach { property ->
        if (property.visibility == KVisibility.PUBLIC) {
            val name = aliasName.toDependencyName(property.name)

            when (val artifact = property.call(catalog)!!) {
                is MavenArtifact -> {
                    library(
                        "${artifact.type.platform}-$name".removeDoubles(artifact.type.platform),
                        artifact.group,
                        artifact.id,
                    ).version(name)
                }
                is MavenVersionlessArtifact -> {
                    library(
                        "bom-$name".removeDoubles("bom"),
                        artifact.group,
                        artifact.id,
                    ).withoutVersion()
                }
                is MavenKmpArtifact -> addDependencies(name, artifact)
                is NodeArtifact -> {
                    library(
                        name,
                        name.extractNodeNamespace(),
                        artifact.id,
                    ).version(name.extractNodeVersion())
                }
                is PythonArtifact -> {
                    library(
                        name,
                        "python",
                        artifact.id,
                    ).version(name)
                }
                else -> {
                    addDependencies(
                        catalog = artifact,
                        prefix = aliasName,
                    )
                }
            }
        }
    }
}

internal fun VersionCatalogBuilder.addDependencies() {
    addDependencies(Kotlinx)
    addDependencies(Ktor)
    addDependencies(MkDocs)
    addDependencies(Node)
}
