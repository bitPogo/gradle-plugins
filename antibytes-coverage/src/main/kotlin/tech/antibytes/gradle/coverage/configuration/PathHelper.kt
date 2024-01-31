/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:filename")

package tech.antibytes.gradle.coverage.configuration

import java.io.File

internal fun makePath(vararg path: String): String = makePath(path.toList())

internal fun makePath(path: List<String>): String = path.joinToString(File.separator.toString())
