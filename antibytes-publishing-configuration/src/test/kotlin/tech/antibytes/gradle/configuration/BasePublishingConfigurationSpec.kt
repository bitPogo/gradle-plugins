/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import com.appmattus.kotlinfixture.kotlinFixture
import java.io.File
import kotlin.test.assertEquals
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.ClearEnvironmentVariable
import org.junitpioneer.jupiter.SetEnvironmentVariable
import tech.antibytes.gradle.publishing.api.DeveloperConfiguration
import tech.antibytes.gradle.publishing.api.DocumentationConfiguration
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.LicenseConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.MemorySigningConfiguration
import tech.antibytes.gradle.publishing.api.SourceControlConfiguration
import tech.antibytes.gradle.versioning.api.VersioningConfiguration

private class TestConfig(
    project: Project,
    githubRepository: String,
) : BasePublishingConfiguration(project, githubRepository) {
    val _developer: DeveloperConfiguration = this.developer
    val _license: LicenseConfiguration = this.license
    val _sourceControl: SourceControlConfiguration = this.sourceControl
}

class BasePublishingConfigurationSpec {
    private val fixture = kotlinFixture()

    @Test
    @SetEnvironmentVariable.SetEnvironmentVariables(
        SetEnvironmentVariable(key = "PACKAGE_REGISTRY_UPLOAD_USERNAME", value = "PACKAGE_REGISTRY_UPLOAD_USERNAME"),
        SetEnvironmentVariable(key = "PACKAGE_REGISTRY_UPLOAD_TOKEN", value = "PACKAGE_REGISTRY_UPLOAD_TOKEN"),
        SetEnvironmentVariable(key = "MAVEN_KEY", value = "MAVEN_KEY"),
        SetEnvironmentVariable(key = "MAVEN_PASSPHRASE", value = "MAVEN_PASSPHRASE"),
        SetEnvironmentVariable(key = "OSSR_USERNAME", value = "OSSR_USERNAME"),
        SetEnvironmentVariable(key = "OSSR_PASSWORD", value = "OSSR_PASSWORD"),
    )
    fun `It sets the properties provided by the project or System Properties`() {
        // Given
        val project = ProjectBuilder.builder().build()
        project.buildDir = File("/tmp/somewhere")
        val repository: String = fixture()

        // When
        val config = TestConfig(
            project = project,
            githubRepository = repository,
        )

        // Then
        assertEquals(
            actual = config._developer,
            expected = DeveloperConfiguration(
                id = "bitPogo",
                name = "bitPogo",
                url = "https://github.com/bitPogo",
                email = "bitpogo@antibytes.tech",
            ),
        )

        assertEquals(
            actual = config._license,
            expected = LicenseConfiguration(
                name = "Apache License, Version 2.0",
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt",
                distribution = "repo",
            ),
        )

        assertEquals(
            actual = config._sourceControl,
            expected = SourceControlConfiguration(
                url = "git://github.com/bitPogo/$repository.git",
                connection = "scm:git://github.com/bitPogo/$repository.git",
                developerConnection = "scm:git://github.com/bitPogo/$repository.git",
            ),
        )

        assertEquals(
            actual = config.additionalPublishingTasks,
            expected = mapOf(
                "linuxMips" to setOf("linuxMips32", "linuxMipsel32"),
            ),
        )

        assertEquals(
            actual = config.documentation,
            expected = DocumentationConfiguration(
                tasks = setOf("dokkaHtml"),
                outputDir = project.buildDir.resolve("dokka"),
            ),
        )

        assertEquals(
            actual = config.repositories,
            expected = setOf(
                MavenRepositoryConfiguration(
                    name = "MavenCentral",
                    url = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/",
                    username = "OSSR_USERNAME",
                    password = "OSSR_PASSWORD",
                ),
                GitRepositoryConfiguration(
                    name = "Development",
                    gitWorkDirectory = "dev",
                    url = "https://github.com/bitPogo/maven-dev",
                    username = "PACKAGE_REGISTRY_UPLOAD_USERNAME",
                    password = "PACKAGE_REGISTRY_UPLOAD_TOKEN",
                ),
                GitRepositoryConfiguration(
                    name = "Snapshot",
                    gitWorkDirectory = "snapshots",
                    url = "https://github.com/bitPogo/maven-snapshots",
                    username = "PACKAGE_REGISTRY_UPLOAD_USERNAME",
                    password = "PACKAGE_REGISTRY_UPLOAD_TOKEN",
                ),
                GitRepositoryConfiguration(
                    name = "RollingRelease",
                    gitWorkDirectory = "rolling",
                    url = "https://github.com/bitPogo/maven-rolling-releases",
                    username = "PACKAGE_REGISTRY_UPLOAD_USERNAME",
                    password = "PACKAGE_REGISTRY_UPLOAD_TOKEN",
                ),
                GitRepositoryConfiguration(
                    name = "Release",
                    gitWorkDirectory = "releases",
                    url = "https://github.com/bitPogo/maven-releases",
                    username = "PACKAGE_REGISTRY_UPLOAD_USERNAME",
                    password = "PACKAGE_REGISTRY_UPLOAD_TOKEN",
                ),
                MavenRepositoryConfiguration(
                    name = "Local",
                    url = project.uri(project.buildDir),
                ),
            ),
        )

        assertEquals(
            actual = config.versioning,
            expected = VersioningConfiguration(
                featurePrefixes = listOf("feature"),
            ),
        )

        assertEquals(
            actual = config.signing,
            expected = MemorySigningConfiguration(
                key = "MAVEN_KEY",
                password = "MAVEN_PASSPHRASE",
            ),
        )
    }

