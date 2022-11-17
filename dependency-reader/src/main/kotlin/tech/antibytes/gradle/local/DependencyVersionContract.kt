/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local

import java.io.File

internal interface DependencyVersionContract {
    fun interface DependencyReader<T : Any> {
        fun extractVersions(): T
    }

    interface ReaderFactory {
        fun getPythonReader(file: File): DependencyReader<Map<String, String>>
    }

    companion object {
        const val PYTHON_SEPARATOR = "=="
    }
}
