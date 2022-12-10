/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import java.io.File
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

interface DependencyContract {
    interface DependencyPluginExtension {
        val keywords: SetProperty<String>
        val versionRegex: Property<Regex>
    }

    interface Update {
        fun configure(
            project: Project,
            configuration: DependencyPluginExtension,
        )
    }

    data class Credentials(
        val username: String,
        val password: String,
    )

    interface Repository {
        val url: String
        val groupIds: List<String>
        val credentials: Credentials?
    }

    fun interface CustomArtifactResolver {
        fun resolveArtifact(coordinates: String): File
    }

    fun interface CustomArtifactResolverFactory {
        fun getInstance(project: Project): CustomArtifactResolver
    }

    companion object {
        const val EXTENSION_ID = "antiBytesDependency"
        val DEPENDENCIES = listOf(
            "com.github.ben-manes.versions",
            "org.owasp.dependencycheck",
        )
    }
}
