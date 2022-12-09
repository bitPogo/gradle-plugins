/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.component

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.component.AdhocComponentWithVariants
import org.gradle.api.component.ConfigurationVariantDetails
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.kotlin.dsl.named
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.test.invokeGradleAction

class AntiBytesCustomComponentSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesCustomComponent(mockk())

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called it registers a new Component`() {
        // Given
        val project: Project = mockk()
        val componentFactory: SoftwareComponentFactory = mockk()
        val extension = createExtension<AntibytesCustomComponentExtension>()
        val component: AdhocComponentWithVariants = mockk()

        every { componentFactory.adhoc(any()) } returns component
        every {
            project.extensions.create(any(), AntibytesCustomComponentExtension::class.java)
        } returns extension
        every { project.components.add(any()) } returns fixture()
        every { project.afterEvaluate(any<Action<Any>>()) } returns mockk()

        // When
        AntiBytesCustomComponent(componentFactory).apply(project)

        // Then
        verify(exactly = 1) { componentFactory.adhoc("antibytesCustomComponent") }
        verify(exactly = 1) { project.components.add(component) }
    }

    @Test
    fun `Given apply is called it configures the component`() {
        // Given
        val project: Project = mockk()
        val componentFactory: SoftwareComponentFactory = mockk()
        val extension = createExtension<AntibytesCustomComponentExtension>()
        val component: AdhocComponentWithVariants = mockk()
        val componentConfiguration: Configuration = mockk(relaxed = true)
        val variantDetails: ConfigurationVariantDetails = mockk(relaxed = true)
        val capturedArtifact = slot<PublishArtifact>()

        val scope: String = fixture()
        val artifacts: Set<CustomComponentApiContract.Artifact> = setOf(
            CustomComponentApiContract.Artifact(
                buildDependencies = mockk(),
                name = fixture(),
                type = fixture(),
                typeExtension = fixture(),
                componentHandle = mockk(),
                classifier = fixture(),
                date = mockk(),
            ),
        )
        val attributes: Map<Any, Any> = mapOf(
            LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE to fixture(),
        )

        extension.scope.set(scope)
        extension.customArtifacts.set(artifacts)
        extension.attributes.set(attributes)

        every { componentFactory.adhoc(any()) } returns component
        every {
            project.extensions.create(any(), AntibytesCustomComponentExtension::class.java)
        } returns extension
        every { project.components.add(any()) } returns fixture()

        every { project.configurations.create(any()) } returns componentConfiguration
        every { project.configurations.named(any()) } returns mockk {
            every { get() } returns componentConfiguration
        }
        every { componentConfiguration.artifacts.add(capture(capturedArtifact)) } returns fixture()

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            project,
        )
        invokeGradleAction(
            { probe -> component.addVariantsFromConfiguration(any(), probe) },
            variantDetails,
        )

        // When
        AntiBytesCustomComponent(componentFactory).apply(project)

        // Then
        verify(exactly = 1) { componentConfiguration.isCanBeResolved = false }
        verify(exactly = 1) { componentConfiguration.isCanBeConsumed = true }
        verify(exactly = 1) { variantDetails.mapToMavenScope(scope) }
        attributes.forEach { (attribute, value) ->
            verify(exactly = 1) {
                @Suppress("UNCHECKED_CAST")
                componentConfiguration.attributes.attribute(attribute as Attribute<Any>, value)
            }
        }

        assertSame(
            actual = capturedArtifact.captured.buildDependencies,
            expected = artifacts.first().buildDependencies,
        )
        assertEquals(
            actual = capturedArtifact.captured.name,
            expected = artifacts.first().name,
        )
        assertEquals(
            actual = capturedArtifact.captured.type,
            expected = artifacts.first().type,
        )
        assertEquals(
            actual = capturedArtifact.captured.extension,
            expected = artifacts.first().typeExtension,
        )
        assertSame(
            actual = capturedArtifact.captured.file,
            expected = artifacts.first().componentHandle,
        )
        assertEquals(
            actual = capturedArtifact.captured.classifier,
            expected = artifacts.first().classifier,
        )
        assertSame(
            actual = capturedArtifact.captured.date,
            expected = artifacts.first().date,
        )
    }
}
