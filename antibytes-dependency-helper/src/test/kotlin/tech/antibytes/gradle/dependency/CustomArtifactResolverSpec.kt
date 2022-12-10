/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import com.appmattus.kotlinfixture.kotlinFixture
import com.squareup.tools.maven.resolution.ArtifactResolver
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import java.io.File
import java.net.URI
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.apache.maven.model.Repository
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class CustomArtifactResolverSpec {
    private val fixture = kotlinFixture()

    @TempDir
    lateinit var projectDir: File

    @Test
    fun `It fulfils CustomArtifactResolverFactory`() {
        val factory: Any = CustomArtifactResolver

        assertTrue(factory is DependencyContract.CustomArtifactResolverFactory)
    }

    @Test
    fun `Given getInstance is called it fails a CustomArtifactResolver if it cannot make a Cache`() {
        // Given
        val project: Project = mockk()

        every { project.rootProject } returns project
        every { project.projectDir } returns File("/somewhere/in/the/root/null")
        every { project.repositories.asMap } returns sortedMapOf<String, ArtifactRepository>()

        // Then
        val error = assertFailsWith<FileSystemException> {
            // When
            CustomArtifactResolver.getInstance(project)
        }

        assertEquals(
            actual = error.message,
            expected = "/somewhere/in/the/root/null/.gradle/artifactCache: Cannot create Cache.",
        )
    }

    @Test
    fun `Given getInstance is called it returns a CustomArtifactResolver while using an existing Cache`() {
        // Given
        val project: Project = mockk()

        File(projectDir, ".gradle/artifactCache").mkdirs()
        every { project.rootProject } returns project
        every { project.projectDir } returns projectDir
        every { project.repositories.asMap } returns sortedMapOf<String, ArtifactRepository>()

        // When
        val resolver: Any = CustomArtifactResolver.getInstance(project)

        // Then
        assertTrue(resolver is DependencyContract.CustomArtifactResolver)
    }

    @Test
    fun `Given getInstance is called it returns a CustomArtifactResolver while creating the Cache`() {
        // Given
        val project: Project = mockk()

        every { project.rootProject } returns project
        every { project.projectDir } returns projectDir
        every { project.repositories.asMap } returns sortedMapOf()

        // When
        val resolver: Any = CustomArtifactResolver.getInstance(project)

        // Then
        var hasCacheDir = false
        projectDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith(".gradle/artifactCache")) {
                hasCacheDir = true
            }
        }
        assertTrue(resolver is DependencyContract.CustomArtifactResolver)
        assertTrue(hasCacheDir)
    }

    @Test
    fun `Given getInstance is called it returns a CustomArtifactResolver while collecting the repositories and cache`() {
        mockkObject(SquareArtifactResolverFactory)
        // Given
        val project: Project = mockk()
        val uri1 = "https://plugins.gradle.org/m2"
        val uri2 = "https://repo.maven.apache.org/maven2/"
        val givenRepositories = sortedMapOf<String, ArtifactRepository>(
            fixture<String>() to mockk<MavenArtifactRepository> {
                every { url } returns URI(uri1)
            },
            fixture<String>() to mockk<MavenArtifactRepository> {
                every { url } returns URI(uri2)
            },
        )

        val path = slot<Path>()
        val repositories = slot<List<Repository>>()

        every { project.rootProject } returns project
        every { project.projectDir } returns projectDir
        every { project.repositories.asMap } returns givenRepositories
        every { SquareArtifactResolverFactory.getInstance(capture(path), capture(repositories)) } returns mockk()

        // When
        val resolver: Any = CustomArtifactResolver.getInstance(project)

        // Then
        assertTrue(resolver is DependencyContract.CustomArtifactResolver)
        assertEquals(
            actual = path.captured.toAbsolutePath().toString(),
            expected = File(projectDir, ".gradle/artifactCache").absolutePath,
        )
        assertEquals(
            actual = repositories.captured[0].id,
            expected = givenRepositories.toList().first().first,
        )
        assertEquals(
            actual = repositories.captured[0].url,
            expected = (givenRepositories.toList().first().second as MavenArtifactRepository).url.toString(),
        )
        assertEquals(
            actual = repositories.captured[1].id,
            expected = givenRepositories.toList().last().first,
        )
        assertEquals(
            actual = repositories.captured[1].url,
            expected = (givenRepositories.toList().last().second as MavenArtifactRepository).url.toString(),
        )

        unmockkObject(SquareArtifactResolverFactory)
    }

    @Test
    fun `Given resolveArtifact it resolves the artifact from the provided Coordinates`() {
        mockkObject(SquareArtifactResolverFactory)
        // Given
        val project: Project = mockk()
        val resolver: ArtifactResolver = mockk()
        val coordinates: String = fixture()
        val artifactHandle: File = mockk()

        // When
        every { SquareArtifactResolverFactory.getInstance(any(), any()) } returns resolver
        every { project.rootProject } returns project
        every { project.projectDir } returns projectDir
        every { project.repositories.asMap } returns sortedMapOf()

        every { resolver.download(any(), any()) } returns mockk {
            every { component1() } returns mockk()
            every { component2() } returns mockk {
                every { toFile() } returns artifactHandle
            }
        }

        // When
        val handle = CustomArtifactResolver.getInstance(project).resolveArtifact(coordinates)

        // Then
        assertSame(
            actual = handle,
            expected = artifactHandle,
        )
        verify(exactly = 1) { resolver.download(coordinates, false) }

        unmockkObject(SquareArtifactResolverFactory)
    }

    @Test
    fun `Given customArtifact is called it returns a FileProvider`() {
        mockkObject(CustomArtifactResolver)
        // Given
        val project: Project = ProjectBuilder.builder().build()
        val resolver: CustomArtifactResolver = mockk()
        val coordinates: String = fixture()
        val artifactHandle: File = mockk()

        every { CustomArtifactResolver.getInstance(any()) } returns resolver
        every { resolver.resolveArtifact(any()) } returns artifactHandle

        // When
        val artifact = project.customArtifact(coordinates).get()

        // Then
        assertSame(
            actual = artifact,
            expected = artifactHandle,
        )
        verify(exactly = 1) { CustomArtifactResolver.getInstance(project) }
        verify(exactly = 1) { resolver.resolveArtifact(coordinates) }

        unmockkObject(CustomArtifactResolver)
    }
}
