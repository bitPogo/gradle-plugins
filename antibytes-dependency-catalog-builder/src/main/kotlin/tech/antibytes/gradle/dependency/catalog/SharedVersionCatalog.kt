/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("UnstableApiUsage")

package tech.antibytes.gradle.dependency.catalog

import org.gradle.api.plugins.catalog.CatalogPluginExtension

fun CatalogPluginExtension.addSharedAntibytesConfiguration() {
    versionCatalog {
        addVersions()
        addDependencies()
    }
}
