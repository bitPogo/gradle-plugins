/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.settings

import java.io.File
import org.gradle.caching.configuration.BuildCacheConfiguration

private const val expiryDays = 10
private val ciTargetDirectory = ".gradle${File.separator}build-cache"
private const val localTargetDirectory = "build-cache"

private fun BuildCacheConfiguration.configureCache(
    enabled: Boolean,
    rootDir: File,
    targetDirectory: String,
) {
    local {
        isEnabled = enabled
        directory = File(rootDir, targetDirectory)
        removeUnusedEntriesAfterDays = expiryDays
    }
}

@Deprecated("use ciCache")
fun BuildCacheConfiguration.localGithub() {
    local {
        isEnabled = isGitHub()
        directory = File(localTargetDirectory)
        removeUnusedEntriesAfterDays = 3
    }
}

fun BuildCacheConfiguration.ciCache(rootDir: File) {
    configureCache(
        isGitHub() || isAzureDevops(),
        rootDir,
        ciTargetDirectory,
    )
}

fun BuildCacheConfiguration.localCache(rootDir: File) {
    configureCache(
        !isGitHub() && !isAzureDevops(),
        rootDir,
        localTargetDirectory,
    )
}

fun BuildCacheConfiguration.fullCache(rootDir: File) {
    val targetDirectory = if (!isGitHub() && !isAzureDevops()) {
        localTargetDirectory
    } else {
        ciTargetDirectory
    }

    configureCache(true, rootDir, targetDirectory)
}
