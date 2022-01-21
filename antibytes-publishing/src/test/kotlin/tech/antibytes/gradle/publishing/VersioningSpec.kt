/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import com.appmattus.kotlinfixture.kotlinFixture
import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import io.mockk.every
import io.mockk.mockk
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.invoke
import org.junit.Test
import tech.antibytes.gradle.publishing.api.VersioningConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class VersioningSpec {
    private val fixture = kotlinFixture()
    private val versionTestConfiguration = VersioningConfiguration(
        releasePrefixes = listOf("xxx"),
        featurePrefixes = listOf("xxx"),
        dependencyBotPrefixes = listOf("xxx"),
        issuePattern = null,
        versionPrefix = "xxx",
        normalization = emptySet()
    )

    @Test
    fun `It fulfils Versioning`() {
        val versioning: Any = Versioning

        assertTrue(versioning is PublishingContract.Versioning)
    }

    @Test
    fun `Given versionName is called, it fails if no pattern matches`() {
        val branchName = "illegal"

        val configuration = versionTestConfiguration.copy()

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { versionDetails.branchName } returns branchName

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        // Then
        val error = assertFailsWith<PublishingError.VersioningError> {
            // When
            Versioning.versionName(
                project,
                configuration
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Ill named branch name ($branchName)! Please adjust it to match the project settings."
        )
    }

    @Test
    fun `Given versionName is called, it marks the version as SNAPSHOT if the branch is dirty`() {
        val branchName = "main"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected.dirty"

        val configuration = versionTestConfiguration.copy(
            releasePrefixes = listOf("main", "release"),
            versionPrefix = versionPrefix
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns false
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it marks the version as SNAPSHOT if the branch is has a commit distance greater then 0`() {
        val branchName = "main"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected-19-g24a885d.dirty"
        val distance = 19

        val configuration = versionTestConfiguration.copy(
            releasePrefixes = listOf("main", "release"),
            versionPrefix = versionPrefix
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns distance

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders the release version`() {
        val branchName = "main"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = versionTestConfiguration.copy(
            releasePrefixes = listOf("main", "release"),
            versionPrefix = versionPrefix
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected
        )
    }

    @Test
    fun `Given versionName is called, it renders a dependencyBot branch`() {
        val branchAction = "test"
        val branchName = "dependabot/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = versionTestConfiguration.copy(
            dependencyBotPrefixes = listOf("dependabot"),
            versionPrefix = versionPrefix
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-bump-$branchAction-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders and normalizes a dependencyBot branch`() {
        val branchAction = "test_abc?dfg\$asd"
        val branchName = "dependabot/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = versionTestConfiguration.copy(
            dependencyBotPrefixes = listOf("dependabot"),
            versionPrefix = versionPrefix,
            normalization = setOf("_", "?", "\$")
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-bump-${
            branchAction
                .replace("_", "-")
                .replace("?", "-")
                .replace("\$", "-")
            }-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders a feature branch`() {
        val branchAction = "test"
        val branchName = "feature/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = versionTestConfiguration.copy(
            featurePrefixes = listOf("feature", "newStuff"),
            versionPrefix = versionPrefix
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-$branchAction-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders and normalizes a feature branch`() {
        val branchAction = "test_abc?dfg\$asd"
        val branchName = "newStuff/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = versionTestConfiguration.copy(
            featurePrefixes = listOf("feature", "newStuff"),
            versionPrefix = versionPrefix,
            normalization = setOf("_", "?", "\$")
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-${
            branchAction
                .replace("_", "-")
                .replace("?", "-")
                .replace("\$", "-")
            }-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders a feature branch with suffix, if the useGitHashFeatureSuffix is true`() {
        val branchAction = "test"
        val branchName = "feature/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"
        val hash: String = fixture()

        val configuration = versionTestConfiguration.copy(
            featurePrefixes = listOf("feature", "newStuff"),
            versionPrefix = versionPrefix,
            useGitHashFeatureSuffix = true
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0
        every { versionDetails.gitHash } returns hash

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-$branchAction-$hash-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders a feature branch with a issue number`() {
        val branchAction = "test"
        val branchName = "feature/issue-123/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = versionTestConfiguration.copy(
            featurePrefixes = listOf("feature", "newStuff"),
            issuePattern = "issue-[0-9]+/(.*)".toRegex(),
            versionPrefix = versionPrefix,
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-$branchAction-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders a feature branch with a issue number and suffix, if the useGitHashFeatureSuffix is true `() {
        val branchAction = "test"
        val branchName = "newStuff/issue-123/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"
        val hash: String = fixture()

        val configuration = versionTestConfiguration.copy(
            featurePrefixes = listOf("feature", "newStuff"),
            issuePattern = "issue-[0-9]+/(.*)".toRegex(),
            versionPrefix = versionPrefix,
            useGitHashFeatureSuffix = true
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.gitHash } returns hash
        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-$branchAction-$hash-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders a feature branch and ignores the issue number if the pattern does not matches`() {
        val branchAction = "test"
        val branchName = "newStuff/sue-123/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = versionTestConfiguration.copy(
            featurePrefixes = listOf("feature", "newStuff"),
            issuePattern = "issue-[0-9]+/(.*)".toRegex(),
            versionPrefix = versionPrefix,
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-${branchName.substringAfter("newStuff/")}-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders and normalizes a feature branch with a issue number`() {
        val branchAction = "test_abc?dfg\$asd"
        val branchName = "feature/issue-123/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = versionTestConfiguration.copy(
            featurePrefixes = listOf("feature", "newStuff"),
            issuePattern = "issue-[0-9]+/(.*)".toRegex(),
            versionPrefix = versionPrefix,
            normalization = setOf("_", "?", "\$")
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionName(
            project,
            configuration
        )

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-${
            branchAction
                .replace("_", "-")
                .replace("?", "-")
                .replace("\$", "-")
            }-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionInfo is called, it returns a VersionInfo, which contains VersionDetails and a VersionName`() {
        // Given
        val branchName = "main"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = versionTestConfiguration.copy(
            releasePrefixes = listOf("main", "release"),
            versionPrefix = versionPrefix
        )

        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val extraProperties: ExtraPropertiesExtension = mockk()

        val versionDetails: VersionDetails = mockk()

        val details: Closure<VersionDetails> = ClosureHelper.createClosure(versionDetails)

        every { extraProperties.has("versionDetails") } returns true
        every { extraProperties.get("versionDetails") } returns details

        every { extensions.extraProperties } returns extraProperties

        every { project.extensions } returns extensions

        every { versionDetails.branchName } returns branchName
        every { versionDetails.isCleanTag } returns true
        every { versionDetails.version } returns version
        every { versionDetails.commitDistance } returns 0

        // When
        val result = Versioning.versionInfo(
            project,
            configuration
        )

        // Then
        assertSame(
            actual = result.details,
            expected = details()
        )

        assertEquals(
            actual = result.name,
            expected = expected
        )
    }
}
