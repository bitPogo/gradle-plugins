/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
package tech.antibytes.gradle.dependency

import org.gradle.api.artifacts.dsl.RepositoryHandler
import tech.antibytes.gradle.dependency.DependencyContract.Credentials

object AntiBytesCustomDependencies {
    var githubGroups: List<String> = emptyList()
    var credentials: Credentials? = null

    private val repositories = listOf(
        "https://raw.github.com/bitPogo/maven-dev/main/dev",
        "https://raw.github.com/bitPogo/maven-snapshots/main/snapshots",
        // "https://raw.github.com/bitPogo/maven-releases/main/releases", just keeping it for now
    )

    fun RepositoryHandler.addCustomRepositories() {
        repositories.forEach { url ->
            maven {
                setUrl(url)
                if (AntiBytesCustomDependencies.credentials is Credentials) {
                    credentials {
                        username = AntiBytesCustomDependencies.credentials!!.username
                        password = AntiBytesCustomDependencies.credentials!!.password
                    }
                }
                content {
                    githubGroups.forEach { group -> includeGroup(group) }
                }
            }
        }
    }
}
