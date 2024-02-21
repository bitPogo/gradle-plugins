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

class CICacheSpec {
    @Test
    @ClearEnvironmentVariable(key = "AZURE_HTTP_USER_AGENT")
    @SetEnvironmentVariable(key = "GITHUB_REPOSITORY", value = "/User/runner/worker/...")
    fun `Given ciCache is called it configures the BuildCacheConfiguration for GitHub`() {
        // Given
        val buildCache: BuildCacheConfiguration = mockk()
        val gitHubCache: DirectoryBuildCache = mockk(relaxed = true)
        val root = File("somewhere")

        invokeGradleAction(gitHubCache) { localCache ->
            buildCache.local(localCache)
        }
        // When
        buildCache.ciCache(root)

        // Then
        verify(exactly = 1) { gitHubCache.isEnabled = true }
        verify(exactly = 1) {
            gitHubCache.directory = File(
                root,
                ".gradle${File.separator}build-cache",
            )
        }
        verify(exactly = 1) { gitHubCache.removeUnusedEntriesAfterDays = 10 }
    }

    @Test
    @ClearEnvironmentVariable(key = "AZURE_HTTP_USER_AGENT")
    @ClearEnvironmentVariable(key = "GITHUB_REPOSITORY")
    fun `Given ciCache is called it disables the BuildCacheConfiguration if no GITHUB or AZURE_HTTP_USER_AGENT Env was found`() {
        // Given
        val buildCache: BuildCacheConfiguration = mockk()
        val gitHubCache: DirectoryBuildCache = mockk(relaxed = true)
        val root = File("somewhere")

        invokeGradleAction(gitHubCache) { localCache ->
            buildCache.local(localCache)
        }
        // When
        buildCache.ciCache(root)

        // Then
        verify(exactly = 1) { gitHubCache.isEnabled = false }
        verify(exactly = 1) {
            gitHubCache.directory = File(
                root,
                ".gradle${File.separator}build-cache",
            )
        }
        verify(exactly = 1) { gitHubCache.removeUnusedEntriesAfterDays = 10 }
    }

    @Test
    @ClearEnvironmentVariable(key = "GITHUB_REPOSITORY")
    @SetEnvironmentVariable(key = "AZURE_HTTP_USER_AGENT", value = "whatever the value is")
    fun `Given ciCache is called it configures the BuildCacheConfiguration for AzureDevops`() {
        // Given
        val buildCache: BuildCacheConfiguration = mockk()
        val azureCache: DirectoryBuildCache = mockk(relaxed = true)
        val root = File("somewhere")

        invokeGradleAction(azureCache) { localCache ->
            buildCache.local(localCache)
        }
        // When
        buildCache.ciCache(root)

        // Then
        verify(exactly = 1) { azureCache.isEnabled = true }
        verify(exactly = 1) {
            azureCache.directory = File(
                root,
                ".gradle${File.separator}build-cache",
            )
        }
        verify(exactly = 1) { azureCache.removeUnusedEntriesAfterDays = 10 }
    }
}
