/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.versioning.api.VersioningConfiguration

class AntibytesPublishingPluginExtensionSpec {
    @Test
    fun `It fulfils PublishingConfiguration`() {
        val extension: Any = createExtension<AntibytesPublishingPluginExtension>()

        assertTrue(extension is PublishingContract.PublishingPluginExtension)
    }

    @Test
    fun `It has a default VersioningConfiguration`() {
        val extension: AntibytesPublishingPluginExtension = createExtension()

        assertEquals(
            actual = extension.versioning.get().releasePrefixes,
            expected = VersioningConfiguration().releasePrefixes,
        )

        assertEquals(
            actual = extension.versioning.get().normalization,
            expected = VersioningConfiguration().normalization,
        )

        assertEquals(
            actual = extension.versioning.get().versionPrefix,
            expected = VersioningConfiguration().versionPrefix,
        )

        assertEquals(
            actual = extension.versioning.get().featurePrefixes,
            expected = VersioningConfiguration().featurePrefixes,
        )

        assertEquals(
            actual = extension.versioning.get().issuePattern,
            expected = VersioningConfiguration().issuePattern,
        )

        assertEquals(
            actual = extension.versioning.get().dependencyBotPrefixes,
            expected = VersioningConfiguration().dependencyBotPrefixes,
        )

        assertEquals(
            actual = extension.versioning.get().useGitHashFeatureSuffix,
            expected = VersioningConfiguration().useGitHashFeatureSuffix,
        )

        assertEquals(
            actual = extension.versioning.get().useGitHashSnapshotSuffix,
            expected = VersioningConfiguration().useGitHashSnapshotSuffix,
        )
    }

    @Test
    fun `It has false as default DryRun`() {
        val extension: AntibytesPublishingPluginExtension = createExtension()

        assertFalse(extension.dryRun.get())
    }

    @Test
    fun `It has an empty list as default RegistryConfiguration`() {
        val extension: AntibytesPublishingPluginExtension = createExtension()

        assertEquals(
            actual = extension.repositories.get(),
            expected = emptySet(),
        )
    }

    @Test
    fun `It has no default PackageConfigurations`() {
        val extension: AntibytesPublishingPluginExtension = createExtension()

        assertNull(extension.packaging.orNull)
    }

    @Test
    fun `It has no default Documentation`() {
        val extension: AntibytesPublishingPluginExtension = createExtension()

        assertNull(extension.documentation.orNull)
    }

    @Test
    fun `It has an empty set as default ExcludeProjects`() {
        val extension: AntibytesPublishingPluginExtension = createExtension()

        assertEquals(
            actual = extension.excludeProjects.get(),
            expected = emptySet(),
        )
    }

    @Test
    fun `It has true as default signing configuration`() {
        val extension: AntibytesPublishingPluginExtension = createExtension()

        assertNull(extension.signing.orNull)
    }

    @Test
    fun `It has true as default Standalone`() {
        val extension: AntibytesPublishingPluginExtension = createExtension()

        assertFalse(extension.standalone.get())
    }
}