    @Test
    @ClearEnvironmentVariable.ClearEnvironmentVariables(
        ClearEnvironmentVariable(key = "PACKAGE_REGISTRY_UPLOAD_USERNAME"),
        ClearEnvironmentVariable(key = "PACKAGE_REGISTRY_UPLOAD_TOKEN"),
        ClearEnvironmentVariable(key = "MAVEN_KEY"),
        ClearEnvironmentVariable(key = "MAVEN_PASSPHRASE"),
        ClearEnvironmentVariable(key = "OSSR_USERNAME"),
        ClearEnvironmentVariable(key = "OSSR_PASSWORD"),
    )
    fun `It sets the properties if they are not provided by System Properties`() {
        // Given
        val project = ProjectBuilder.builder().build()
        project.buildDir = File("/tmp/somewhere")
        val repository: String = fixture()

        // When
        val config = TestConfig(
            project = project,
            githubRepository = repository,
        )

        // Then
        assertEquals(
            actual = config._developer,
            expected = DeveloperConfiguration(
                id = "bitPogo",
                name = "bitPogo",
                url = "https://github.com/bitPogo",
                email = "bitpogo@antibytes.tech",
            ),
        )

        assertEquals(
            actual = config._license,
            expected = LicenseConfiguration(
                name = "Apache License, Version 2.0",
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt",
                distribution = "repo",
            ),
        )

        assertEquals(
            actual = config._sourceControl,
            expected = SourceControlConfiguration(
                url = "git://github.com/bitPogo/$repository.git",
                connection = "scm:git://github.com/bitPogo/$repository.git",
                developerConnection = "scm:git://github.com/bitPogo/$repository.git",
            ),
        )

        assertEquals(
            actual = config.documentation,
            expected = DocumentationConfiguration(
                tasks = setOf("dokkaHtml"),
                outputDir = project.buildDir.resolve("dokka"),
            ),
        )

        assertEquals(
            actual = config.additionalPublishingTasks,
            expected = mapOf(
                "linuxMips" to setOf("linuxMips32", "linuxMipsel32"),
            ),
        )

        assertEquals(
            actual = config.repositories,
            expected = setOf(
                MavenRepositoryConfiguration(
                    name = "MavenCentral",
                    url = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/",
                    username = "",
                    password = "",
                ),
                GitRepositoryConfiguration(
                    name = "Development",
                    gitWorkDirectory = "dev",
                    url = "https://github.com/bitPogo/maven-dev",
                    username = "",
                    password = "",
                ),
                GitRepositoryConfiguration(
                    name = "Snapshot",
                    gitWorkDirectory = "snapshots",
                    url = "https://github.com/bitPogo/maven-snapshots",
                    username = "",
                    password = "",
                ),
                GitRepositoryConfiguration(
                    name = "RollingRelease",
                    gitWorkDirectory = "rolling",
                    url = "https://github.com/bitPogo/maven-rolling-releases",
                    username = "",
                    password = "",
                ),
                GitRepositoryConfiguration(
                    name = "Release",
                    gitWorkDirectory = "releases",
                    url = "https://github.com/bitPogo/maven-releases",
                    username = "",
                    password = "",
                ),
                MavenRepositoryConfiguration(
                    name = "Local",
                    url = project.uri(project.buildDir),
                ),
            ),
        )

        assertEquals(
            actual = config.versioning,
            expected = VersioningConfiguration(
                featurePrefixes = listOf("feature"),
            ),
        )

        assertEquals(
            actual = config.signing,
            expected = MemorySigningConfiguration(
                key = "",
                password = "",
            ),
        )
    }
}
