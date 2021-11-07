/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AntiBytesPublishingPluginExtensionSpec {
    @Test
    fun `It fulfils PublishingConfiguration`() {
        val extension: Any = createExtension<AntiBytesPublishingPluginExtension>()

        assertTrue(extension is PublishingContract.PublishingPluginConfiguration)
    }

    @Test
    fun `It has a default VersioningConfiguration`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertTrue(extension.versioning.isPresent)
    }

    @Test
    fun `It has false as default DryRun`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertFalse(extension.dryRun.get())
    }

    @Test
    fun `It has an empty list as default DryRun`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertEquals(
            actual = extension.registryConfiguration.get(),
            expected = emptySet()
        )
    }

    @Test
    fun `It has no default PackageConfigurations`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertFalse(extension.packageConfiguration.isPresent)
    }

    @Test
    fun `It has an empty set as default ExcludeProjects`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertEquals(
            actual = extension.excludeProjects.get(),
            expected = emptySet<String>()
        )
    }
}
