/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.versioning.api.VersioningConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AntiBytesPublishingPluginExtensionSpec {
    @Test
    fun `It fulfils PublishingConfiguration`() {
        val extension: Any = createExtension<AntiBytesPublishingPluginExtension>()

        assertTrue(extension is PublishingContract.PublishingPluginExtension)
    }

    @Test
    fun `It has a default VersioningConfiguration`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertEquals(
            actual = extension.versioning.releasePrefixes,
            expected = VersioningConfiguration().releasePrefixes
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
            actual = extension.versioning.featurePrefixes,
            expected = VersioningConfiguration().featurePrefixes
        )

        assertEquals(
            actual = extension.versioning.issuePattern,
            expected = VersioningConfiguration().issuePattern
        )

        assertEquals(
            actual = extension.versioning.dependencyBotPrefixes,
            expected = VersioningConfiguration().dependencyBotPrefixes
        )

        assertEquals(
            actual = extension.versioning.useGitHashFeatureSuffix,
            expected = VersioningConfiguration().useGitHashFeatureSuffix
        )

        assertEquals(
            actual = extension.versioning.useGitHashSnapshotSuffix,
            expected = VersioningConfiguration().useGitHashSnapshotSuffix
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
            actual = extension.repositoryConfiguration,
            expected = emptySet()
        )
    }

    @Test
    fun `It has no default PackageConfigurations`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertNull(extension.packageConfiguration)
    }

    @Test
    fun `It has no default Documentation`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertNull(extension.documentation)
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
    fun `It has true as default signing configuration`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertNull(extension.signingConfiguration)
    }

    @Test
    fun `It has true as default Standalone`() {
        val extension: AntiBytesPublishingPluginExtension = createExtension()

        assertFalse(extension.standalone)
    }
}
