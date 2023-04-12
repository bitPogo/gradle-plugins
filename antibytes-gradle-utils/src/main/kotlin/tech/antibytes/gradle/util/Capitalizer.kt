/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.util

fun String.capitalize(): String {
    return replaceFirstChar {
        if (it.isLetter()) {
            it.uppercase()
        } else {
            it.toString()
        }
    }
}

fun String.decapitalize(): String = replaceFirstChar(Char::lowercase)
