/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import io.mockk.every
import io.mockk.mockk
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.invoke
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class VersioningSpec {
    @Test
    fun `It fulfils Versioning`() {
        val versioning: Any = Versioning(
            mockk(relaxed = true),
            mockk()
        )

        assertTrue(versioning is PublishingContract.Versioning)
    }

    @Test
    fun `Given versionName is called, it fails if no pattern matches`() {
        val branchName = "illegal"

        val configuration = VersionConfiguration()

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

        val versioning = Versioning(
            project,
            configuration
        )

        // Then
        val error = assertFailsWith<PublishingError.VersioningError> {
            // When
            versioning.versionName()
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

        val configuration = VersionConfiguration(
            releasePattern = "main|release/.*".toRegex(),
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionName()

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

        val configuration = VersionConfiguration(
            releasePattern = "main|release/.*".toRegex(),
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionName()

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

        val configuration = VersionConfiguration(
            releasePattern = "main|release/.*".toRegex(),
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionName()

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

        val configuration = VersionConfiguration(
            dependencyBotPattern = "dependabot/(.*)".toRegex(),
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionName()

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

        val configuration = VersionConfiguration(
            dependencyBotPattern = "dependabot/(.*)".toRegex(),
            versionPrefix = versionPrefix,
            normalization = listOf("_", "?", "\$")
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionName()

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

        val configuration = VersionConfiguration(
            featurePattern = "feature/(.*)".toRegex(),
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionName()

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-$branchAction-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders and normalizes a feature branch`() {
        val branchAction = "test_abc?dfg\$asd"
        val branchName = "feature/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = VersionConfiguration(
            featurePattern = "feature/(.*)".toRegex(),
            versionPrefix = versionPrefix,
            normalization = listOf("_", "?", "\$")
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionName()

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
    fun `Given versionName is called, it renders a feature branch with a issue number`() {
        val branchAction = "test"
        val branchName = "feature/issue-123/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = VersionConfiguration(
            featurePattern = "feature/(.*)".toRegex(),
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionName()

        // Then
        assertEquals(
            actual = result,
            expected = "$expected-$branchAction-SNAPSHOT"
        )
    }

    @Test
    fun `Given versionName is called, it renders and normalizes a feature branch with a issue number`() {
        val branchAction = "test_abc?dfg\$asd"
        val branchName = "feature/issue-123/$branchAction"
        val expected = "1.15.1"
        val versionPrefix = "v"
        val version = "$versionPrefix$expected"

        val configuration = VersionConfiguration(
            featurePattern = "feature/(.*)".toRegex(),
            issuePattern = "issue-[0-9]+/(.*)".toRegex(),
            versionPrefix = versionPrefix,
            normalization = listOf("_", "?", "\$")
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionName()

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

        val configuration = VersionConfiguration(
            releasePattern = "main|release/.*".toRegex(),
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

        val versioning = Versioning(
            project,
            configuration
        )

        // When
        val result = versioning.versionInfo().details

        // Then
        assertSame(
            actual = result,
            expected = details()
        )
    }
}

private class VersionConfiguration(
    releasePattern: Regex = "xxx".toRegex(),
    featurePattern: Regex = "xxx".toRegex(),
    dependencyBotPattern: Regex = "xxx".toRegex(),
    issuePattern: Regex? = null,
    versionPrefix: String = "xxx",
    normalization: List<String> = emptyList()
) : PublishingContract.VersioningConfiguration {
    override val releasePattern: Property<Regex>
    override val featurePattern: Property<Regex>
    override val dependencyBotPattern: Property<Regex>
    override val issuePattern: Property<Regex?>
    override val versionPrefix: Property<String>
    override val normalization: SetProperty<String>

    init {
        val project = ProjectBuilder.builder().build()

        this.releasePattern = project.objects.property(Regex::class.java).also {
            it.set(releasePattern)
        }

        this.featurePattern = project.objects.property(Regex::class.java).also {
            it.set(featurePattern)
        }

        this.dependencyBotPattern = project.objects.property(Regex::class.java).also {
            it.set(dependencyBotPattern)
        }

        this.issuePattern = project.objects.property(Regex::class.java).also {
            it.set(issuePattern)
        }

        this.versionPrefix = project.objects.property(String::class.java).also {
            it.set(versionPrefix)
        }

        this.normalization = project.objects.setProperty(String::class.java).also {
            it.set(normalization)
        }
    }
}
