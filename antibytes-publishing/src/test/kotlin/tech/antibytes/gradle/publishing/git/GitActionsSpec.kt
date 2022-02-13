/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import io.mockk.verifyOrder
import org.eclipse.jgit.api.AddCommand
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.CommitCommand
import org.eclipse.jgit.api.FetchCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PushCommand
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.transport.CredentialItem
import org.eclipse.jgit.transport.PushResult
import org.eclipse.jgit.transport.RemoteRefUpdate
import org.eclipse.jgit.transport.URIish
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.api.Project
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingApiContract
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class GitActionsSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setUp() {
        mockkStatic(Git::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Git::class)
    }

    @Test
    fun `It fulfils GitActions`() {
        val actions: Any = GitActions

        assertTrue(actions is GitContract.GitActions)
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it updates and resets the repository if it already exists locally`() {
        // Given
        val buildDir = File(fixture<String>())
        val name: String = fixture()

        val project: Project = mockk()
        val git: Git = mockk()
        val fetch: FetchCommand = mockk()
        val reset: ResetCommand = mockk()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = fixture(),
        )

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } returns git

        every { git.fetch() } returns fetch
        every { fetch.setForceUpdate(true) } returns fetch
        every { fetch.setCredentialsProvider(any()) } returns fetch
        every { fetch.call() } returns mockk()

        every { git.reset() } returns reset
        every { reset.setMode(ResetCommand.ResetType.HARD) } returns reset
        every { reset.setRef("origin/main") } returns reset
        every { reset.call() } returns mockk()

        every { git.close() } just Runs

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = Unit,
            expected = result
        )

        verifyOrder {
            git.fetch()
            fetch.isForceUpdate = true
            fetch.setCredentialsProvider(any())
            fetch.call()

            git.reset()
            reset.setMode(ResetCommand.ResetType.HARD)
            reset.setRef("origin/main")
            reset.call()

            git.close()
        }
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it sets the Credentials for the update, if username and password were given`() {
        // Given
        val buildDir = File(fixture<String>())
        val name: String = fixture()

        val username: String = fixture()
        val password: String = fixture()

        val project: Project = mockk()
        val git: Git = mockk()
        val fetch: FetchCommand = mockk()
        val reset: ResetCommand = mockk()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = fixture(),
            username = username,
            password = password
        )

        val credentialsProvider = slot<UsernamePasswordCredentialsProvider>()

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } returns git

        every { git.fetch() } returns fetch
        every { fetch.setForceUpdate(true) } returns fetch
        every { fetch.setCredentialsProvider(capture(credentialsProvider)) } returns fetch
        every { fetch.call() } returns mockk()

        every { git.reset() } returns reset
        every { reset.setMode(ResetCommand.ResetType.HARD) } returns reset
        every { reset.setRef("origin/main") } returns reset
        every { reset.call() } returns mockk()

        every { git.close() } just Runs

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = Unit,
            expected = result
        )

        val actualUsername = CredentialItem.Username()
        val actualPassword = CredentialItem.Password()
        val credentialURI = URIish()

        credentialsProvider.captured.get(credentialURI, actualUsername, actualPassword)

        assertEquals(
            actual = actualUsername.value,
            expected = username
        )

        assertEquals(
            actual = String(actualPassword.value),
            expected = password
        )

        verifyOrder {
            git.fetch()
            fetch.isForceUpdate = true
            fetch.setCredentialsProvider(any())
            fetch.call()

            git.reset()
            reset.setMode(ResetCommand.ResetType.HARD)
            reset.setRef("origin/main")
            reset.call()

            git.close()
        }
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it clones the repository to RootProjects BuildDir with the given name`() {
        // Given
        val buildDir = File(fixture<String>())
        val url: String = fixture()
        val name: String = fixture()

        var firstOpen = true

        val project: Project = mockk()
        val git: Git = mockk()
        val clone: CloneCommand = mockk()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = url,
        )

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } answers {
            if (!firstOpen) {
                firstOpen = false
                throw Exception("not there")
            } else {
                git
            }
        }

        every { Git.cloneRepository() } returns clone
        every { clone.setURI(url) } returns clone
        every { clone.setDirectory(File("${buildDir.absolutePath}/$name")) } returns clone
        every { clone.setCredentialsProvider(any()) } returns clone
        every { clone.call() } returns mockk()

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = Unit,
            expected = result
        )

        verifyOrder {
            clone.setURI(url)
            clone.setCredentialsProvider(any())
            clone.setDirectory(File("${buildDir.absolutePath}/$name"))
            clone.call()
        }
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it sets the credentials for clone if username and password were given`() {
        // Given
        val buildDir = File(fixture<String>())
        val url: String = fixture()
        val name: String = fixture()
        val username: String = fixture()
        val password: String = fixture()

        var firstOpen = true

        val project: Project = mockk()
        val git: Git = mockk()
        val clone: CloneCommand = mockk()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = url,
            username = username,
            password = password
        )

        val credentialsProvider = slot<UsernamePasswordCredentialsProvider>()

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } answers {
            if (!firstOpen) {
                firstOpen = false
                throw Exception("not there")
            } else {
                git
            }
        }

        every { Git.cloneRepository() } returns clone
        every { clone.setURI(url) } returns clone
        every { clone.setDirectory(File("${buildDir.absolutePath}/$name")) } returns clone
        every { clone.setCredentialsProvider(capture(credentialsProvider)) } returns clone
        every { clone.call() } returns mockk()

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        val actualUsername = CredentialItem.Username()
        val actualPassword = CredentialItem.Password()
        val credentialURI = URIish()

        credentialsProvider.captured.get(credentialURI, actualUsername, actualPassword)

        assertEquals(
            actual = actualUsername.value,
            expected = username
        )

        assertEquals(
            actual = String(actualPassword.value),
            expected = password
        )

        assertSame(
            actual = Unit,
            expected = result
        )

        verifyOrder {
            clone.setURI(url)
            clone.setCredentialsProvider(capture(credentialsProvider))
            clone.setDirectory(File("${buildDir.absolutePath}/$name"))
            clone.call()
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it commits and pushes all files in the given repository`() {
        // Given
        val buildDir = File(fixture<String>())
        val name: String = fixture()
        val message: String = fixture()
        val dryRun: Boolean = fixture()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = fixture(),
        )

        val project: Project = mockk()
        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } returns git

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.setCredentialsProvider(any()) } returns push
        every { push.call() } returns listOf(pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult)
        every { referenceResult.status } returns RemoteRefUpdate.Status.OK

        every { git.close() } just Runs

        // When
        val result = GitActions.push(
            project,
            configuration,
            message,
            dryRun
        )

        // Then

        assertTrue(result)

        verifyOrder {
            git.add()
            add.addFilepattern(".")
            add.call()

            git.commit()
            commit.message = message
            commit.setSign(false)
            commit.call()

            git.push()
            push.isDryRun = dryRun
            push.setCredentialsProvider(any())
            push.call()

            pushResult.remoteUpdates
            referenceResult.status

            git.close()
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it respects only the first PushResult`() {
        // Given
        val buildDir = File(fixture<String>())
        val name: String = fixture()
        val message: String = fixture()
        val dryRun: Boolean = fixture()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = fixture(),
        )

        val project: Project = mockk()
        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } returns git

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.setCredentialsProvider(any()) } returns push
        every { push.call() } returns listOf(pushResult, pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult)
        every { referenceResult.status } returns RemoteRefUpdate.Status.OK

        every { git.close() } just Runs

        // When
        val result = GitActions.push(
            project,
            configuration,
            message,
            dryRun
        )

        // Then
        assertTrue(result)

        verify(exactly = 1) { pushResult.remoteUpdates }

        verifyOrder {
            git.add()
            add.addFilepattern(".")
            add.call()

            git.commit()
            commit.message = message
            commit.setSign(false)
            commit.call()

            git.push()
            push.isDryRun = dryRun
            push.setCredentialsProvider(any())
            push.call()

            pushResult.remoteUpdates
            referenceResult.status

            git.close()
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it respects only the first RemoteRefUpdate`() {
        // Given
        val buildDir = File(fixture<String>())
        val name: String = fixture()
        val message: String = fixture()
        val dryRun: Boolean = fixture()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = fixture(),
        )

        val project: Project = mockk()
        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } returns git

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.setCredentialsProvider(any()) } returns push
        every { push.call() } returns listOf(pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult, referenceResult)
        every { referenceResult.status } returns RemoteRefUpdate.Status.OK

        every { git.close() } just Runs

        // When
        val result = GitActions.push(
            project,
            configuration,
            message,
            dryRun
        )

        // Then
        assertTrue(result)

        verify(exactly = 1) { referenceResult.status }

        verifyOrder {
            git.add()
            add.addFilepattern(".")
            add.call()

            git.commit()
            commit.message = message
            commit.setSign(false)
            commit.call()

            git.push()
            push.isDryRun = dryRun
            push.setCredentialsProvider(any())
            push.call()

            pushResult.remoteUpdates
            referenceResult.status

            git.close()
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it sets given credentials`() {
        // Given
        val dryRun: Boolean = fixture()
        val buildDir = File(fixture<String>())
        val name: String = fixture()
        val message: String = fixture()
        val username: String = fixture()
        val password: String = fixture()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = fixture(),
            username = username,
            password = password
        )

        val credentialsProvider = slot<UsernamePasswordCredentialsProvider>()

        val project: Project = mockk()
        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } returns git

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.setCredentialsProvider(capture(credentialsProvider)) } returns push
        every { push.call() } returns listOf(pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult)
        every { referenceResult.status } returns RemoteRefUpdate.Status.OK

        every { git.close() } just Runs

        // When
        val result = GitActions.push(
            project,
            configuration,
            message,
            dryRun
        )

        // Then
        val actualUsername = CredentialItem.Username()
        val actualPassword = CredentialItem.Password()
        val credentialURI = URIish()

        credentialsProvider.captured.get(credentialURI, actualUsername, actualPassword)

        assertEquals(
            actual = actualUsername.value,
            expected = username
        )

        assertEquals(
            actual = String(actualPassword.value),
            expected = password
        )

        assertTrue(result)

        verifyOrder {
            git.add()
            add.addFilepattern(".")
            add.call()

            git.commit()
            commit.message = message
            commit.setSign(false)
            commit.call()

            git.push()
            push.isDryRun = dryRun
            push.setCredentialsProvider(capture(credentialsProvider))
            push.call()

            pushResult.remoteUpdates
            referenceResult.status

            git.close()
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it returns false if the Rejected status`() {
        // Given
        val dryRun: Boolean = fixture()
        val buildDir = File(fixture<String>())
        val name: String = fixture()
        val message: String = fixture()
        val password: String = fixture()

        val rejections = listOf(
            RemoteRefUpdate.Status.REJECTED_NODELETE,
            RemoteRefUpdate.Status.REJECTED_NONFASTFORWARD,
            RemoteRefUpdate.Status.REJECTED_OTHER_REASON,
            RemoteRefUpdate.Status.REJECTED_REMOTE_CHANGED
        )

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = fixture(),
            password = password,
        )

        val project: Project = mockk()
        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } returns git

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.setCredentialsProvider(any()) } returns push
        every { push.call() } returns listOf(pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult)
        every { referenceResult.status } returnsMany rejections

        every { git.close() } just Runs

        rejections.forEach { status ->
            // When
            val result = GitActions.push(
                project,
                configuration,
                message,
                dryRun
            )

            // Then
            assertFalse(
                result,
                message = "Failed at ${status.name}"
            )
        }
    }
}

private data class TestRepositoryConfiguration(
    override val name: String,
    override val url: String,
    override val username: String = "",
    override val password: String = ""
) : PublishingApiContract.RepositoryConfiguration
