/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.helper

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

    fun interface Configurator {
        fun configure(project: Project)
    }

    companion object {
        const val EXTENSION_ID = "antibytesDependencyHelper"
        val DEPENDENCIES = listOf(
            "com.github.ben-manes.versions",
            "org.owasp.dependencycheck",
        )
    }
}
