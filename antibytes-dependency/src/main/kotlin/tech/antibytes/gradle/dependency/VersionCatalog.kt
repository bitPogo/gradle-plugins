/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import tech.antibytes.gradle.dependency.version.Android
import tech.antibytes.gradle.dependency.version.Google
import tech.antibytes.gradle.dependency.version.Js
import tech.antibytes.gradle.dependency.version.Jvm
import tech.antibytes.gradle.dependency.version.Kotlin
import tech.antibytes.gradle.dependency.version.Npm
import tech.antibytes.gradle.dependency.version.Square
import kotlin.reflect.full.memberProperties

internal fun VersionCatalogBuilder.addVersions(
    catalog: Any,
    prefix: List<String> = emptyList(),
) {
    val aliasName = prefix.toMutableList().apply {
        add(catalog::class.simpleName!!.decapitalize())
    }
    catalog::class.memberProperties.forEach { property ->
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
}

internal fun VersionCatalogBuilder.addVersions() {
    addVersions(Android)
    addVersions(Google)
    addVersions(Js)
    addVersions(Jvm)
    addVersions(Kotlin)
    addVersions(Npm)
    addVersions(Square)
}

internal object Version {

    val gradle = Gradle

    // Kotlin
    val kotlin = Kotlin

    val square = Square

    val android = Android

    val jvm = Jvm

    val google = Google

    val js = Js

    val npm = Npm
}
