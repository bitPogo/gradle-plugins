/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:filename")

package tech.antibytes.gradle.test

import groovy.lang.Closure

fun <T> createClosure(value: T): Closure<T> {
    return object : Closure<T>(value) {
        fun doCall(args: Any?): T = value
    }
}
