/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.settings

import java.io.File
import org.gradle.caching.configuration.BuildCacheConfiguration

fun BuildCacheConfiguration.localCache(rootDir: File) {
    local {
        isEnabled = !isGitHub() && !isAzureDevops()
        directory = File(
            rootDir,
            "build-cache",
        )
        removeUnusedEntriesAfterDays = 10
    }
}
