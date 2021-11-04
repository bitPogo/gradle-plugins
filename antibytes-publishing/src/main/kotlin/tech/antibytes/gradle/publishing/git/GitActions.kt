/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.transport.PushResult
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract
import java.io.File

internal object GitActions : GitContract.GitActions {
    private fun useCredentials(
        username: String?,
        password: String?
    ): Boolean {
        return username is String && password is String
    }

    private fun update(
        git: Git,
        username: String?,
        password: String?
    ) {
        val fetch = git.fetch()
            .setForceUpdate(true)

        if (useCredentials(username, password)) {
            fetch.setCredentialsProvider(
                UsernamePasswordCredentialsProvider(username, password)
            )
        }

        fetch.call()
    }

    private fun hardReset(git: Git) {
        git.reset()
            .setMode(ResetCommand.ResetType.HARD)
            .setRef("origin/main")
            .call()
    }

    private fun updateAndReset(
        targetDirectory: File,
        repository: PublishingApiContract.RepositoryConfiguration
    ): Git {
        val git = Git.open(targetDirectory)

        update(
            git,
            repository.username,
            repository.password
        )

        hardReset(git)

        return git
    }

    private fun clone(
        targetDirectory: File,
        repository: PublishingApiContract.RepositoryConfiguration
    ): Git {
        val clone = Git.cloneRepository()
            .setURI(repository.url)

        if (useCredentials(repository.username, repository.password)) {
            clone.setCredentialsProvider(
                UsernamePasswordCredentialsProvider(repository.username, repository.password)
            )
        }

        clone.setDirectory(targetDirectory)
            .call()

        return Git.open(targetDirectory)
    }

    override fun checkout(
        project: Project,
        repository: PublishingApiContract.RepositoryConfiguration
    ): Git {
        val targetDirectory = File("${project.rootProject.buildDir.absolutePath}/${repository.name}")

        return try {
            updateAndReset(targetDirectory, repository)
        } catch (_: Throwable) {
            clone(targetDirectory, repository)
        }
    }

    private fun commit(
        repository: Git,
        commitMessage: String,
    ) {
        repository.add()
            .addFilepattern(".")
            .call()

        repository.commit()
            .setMessage(commitMessage)
            .setSign(false)
            .call()
    }

    private fun parsePushResult(
        results: Iterable<PushResult>
    ): Boolean {
        val update = results.first().remoteUpdates.first()
        return !update.status.name.startsWith("REJECTED")
    }

    private fun push(
        repository: Git,
        credentials: PublishingApiContract.Credentials,
        dryRun: Boolean
    ): Boolean {
        val push = repository.push().setDryRun(dryRun)
        if (useCredentials(credentials.username, credentials.password)) {
            push.setCredentialsProvider(
                UsernamePasswordCredentialsProvider(credentials.username, credentials.password)
            )
        }

        return parsePushResult(push.call())
    }

    override fun push(
        repository: Git,
        credentials: PublishingApiContract.Credentials,
        commitMessage: String,
        dryRun: Boolean
    ): Boolean {
        commit(repository, commitMessage)
        return push(repository, credentials, dryRun)
    }
}
