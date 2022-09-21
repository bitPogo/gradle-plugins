/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

data class VersionDescriptor(
    val major: Int,
    val minor: Int,
    val patch: Int,
) {
    constructor(version: String) : this(
        major = extractMajorVersion(version),
        minor = extractMinorVersion(version),
        patch = extractPatchVersion(version)
    )

    override fun toString(): String = "$major${DELIMITER}$minor${DELIMITER}$patch"

    private companion object {
        const val DEFAULT_VERSION = 0
        const val DELIMITER = '.'

        private fun String.extract(position: Int): String? {
            val parts = this.split(DELIMITER)

            return try {
                parts[position]
            } catch (_: Throwable) {
                null
            }
        }

        fun extractMajorVersion(version: String): Int = version.substringBefore(DELIMITER).toInt()
        fun extractMinorVersion(version: String): Int {
            return version.extract(1)?.toInt() ?: DEFAULT_VERSION
        }

        fun extractPatchVersion(version: String): Int {
            return version.extract(2)?.toInt() ?: DEFAULT_VERSION
        }
    }
}
