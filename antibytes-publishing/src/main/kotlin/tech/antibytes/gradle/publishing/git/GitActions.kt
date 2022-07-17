/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import java.io.File
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.transport.PushResult
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract

internal object GitActions : GitContract.GitActions {
    private fun update(
        git: Git,
        credentials: PublishingApiContract.Credentials,
    ) {
        git.fetch()
            .setForceUpdate(true)
            .setCredentialsProvider(
                UsernamePasswordCredentialsProvider(
                    credentials.username,
                    credentials.password,
                ),
            )
            .call()
    }

    private fun hardReset(git: Git) {
        git.reset()
            .setMode(ResetCommand.ResetType.HARD)
            .setRef("origin/main")
            .call()
    }

    private fun updateAndReset(
        targetDirectory: File,
        repository: PublishingApiContract.RepositoryConfiguration,
    ) {
        val git = Git.open(targetDirectory)

        update(git, repository)

        hardReset(git)

        git.close()
    }

    private fun clone(
        targetDirectory: File,
        repository: PublishingApiContract.RepositoryConfiguration,
    ) {
        Git.cloneRepository()
            .setURI(repository.url)
            .setCredentialsProvider(
                UsernamePasswordCredentialsProvider(
                    repository.username,
                    repository.password,
                ),
            )
            .setDirectory(targetDirectory)
            .call()
    }

    override fun checkout(
        project: Project,
        repository: PublishingApiContract.RepositoryConfiguration,
    ) {
        val targetDirectory = File("${project.rootProject.buildDir.absolutePath}/${repository.name}")

        try {
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
        results: Iterable<PushResult>,
    ): Boolean {
        val update = results.first().remoteUpdates.first()
        return !update.status.name.startsWith("REJECTED")
    }

    private fun push(
        git: Git,
        credentials: PublishingApiContract.Credentials,
        dryRun: Boolean,
    ): Boolean {
        val push = git.push()
            .setDryRun(dryRun)
            .setCredentialsProvider(
                UsernamePasswordCredentialsProvider(
                    credentials.username,
                    credentials.password,
                ),
            )

        return parsePushResult(push.call())
    }

    private fun push(
        git: Git,
        credentials: PublishingApiContract.Credentials,
        commitMessage: String,
        dryRun: Boolean,
    ): Boolean {
        commit(git, commitMessage)
        return push(git, credentials, dryRun)
    }

    override fun push(
        project: Project,
        repository: PublishingApiContract.RepositoryConfiguration,
        commitMessage: String,
        dryRun: Boolean,
    ): Boolean {
        val git = Git.open(
            File("${project.rootProject.buildDir.absolutePath}/${repository.name}"),
        )

        return push(
            git,
            repository,
            commitMessage,
            dryRun,
        ).also { git.close() }
    }
}
