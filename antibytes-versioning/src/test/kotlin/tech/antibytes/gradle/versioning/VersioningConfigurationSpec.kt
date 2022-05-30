/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning

import org.junit.jupiter.api.Test
import tech.antibytes.gradle.versioning.VersioningContract
import tech.antibytes.gradle.versioning.api.VersioningConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VersioningConfigurationSpec {
    @Test
    fun `It fulfils VersioningConfiguration`() {
        val actual: Any = VersioningConfiguration()

        assertTrue(actual is VersioningContract.VersioningConfiguration)
    }

    @Test
    fun `It has a default VersioningConfiguration`() {
        assertEquals(
            actual = VersioningConfiguration().releasePrefixes,
            expected = listOf("main", "release"),
        )

        assertEquals(
            actual = VersioningConfiguration().normalization,
            expected = emptySet(),
        )

        assertEquals(
            actual = VersioningConfiguration().versionPrefix,
            expected = "v",
        )

        assertEquals(
            actual = VersioningConfiguration().featurePrefixes,
            expected = listOf("feature"),
        )

        assertEquals(
            actual = VersioningConfiguration().issuePattern,
            expected = null,
        )

        assertEquals(
            actual = VersioningConfiguration().dependencyBotPrefixes,
            expected = listOf("dependabot"),
        )

        assertEquals(
            actual = VersioningConfiguration().useGitHashFeatureSuffix,
            expected = false,
        )

        assertEquals(
            actual = VersioningConfiguration().useGitHashSnapshotSuffix,
            expected = false,
        )
    }
}
