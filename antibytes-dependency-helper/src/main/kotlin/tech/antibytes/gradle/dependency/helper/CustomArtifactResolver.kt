/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import com.squareup.tools.maven.resolution.ArtifactResolver
import java.io.File
import java.nio.file.Path
import org.apache.maven.model.Repository
import org.apache.maven.model.RepositoryPolicy
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.provider.Provider

fun Project.customArtifact(coordinates: String): Provider<File> {
    return this.provider {
        CustomArtifactResolver.getInstance(this).resolveArtifact(coordinates)
    }
}

internal object SquareArtifactResolverFactory {
    fun getInstance(
        cacheDir: Path,
        repositories: List<Repository>,
    ): ArtifactResolver = ArtifactResolver(
        suppressAddRepositoryWarnings = true,
        cacheDir = cacheDir,
        repositories = repositories,
    )
}

internal class CustomArtifactResolver private constructor(
    private val resolver: ArtifactResolver,
) : DependencyContract.CustomArtifactResolver {
    override fun resolveArtifact(coordinates: String): File {
        return resolver.download(coordinates, false).let { (_, artifact) ->
            artifact.toFile()
        }
    }

    companion object : DependencyContract.CustomArtifactResolverFactory {
        private fun Project.extractRepositories(): List<Repository> {
            return this.repositories.asMap.map { (name, repository) ->
                Repository().apply {
                    id = name
                    releases = RepositoryPolicy().apply { enabled = "true" }
                    url = (repository as MavenArtifactRepository).url.toURL().toExternalForm()
                }
            }
        }

        private fun File.ensure() {
            if (!exists()) {
                mkdirs().apply {
                    if (!this) {
                        throw FileSystemException(reason = "Cannot create Cache.", file = this@ensure)
                    }
                }
            }
        }

        override fun getInstance(project: Project): DependencyContract.CustomArtifactResolver {
            val cacheDir = File(project.rootDir, ".gradle/artifactCache").apply {
                ensure()
            }

            return CustomArtifactResolver(
                SquareArtifactResolverFactory.getInstance(
                    cacheDir = cacheDir.toPath(),
                    repositories = project.extractRepositories(),
                ),
            )
        }
    }
}
