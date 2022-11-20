/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
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
