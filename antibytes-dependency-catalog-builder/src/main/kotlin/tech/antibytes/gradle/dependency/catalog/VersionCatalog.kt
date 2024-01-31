/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog

import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import tech.antibytes.gradle.dependency.catalog.version.Android
import tech.antibytes.gradle.dependency.catalog.version.Gradle
import tech.antibytes.gradle.dependency.catalog.version.Jetbrains
import tech.antibytes.gradle.dependency.catalog.version.Koin
import tech.antibytes.gradle.dependency.catalog.version.Kotlin
import tech.antibytes.gradle.dependency.catalog.version.Kotlinx
import tech.antibytes.gradle.dependency.catalog.version.Ktor
import tech.antibytes.gradle.dependency.catalog.version.Node
import tech.antibytes.gradle.dependency.catalog.version.Python
import tech.antibytes.gradle.dependency.catalog.version.Square
import tech.antibytes.gradle.dependency.catalog.version.Stately
import tech.antibytes.gradle.dependency.catalog.version.Vendor

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
    addVersions(Jetbrains)
    addVersions(Koin)
    addVersions(Kotlin)
    addVersions(Kotlinx)
    addVersions(Ktor)
    addVersions(Python)
    addVersions(Node)
    addVersions(Stately)
    addVersions(Square)
    addVersions(Vendor)
}
