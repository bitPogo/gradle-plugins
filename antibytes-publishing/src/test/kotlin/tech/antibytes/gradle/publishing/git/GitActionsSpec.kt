/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
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
import org.junit.Test
import tech.antibytes.gradle.publishing.PublishingApiContract
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class GitActionsSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils GitActions`() {
        val actions: Any = GitActions

        assertTrue(actions is GitContract.GitActions)
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it updates and resets the repository if it already exists locally`() {
        mockkStatic(Git::class)

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
        every { fetch.call() } returns mockk()

        every { git.reset() } returns reset
        every { reset.setMode(ResetCommand.ResetType.HARD) } returns reset
        every { reset.setRef("origin/main") } returns reset
        every { reset.call() } returns mockk()

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = git,
            expected = result
        )

        verifyOrder {
            git.fetch()
            fetch.isForceUpdate = true
            fetch.call()
            git.reset()
            reset.setMode(ResetCommand.ResetType.HARD)
            reset.setRef("origin/main")
            reset.call()
        }

        unmockkStatic(Git::class)
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it sets the Credentials for the update, if username and password were given`() {
        mockkStatic(Git::class)

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

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = git,
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
        }

        unmockkStatic(Git::class)
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it will not sets the Credentials for the update, if only the username was given`() {
        mockkStatic(Git::class)

        // Given
        val buildDir = File(fixture<String>())
        val name: String = fixture()
        val username: String = fixture()

        val project: Project = mockk()
        val git: Git = mockk()
        val fetch: FetchCommand = mockk()
        val reset: ResetCommand = mockk()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = fixture(),
            username = username
        )

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } returns git

        every { git.fetch() } returns fetch
        every { fetch.setForceUpdate(true) } returns fetch
        every { fetch.call() } returns mockk()

        every { git.reset() } returns reset
        every { reset.setMode(ResetCommand.ResetType.HARD) } returns reset
        every { reset.setRef("origin/main") } returns reset
        every { reset.call() } returns mockk()

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = git,
            expected = result
        )

        verify(exactly = 0) { fetch.setCredentialsProvider(any()) }

        verifyOrder {
            git.fetch()
            fetch.isForceUpdate = true
            fetch.call()

            git.reset()
            reset.setMode(ResetCommand.ResetType.HARD)
            reset.setRef("origin/main")
            reset.call()
        }

        unmockkStatic(Git::class)
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it sets the Credentials for the update, if only password was given`() {
        mockkStatic(Git::class)

        // Given
        val buildDir = File(fixture<String>())
        val name: String = fixture()
        val password: String = fixture()

        val project: Project = mockk()
        val git: Git = mockk()
        val fetch: FetchCommand = mockk()
        val reset: ResetCommand = mockk()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = fixture(),
            password = password
        )

        every { project.rootProject.buildDir } returns buildDir
        every { Git.open(File("${buildDir.absolutePath}/$name")) } returns git

        every { git.fetch() } returns fetch
        every { fetch.setForceUpdate(true) } returns fetch
        every { fetch.call() } returns mockk()

        every { git.reset() } returns reset
        every { reset.setMode(ResetCommand.ResetType.HARD) } returns reset
        every { reset.setRef("origin/main") } returns reset
        every { reset.call() } returns mockk()

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = git,
            expected = result
        )

        verify(exactly = 0) { fetch.setCredentialsProvider(any()) }

        verifyOrder {
            git.fetch()
            fetch.isForceUpdate = true
            fetch.call()

            git.reset()
            reset.setMode(ResetCommand.ResetType.HARD)
            reset.setRef("origin/main")
            reset.call()
        }

        unmockkStatic(Git::class)
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it clones the repository to Rootprojects BuildDir with the given name`() {
        mockkStatic(Git::class)

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
        every { clone.call() } returns mockk()

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = git,
            expected = result
        )

        verifyOrder {
            clone.setURI(url)
            clone.setDirectory(File("${buildDir.absolutePath}/$name"))
            clone.call()
        }

        unmockkStatic(Git::class)
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it sets the credentials for clone if username and password were given`() {
        mockkStatic(Git::class)

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
            actual = git,
            expected = result
        )

        verifyOrder {
            clone.setURI(url)
            clone.setCredentialsProvider(capture(credentialsProvider))
            clone.setDirectory(File("${buildDir.absolutePath}/$name"))
            clone.call()
        }

        unmockkStatic(Git::class)
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it will not set the credentials for clone if only the username was given`() {
        mockkStatic(Git::class)

        // Given
        val buildDir = File(fixture<String>())
        val url: String = fixture()
        val name: String = fixture()
        val username: String = fixture()

        var firstOpen = true

        val project: Project = mockk()
        val git: Git = mockk()
        val clone: CloneCommand = mockk()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = url,
            username = username
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
        every { clone.call() } returns mockk()

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = git,
            expected = result
        )

        verify(exactly = 0) { clone.setCredentialsProvider(any()) }

        verifyOrder {
            clone.setURI(url)
            clone.setDirectory(File("${buildDir.absolutePath}/$name"))
            clone.call()
        }

        unmockkStatic(Git::class)
    }

    @Test
    fun `Given checkout is called with a GitRepositoryConfiguration, it will not set the credentials for clone if only the password was given`() {
        mockkStatic(Git::class)

        // Given
        val buildDir = File(fixture<String>())
        val url: String = fixture()
        val name: String = fixture()
        val password: String = fixture()

        var firstOpen = true

        val project: Project = mockk()
        val git: Git = mockk()
        val clone: CloneCommand = mockk()

        val configuration = TestRepositoryConfiguration(
            name = name,
            url = url,
            password = password
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
        every { clone.call() } returns mockk()

        // When
        val result = GitActions.checkout(project, configuration)

        // Then
        assertSame(
            actual = git,
            expected = result
        )

        verify(exactly = 0) { clone.setCredentialsProvider(any()) }

        verifyOrder {
            clone.setURI(url)
            clone.setDirectory(File("${buildDir.absolutePath}/$name"))
            clone.call()
        }

        unmockkStatic(Git::class)
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it commits and pushs all files in the given repository`() {
        // Given
        val message: String = fixture()
        val dryRun = false

        val configuration = TestRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
        )

        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.call() } returns listOf(pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult)
        every { referenceResult.status } returns RemoteRefUpdate.Status.OK

        // When
        val result = GitActions.push(
            git,
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
            push.call()

            pushResult.remoteUpdates
            referenceResult.status
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it respects only the first PushResult`() {
        // Given
        val message: String = fixture()
        val dryRun = false

        val configuration = TestRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
        )

        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.call() } returns listOf(pushResult, pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult)
        every { referenceResult.status } returns RemoteRefUpdate.Status.OK

        // When
        val result = GitActions.push(
            git,
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
            push.call()

            pushResult.remoteUpdates
            referenceResult.status
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it respects only the first RemoteRefUpdate`() {
        // Given
        val message: String = fixture()
        val dryRun = true

        val configuration = TestRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
        )

        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.call() } returns listOf(pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult, referenceResult)
        every { referenceResult.status } returns RemoteRefUpdate.Status.OK

        // When
        val result = GitActions.push(
            git,
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
            push.setDryRun(dryRun)
            push.call()

            pushResult.remoteUpdates
            referenceResult.status
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it sets given credentials`() {
        // Given
        val dryRun = true
        val message: String = fixture()
        val username: String = fixture()
        val password: String = fixture()

        val configuration = TestRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = username,
            password = password
        )

        val credentialsProvider = slot<UsernamePasswordCredentialsProvider>()

        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

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

        // When
        val result = GitActions.push(
            git,
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
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it will not set credentials if only a username was given`() {
        // Given
        val dryRun = false
        val message: String = fixture()
        val username: String = fixture()

        val configuration = TestRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = username,
        )

        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.call() } returns listOf(pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult)
        every { referenceResult.status } returns RemoteRefUpdate.Status.OK

        // When
        val result = GitActions.push(
            git,
            configuration,
            message,
            dryRun
        )

        // Then
        assertTrue(result)

        verify(exactly = 0) { push.setCredentialsProvider(any()) }

        verifyOrder {
            git.add()
            add.addFilepattern(".")
            add.call()

            git.commit()
            commit.message = message
            commit.setSign(false)
            commit.call()

            git.push()
            push.setDryRun(dryRun)
            push.call()

            pushResult.remoteUpdates
            referenceResult.status
        }
    }

    @Test
    fun `Given push is called with a GitRepository, Credentials, a CommitMessage, and a DryRun flag, it will not set credentials if only a password was given`() {
        // Given
        val dryRun = false
        val message: String = fixture()
        val password: String = fixture()

        val configuration = TestRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            password = password,
        )

        val git: Git = mockk()
        val add: AddCommand = mockk()
        val commit: CommitCommand = mockk()
        val push: PushCommand = mockk()
        val pushResult: PushResult = mockk()
        val referenceResult: RemoteRefUpdate = mockk()

        every { git.add() } returns add
        every { add.addFilepattern(".") } returns add
        every { add.call() } returns mockk()

        every { git.commit() } returns commit
        every { commit.setMessage(message) } returns commit
        every { commit.setSign(false) } returns commit
        every { commit.call() } returns mockk()

        every { git.push() } returns push
        every { push.setDryRun(dryRun) } returns push
        every { push.call() } returns listOf(pushResult)

        every { pushResult.remoteUpdates } returns listOf(referenceResult)
        every { referenceResult.status } returns RemoteRefUpdate.Status.OK

        // When
        val result = GitActions.push(
            git,
            configuration,
            message,
            dryRun
        )

        // Then
        assertTrue(result)

        verify(exactly = 0) { push.setCredentialsProvider(any()) }

        verifyOrder {
            git.add()
            add.addFilepattern(".")
            add.call()

            git.commit()
            commit.message = message
            commit.setSign(false)
            commit.call()

            git.push()
            push.setDryRun(dryRun)
            push.call()

            pushResult.remoteUpdates
            referenceResult.status
        }
    }
}

private data class TestRepositoryConfiguration(
    override val name: String,
    override val url: String,
    override val username: String? = null,
    override val password: String? = null,

) : PublishingApiContract.RepositoryConfiguration
