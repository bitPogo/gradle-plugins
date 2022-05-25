/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.signing

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.api.CompleteMemorySigningConfiguration
import tech.antibytes.gradle.publishing.api.MemorySigningConfiguration
import tech.antibytes.gradle.test.invokeGradleAction
import kotlin.test.assertTrue

class MemorySigningSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils MemorySigning`() {
        val publisher: Any = MemorySigning

        assertTrue(publisher is SigningContract.MemorySigning)
    }

    @Test
    fun `Given configure signing is called with basic memory configuration, it calls useInMemoryPgpKeys`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()

        val signingExtension: SigningExtension = mockk(relaxed = true)
        val publishingExtension: PublishingExtension = mockk()

        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        every { extensions.getByType<PublishingExtension>() } returns publishingExtension

        every { signingExtension.sign(any<DomainObjectCollection<Publication>>()) } returns emptyList()
        every { signingExtension.useInMemoryPgpKeys(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> extensions.configure(SigningExtension::class.java, probe) },
            signingExtension
        )

        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )

        val signingConfiguration = MemorySigningConfiguration(
            key = fixture(),
            password = fixture(),
        )

        // When
        MemorySigning.configure(project, signingConfiguration)

        // Then
        verify(exactly = 1) {
            signingExtension.useInMemoryPgpKeys(signingConfiguration.key, signingConfiguration.password)
        }
    }

    @Test
    fun `Given configure signing is called with complete memory configuration, it calls useInMemoryPgpKeys`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()

        val signingExtension: SigningExtension = mockk(relaxed = true)
        val publishingExtension: PublishingExtension = mockk()

        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        every { extensions.getByType<PublishingExtension>() } returns publishingExtension

        every { signingExtension.sign(any<DomainObjectCollection<Publication>>()) } returns emptyList()
        every { signingExtension.useInMemoryPgpKeys(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> extensions.configure(SigningExtension::class.java, probe) },
            signingExtension
        )

        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )

        val signingConfiguration = CompleteMemorySigningConfiguration(
            key = fixture(),
            password = fixture(),
            keyId = fixture(),
        )

        // When
        MemorySigning.configure(project, signingConfiguration)

        // Then
        verify(exactly = 1) {
            signingExtension.useInMemoryPgpKeys(
                signingConfiguration.keyId,
                signingConfiguration.key,
                signingConfiguration.password,
            )
        }
    }

    @Test
    fun `Given configure signing is called calls sign for publications`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()

        val signingExtension: SigningExtension = mockk(relaxed = true)
        val publishingExtension: PublishingExtension = mockk()

        val publicationContainer: PublicationContainer = mockk()
        val publication: MavenPublication = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.publications } returns publicationContainer

        every { extensions.getByType<PublishingExtension>() } returns publishingExtension

        every { signingExtension.sign(any<DomainObjectCollection<Publication>>()) } returns emptyList()
        every { signingExtension.useInMemoryPgpKeys(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> extensions.configure(SigningExtension::class.java, probe) },
            signingExtension
        )

        invokeGradleAction(
            { probe -> publishingExtension.publications(probe) },
            publicationContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> publicationContainer.withType(MavenPublication::class.java, probe) },
            publication,
            mockk()
        )

        val signingConfiguration = CompleteMemorySigningConfiguration(
            key = fixture(),
            password = fixture(),
            keyId = fixture(),
        )

        // When
        MemorySigning.configure(project, signingConfiguration)

        // Then
        verify(exactly = 1) {
            signingExtension.sign(publicationContainer)
        }
    }
}
