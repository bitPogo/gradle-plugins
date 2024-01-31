/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog

import java.util.Locale

internal fun List<String>.toDependencyName(): String = this.joinToString("-")

private fun String.decapitalize(): String = replaceFirstChar { it.lowercase(Locale.getDefault()) }

internal fun Any.toDependencyName(): String = this::class.simpleName!!.decapitalize()
