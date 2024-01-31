/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.node

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import tech.antibytes.gradle.dependency.node.catalog.VersionCatalogBridge
import tech.antibytes.gradle.dependency.node.reader.NodeReader

fun VersionCatalogBuilder.nodeToDependencyCatalog(packageJsons: ConfigurableFileCollection) {
    packageJsons.forEach { file ->
        VersionCatalogBridge.addNodeDependencies(
            this,
            NodeReader.extractPackages(file),
        )
    }
}
