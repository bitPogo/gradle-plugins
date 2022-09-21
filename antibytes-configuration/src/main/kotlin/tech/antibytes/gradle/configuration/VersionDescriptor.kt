/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

internal data class VersionDescriptor(
    val major: Int,
    val minor: Int,
    val patch: Int,
) : Comparable<VersionDescriptor> {
    constructor(version: String) : this(
        major = extractMajorVersion(version),
        minor = extractMinorVersion(version),
        patch = extractPatchVersion(version),
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

    override operator fun compareTo(other: VersionDescriptor): Int {
        return when {
            other.major != this.major -> this.major - other.major
            other.minor != this.minor -> this.minor - other.minor
            other.patch != this.patch -> this.patch - other.patch
            else -> 0
        }
    }
}
