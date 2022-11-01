/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import kotlin.reflect.full.memberProperties

private fun VersionCatalogBuilder.addDependencies(
    catalog: Any,
    prefix: List<String> = emptyList(),
) {
    val aliasName = prefix.toMutableList().apply {
        add(catalog.toDependencyName())
    }
    catalog::class.memberProperties.forEach { property ->
        val name = aliasName.toDependencyName(property.name)

        when (val artifact = property.call(catalog)!!) {
            is MavenArtifact -> {

            }
            is MavenVersionlessArtifact -> {

            }
            is NpmArtifact -> {

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

internal fun VersionCatalogBuilder.addDependencies() {

    addDependencies(MkDocs)
}
