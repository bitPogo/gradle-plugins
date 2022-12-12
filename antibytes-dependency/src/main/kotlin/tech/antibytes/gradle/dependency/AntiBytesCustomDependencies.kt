/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
package tech.antibytes.gradle.dependency

import org.gradle.api.artifacts.dsl.RepositoryHandler
import tech.antibytes.gradle.dependency.DependencyContract.Credentials
import tech.antibytes.gradle.dependency.DependencyContract.Repository

data class CustomRepository(
    override val url: String,
    override val groupIds: List<String> = emptyList(),
    override val credentials: Credentials? = null,
) : Repository

enum class AntibytesUrl(val url: String) {
    DEV("https://raw.github.com/bitPogo/maven-dev/main/dev"),
    SNAPSHOT("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots"),
    ROLLING("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling"),
    // RELEASE("https://raw.github.com/bitPogo/maven-releases/main/releases"), just keeping it for now
}

data class AntibytesRepository internal constructor(
    override val url: String,
    override var groupIds: List<String>,
    override var credentials: Credentials?,
) : Repository {
    constructor(
        url: AntibytesUrl,
        groupIds: List<String> = emptyList(),
        credentials: Credentials? = null,
    ) : this(
        url = url.url,
        groupIds = groupIds,
        credentials = credentials,
    )
}

fun RepositoryHandler.addCustomRepositories(
    repositories: List<Repository>,
) {
    repositories.forEach { repository ->
        maven {
            setUrl(repository.url)
            if (repository.credentials is Credentials) {
                credentials {
                    username = repository.credentials!!.username
                    password = repository.credentials!!.password
                }
            }
            content {
                repository.groupIds.forEach { group -> includeGroup(group) }
            }
        }
    }
}
