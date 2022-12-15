/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.versioning.api.VersioningConfiguration

class AntiBytesVersioningPluginExtensionSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Extension`() {
        val extension: Any = createExtension<AntiBytesVersioningPluginExtension>()

        assertTrue(extension is VersioningInternalContract.Extension)
    }

    @Test
    fun `It has no default Configuration`() {
        assertNull(createExtension<AntiBytesVersioningPluginExtension>().configuration)
    }

    @Test
    fun `Given a configuration is set to null it set the version to unspecified`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val extension = createExtension<AntiBytesVersioningPluginExtension>(project)

        // When
        extension.configuration = null

        // Then
        assertNull(extension.configuration)
        verify(exactly = 1) { project.version = "unspecified" }
    }

    @Test
    fun `Given apply is called it sets a version if a configuration is given`() {
        mockkObject(Versioning)
        // Given
        val project: Project = mockk(relaxed = true)
        val versioning: Versioning = mockk()
        val extension = createExtension<AntiBytesVersioningPluginExtension>(project)
        val configuration = VersioningConfiguration()
        val version: String = fixture()

        every { project.extensions.create(any(), AntiBytesVersioningPluginExtension::class.java) } returns extension
        every { Versioning.getInstance(any(), any()) } returns versioning
        every { versioning.versionName() } returns version

        // When
        extension.configuration = configuration

        // Then
        assertSame(
            actual = extension.configuration,
            expected = configuration,
        )
        verify(exactly = 1) { project.version = version }

        unmockkObject(Versioning)
    }
}
