/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import tech.antibytes.gradle.dependency.module.Koin
import tech.antibytes.gradle.dependency.module.Kotlinx
import tech.antibytes.gradle.dependency.module.Ktor
import tech.antibytes.gradle.dependency.module.MkDocs
import tech.antibytes.gradle.dependency.module.Node
import tech.antibytes.gradle.dependency.module.Vendor
import tech.antibytes.gradle.dependency.module.Square
import tech.antibytes.gradle.dependency.module.Stately

private fun String.extractNodeNamespace(): String = "node-${split('-', limit = 3)[1]}"

private fun String.extractNodeVersion(): String = "node-${split('-', limit = 3)[2]}"

private val Platform.platformId: String
    get() = platform.toLowerCase()

private fun String.injectPlatform(platform: Platform, infix: String = ""): String {
    return if (infix.isEmpty()) {
        "${platform.platformId}-$this"
    } else {
        "${platform.platformId}-$infix-$this"
    }
}

private fun String.toPlatformDependency(platform: Platform): String {
    return "$this-${platform.platformId}"
}

private fun Any.determineInfix(): String {
    return if (this is TestArtifact) {
        "test"
    } else {
        ""
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
    aliasName: String,
    artifact: KmpArtifact,
) {
    artifact.platforms.forEach { platform ->
        val infix = artifact.determineInfix()
        val name = aliasName.injectPlatform(platform, infix)
            .removeDoubles("test")

        if (platform == Platform.COMMON) {
            library(
                name,
                artifact.group,
                artifact.id,
            ).versionRef(aliasName)
        } else {
            library(
                name,
                artifact.group,
                artifact.id.toPlatformDependency(platform),
            ).versionRef(aliasName)
        }
    }
}

private fun VersionCatalogBuilder.addDependencies(
    aliasName: String,
    artifact: SinglePlatformArtifact,
) {
    val infix = artifact.determineInfix()
    val name = aliasName.injectPlatform(artifact.platform, infix)
        .removeDoubles(artifact.platform.platform)
        .removeDoubles("test")

    library(
        name,
        artifact.group,
        artifact.id,
    ).versionRef(aliasName)
}

private fun VersionCatalogBuilder.addDependencies(
    aliasName: String,
    artifact: VersionlessArtifact,
) {
    val infix = artifact.determineInfix()
    val name = aliasName.injectPlatform(artifact.platform, infix)
        .removeDoubles(artifact.platform.platform)
        .removeDoubles("test")

    library(
        name,
        artifact.group,
        artifact.id,
    ).withoutVersion()
}


private fun VersionCatalogBuilder.addDependencies(
    aliasName: String,
    artifact: Plugin,
) {
    plugin(
        aliasName.removeDoubles("plugin"),
        artifact.id
    ).versionRef(aliasName)
}

private fun VersionCatalogBuilder.addDependencies(
    aliasName: String,
    artifact: NodeArtifact,
) {
    library(
        aliasName,
        aliasName.extractNodeNamespace(),
        artifact.id,
    ).versionRef(aliasName.extractNodeVersion())
}

private fun VersionCatalogBuilder.addDependencies(
    aliasName: String,
    artifact: PythonArtifact,
) {
    library(
        aliasName,
        "python",
        artifact.id,
    ).versionRef(aliasName)
}

private fun MutableList<String>.addIfNotVendor(
    catalog: Any
) {
    if (catalog != Vendor) {
        add(catalog.toDependencyName())
    }
}

private fun VersionCatalogBuilder.addDependencies(
    catalog: Any,
    prefix: List<String>,
) {
    catalog::class.memberProperties.forEach { property ->
        if (property.visibility == KVisibility.PUBLIC) {
            val aliasName = prefix.toMutableList().apply {
                add(property.name)
            }
            val name = aliasName.toDependencyName()

            when (val artifact = property.call(catalog)!!) {
                is SinglePlatformArtifact -> addDependencies(name, artifact)
                is VersionlessArtifact -> addDependencies(name, artifact)
                is KmpArtifact -> addDependencies(name, artifact)
                is Plugin -> addDependencies(name, artifact)
                is NodeArtifact -> addDependencies(name, artifact)
                is PythonArtifact -> addDependencies(name, artifact)
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

private fun VersionCatalogBuilder.addDependencies(
    catalog: Any,
) {
    val prefix: List<String> = mutableListOf<String>().apply {
        addIfNotVendor(catalog)
    }

    addDependencies(catalog, prefix)
}

internal fun VersionCatalogBuilder.addDependencies() {
    addDependencies(Koin)
    addDependencies(Kotlinx)
    addDependencies(Ktor)
    addDependencies(MkDocs)
    addDependencies(Node)
    addDependencies(Stately)
    addDependencies(Square)
    addDependencies(Vendor)
}
