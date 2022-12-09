/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import com.squareup.tools.maven.resolution.ArtifactResolver
import java.io.File
import org.apache.maven.model.Repository
import org.apache.maven.model.RepositoryPolicy
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class MavenArtifactResolverSpec {
    @Test
    fun `xxx`() {
        val repository = Repository().apply {
            id = "Snap"
            releases = RepositoryPolicy().apply { enabled = "true" }
            url = "https://raw.github.com/bitPogo/maven-snapshots/main/snapshots"
        }

        val resolver = ArtifactResolver(
            cacheDir = File(
                "/Users/bitpogo/projects/antibytes/gradle-plugins/antibytes-dependency-helper/build",
            ).toPath(),
            repositories = listOf(repository),
        )
        /*val result = resolver.download(
            "tech.antibytes.gradle-plugins:antibytes-detekt-configuration:150b2e1",
            false
        )

        println(result)*/

        val artifact = resolver.artifactFor("tech.antibytes.gradle-plugins:antibytes-detekt-configuration:150b2e1")
        val resolution = resolver.resolve(artifact)
        val resolvedArtifact = resolution.artifact
        val result = resolver.downloadArtifact(resolution.artifact!!)

        println(resolution.status)

        println(resolvedArtifact?.main?.artifact)
    }
}
