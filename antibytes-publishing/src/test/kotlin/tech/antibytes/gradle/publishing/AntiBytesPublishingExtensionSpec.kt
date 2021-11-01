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

class AntiBytesPublishingExtensionSpec {
    @Test
    fun `It fulfils PublishingConfiguration`() {
        val project = ProjectBuilder.builder().build()
        val extension: Any = project.extensions.create(
            "publishing",
            AntiBytesPublishingExtension::class.java
        )

        assertTrue(extension is PublishingContract.PublishingConfiguration)
    }

    @Test
    fun `It has a default FeaturePattern`() {
        val project = ProjectBuilder.builder().build()
        val extension = project.extensions.create(
            "publishing",
            AntiBytesPublishingExtension::class.java
        )

        assertEquals(
            actual = extension.featurePattern.get().pattern,
            expected = "feature/(.*)"
        )
    }

    @Test
    fun `It has a default ReleasePattern`() {
        val project = ProjectBuilder.builder().build()
        val extension = project.extensions.create(
            "publishing",
            AntiBytesPublishingExtension::class.java
        )

        assertEquals(
            actual = extension.releasePattern.get().pattern,
            expected = "main|release/.*"
        )
    }

    @Test
    fun `It has no default IssuePattern`() {
        val project = ProjectBuilder.builder().build()
        val extension = project.extensions.create(
            "publishing",
            AntiBytesPublishingExtension::class.java
        )

        assertFalse(extension.issuePattern.isPresent)
    }
}
