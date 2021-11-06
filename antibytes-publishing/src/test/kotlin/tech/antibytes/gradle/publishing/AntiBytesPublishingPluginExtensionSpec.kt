/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AntiBytesPublishingPluginExtensionSpec {
    @Test
    fun `It fulfils PublishingConfiguration`() {
        val project = ProjectBuilder.builder().build()
        val extension: Any = project.extensions.create(
            "publishing",
            AntiBytesPublishingPluginExtension::class.java
        )

        assertTrue(extension is PublishingContract.PublishingPluginConfiguration)
    }

    @Test
    fun `It has a default VersioningConfiguration`() {
        val project = ProjectBuilder.builder().build()
        val extension = project.extensions.create(
            "publishing",
            AntiBytesPublishingPluginExtension::class.java
        )

        assertTrue(extension.versioning.isPresent)
    }

    @Test
    fun `It has false as default DryRun`() {
        val project = ProjectBuilder.builder().build()
        val extension = project.extensions.create(
            "publishing",
            AntiBytesPublishingPluginExtension::class.java
        )

        assertFalse(extension.dryRun.get())
    }

    @Test
    fun `It has an empty list as default DryRun`() {
        val project = ProjectBuilder.builder().build()
        val extension = project.extensions.create(
            "publishing",
            AntiBytesPublishingPluginExtension::class.java
        )

        assertEquals(
            actual = extension.registryConfiguration.get(),
            expected = emptySet()
        )
    }

    @Test
    fun `It has no default PackageConfigurations`() {
        val project = ProjectBuilder.builder().build()
        val extension = project.extensions.create(
            "publishing",
            AntiBytesPublishingPluginExtension::class.java
        )

        assertFalse(extension.packageConfiguration.isPresent)
    }

    @Test
    fun `It has an empty set as default ExcludeProjects`() {
        val project = ProjectBuilder.builder().build()
        val extension = project.extensions.create(
            "publishing",
            AntiBytesPublishingPluginExtension::class.java
        )

        assertEquals(
            actual = extension.excludeProjects.get(),
            expected = emptySet<String>()
        )
    }
}
