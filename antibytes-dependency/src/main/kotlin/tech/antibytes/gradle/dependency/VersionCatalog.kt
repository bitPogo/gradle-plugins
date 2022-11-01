/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import tech.antibytes.gradle.dependency.version.Android
import tech.antibytes.gradle.dependency.version.Js
import tech.antibytes.gradle.dependency.version.Jvm
import tech.antibytes.gradle.dependency.version.Kotlin
import tech.antibytes.gradle.dependency.version.MkDocs
import tech.antibytes.gradle.dependency.version.Square
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

private fun <T, V> VersionCatalogBuilder.addVersions(
    catalog: Any,
    aliasName: List<String>,
    property: KProperty1<T, V>
) {
    if (property.isConst) {
        version(
            aliasName.toDependencyName(property.name),
            property.call() as String
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
    prefix: List<String> = emptyList(),
) {
    val aliasName = prefix.toMutableList().apply {
        add(catalog.toDependencyName())
    }

    catalog::class.memberProperties.forEach { property -> this.addVersions(catalog, aliasName, property) }
}

internal fun VersionCatalogBuilder.addVersions() {
    addVersions(Android)
    addVersions(Gradle)
    addVersions(Js)
    addVersions(Jvm)
    addVersions(Kotlin)
    addVersions(MkDocs)
    addVersions(Square)
}

internal object Version {
    val gradle = Gradle

    // Kotlin
    val kotlin = Kotlin

    val square = Square

    val android = Android

    val jvm = Jvm

    val js = Js
}
