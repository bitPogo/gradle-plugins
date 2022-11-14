/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import tech.antibytes.gradle.dependency.version.Android
import tech.antibytes.gradle.dependency.version.Gradle
import tech.antibytes.gradle.dependency.version.Koin
import tech.antibytes.gradle.dependency.version.Kotlin
import tech.antibytes.gradle.dependency.version.Kotlinx
import tech.antibytes.gradle.dependency.version.Ktor
import tech.antibytes.gradle.dependency.version.MkDocs
import tech.antibytes.gradle.dependency.version.Node
import tech.antibytes.gradle.dependency.version.Square
import tech.antibytes.gradle.dependency.version.Stately
import tech.antibytes.gradle.dependency.version.Vendor

private fun <T, V> KProperty1<T, V>.determineVersion(catalog: Any): String {
    return if (this.isConst) {
        this.call()
    } else {
        this.call(catalog)
    } as String
}

private fun <T, V> VersionCatalogBuilder.addVersions(
    catalog: Any,
    aliasName: List<String>,
    property: KProperty1<T, V>,
) {
    if (property.returnType.toString() == "kotlin.String") {
        version(
            aliasName.toDependencyName(),
            property.determineVersion(catalog),
        )
    } else {
        addVersions(
            catalog = property.call(catalog)!!,
            prefix = aliasName,
        )
    }
}

private fun VersionCatalogBuilder.addVersions(
    catalog: Any,
    prefix: List<String>,
) {
    catalog::class.memberProperties.forEach { property ->
        if (property.visibility != KVisibility.PRIVATE) {
            val aliasName = prefix.toMutableList().apply {
                add(property.name)
            }

            addVersions(catalog, aliasName, property)
        }
    }
}

private fun MutableList<String>.addIfNotVendor(
    catalog: Any,
) {
    if (catalog != Vendor) {
        add(catalog.toDependencyName())
    }
}

private fun VersionCatalogBuilder.addVersions(
    catalog: Any,
) {
    val prefix: List<String> = mutableListOf<String>().apply {
        addIfNotVendor(catalog)
    }

    addVersions(catalog, prefix)
}

internal fun VersionCatalogBuilder.addVersions() {
    addVersions(Android)
    addVersions(Gradle)
    addVersions(Koin)
    addVersions(Kotlin)
    addVersions(Kotlinx)
    addVersions(Ktor)
    addVersions(MkDocs)
    addVersions(Node)
    addVersions(Stately)
    addVersions(Square)
    addVersions(Vendor)
}
