/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import org.junit.Test
import tech.antibytes.gradle.publishing.api.VersioningConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
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

        assertEquals(
            actual = extension.versioning.releasePattern.pattern,
            expected = VersioningConfiguration().releasePattern.pattern
        )

        assertEquals(
            actual = extension.versioning.normalization,
            expected = VersioningConfiguration().normalization
        )

        assertEquals(
            actual = extension.versioning.versionPrefix,
            expected = VersioningConfiguration().versionPrefix
        )

        assertEquals(
            actual = extension.versioning.featurePattern.pattern,
            expected = VersioningConfiguration().featurePattern.pattern
        )

        assertEquals(
            actual = extension.versioning.issuePattern,
            expected = VersioningConfiguration().issuePattern
        )

        assertEquals(
            actual = extension.versioning.dependencyBotPattern.pattern,
            expected = VersioningConfiguration().dependencyBotPattern.pattern
        )
    }

    @Test
    fun `It has false as default DryRun`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertFalse(extension.dryRun)
    }

    @Test
    fun `It has an empty list as default RegistryConfiguration`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertEquals(
            actual = extension.registryConfiguration,
            expected = emptySet()
        )
    }

    @Test
    fun `It has no default PackageConfigurations`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertNull(extension.packageConfiguration)
    }

    @Test
    fun `It has an empty set as default ExcludeProjects`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertEquals(
            actual = extension.excludeProjects,
            expected = emptySet()
        )
    }

    @Test
    fun `It has true as default Standalone`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertTrue(extension.standalone)
    }
}
