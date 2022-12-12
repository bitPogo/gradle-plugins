/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

/* ktlint-disable filename */
package tech.antibytes.gradle.test

import groovy.lang.Closure

fun <T> createClosure(value: T): Closure<T> {
    return object : Closure<T>(value) {
        fun doCall(args: Any?): T = value
    }
}
