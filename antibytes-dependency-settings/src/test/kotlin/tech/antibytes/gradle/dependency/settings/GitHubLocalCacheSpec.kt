/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
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
    @ClearEnvironmentVariable(key = "AZURE_HTTP_USER_AGENT")
    @SetEnvironmentVariable(key = "GITHUB_REPOSITORY", value = "/User/runner/worker/...")
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
                ".gradle${File.separator}build-cache",
            )
        }
        verify(exactly = 1) { gitHubCache.removeUnusedEntriesAfterDays = 3 }
    }

    @Test
    @SetEnvironmentVariable(key = "AZURE_HTTP_USER_AGENT", value = "whatever the value is")
    @ClearEnvironmentVariable(key = "GITHUB_REPOSITORY")
    fun `Given localGitHub is called it disables the BuildCacheConfiguration for Azure`() {
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
                ".gradle${File.separator}build-cache",
            )
        }
        verify(exactly = 1) { gitHubCache.removeUnusedEntriesAfterDays = 3 }
    }
}
