/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.settings

import io.mockk.mockk
import io.mockk.verify
import java.io.File
import org.gradle.caching.configuration.BuildCacheConfiguration
import org.gradle.caching.local.DirectoryBuildCache
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.ClearEnvironmentVariable
import org.junitpioneer.jupiter.SetEnvironmentVariable
import tech.antibytes.gradle.test.invokeGradleAction

class GitHubLocalCacheSpec {
    @Test
    @SetEnvironmentVariable(key = "GITHUB", value = "/User/runner/worker/...")
    fun `Given localGitHub is called it configures the BuildCacheConfiguration for GitHub`() {
        // Given
        val buildCache: BuildCacheConfiguration = mockk()
        val gitHubCache: DirectoryBuildCache = mockk(relaxed = true)

        invokeGradleAction(gitHubCache) { localCache ->
            buildCache.local(localCache)
        }
        // When
        buildCache.localGithub()

        // Then
        verify(exactly = 1) { gitHubCache.isEnabled = true }
        verify(exactly = 1) {
            gitHubCache.directory = File(
                "/User/runner/.gradle/caches",
                "build-cache",
            )
        }
        verify(exactly = 1) { gitHubCache.removeUnusedEntriesAfterDays = 3 }
    }

    @Test
    @ClearEnvironmentVariable(key = "GITHUB")
    fun `Given localGitHub is called it disables the BuildCacheConfiguration for GitHub if no GITHUB Env was found`() {
        // Given
        val buildCache: BuildCacheConfiguration = mockk()
        val gitHubCache: DirectoryBuildCache = mockk(relaxed = true)

        invokeGradleAction(gitHubCache) { localCache ->
            buildCache.local(localCache)
        }
        // When
        buildCache.localGithub()

        // Then
        verify(exactly = 1) { gitHubCache.isEnabled = false }
        verify(exactly = 1) {
            gitHubCache.directory = File(
                ".gradle/caches",
                "build-cache",
            )
        }
        verify(exactly = 1) { gitHubCache.removeUnusedEntriesAfterDays = 3 }
    }
}
