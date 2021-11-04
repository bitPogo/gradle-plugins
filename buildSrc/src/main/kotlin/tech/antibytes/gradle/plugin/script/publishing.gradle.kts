package tech.antibytes.gradle.plugin.script

import tech.antibytes.gradle.plugin.config.LibraryConfig
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.transport.PushResult
import org.eclipse.jgit.transport.RemoteRefUpdate
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

/**
 * Publish generated artefacts to our maven-repository using [jGit](https://www.eclipse.org/jgit/)
 *
 * Install:
 *
 * You need to add following dependencies to the buildSrc/build.gradle.kts
 *
 * dependencies {
 *     implementation("org.eclipse.jgit:org.eclipse.jgit:5.11.0.202103091610-r")
 * }
 *
 * and ensure that the mavenCentral repository is available
 *
 * repositories {
 *     mavenCentral()
 * }
 *
 * Now just add id("tech.antibytes.gradle.plugin.script.publishing") to your rootProject build.gradle.kts plugins
 *
 * plugins {
 *     id("tech.antibytes.gradle.plugin.script.publishing")
 * }
 *
 * Usage:
 *
 * To publish to to https://github.com/bitpogo/maven-dev/ just run:
 * - ./gradlew publishFeature
 * To publish to to https://github.com/bitpogo/maven-snapshot/ just run:
 * - ./gradlew publishSnapshot
 * To publish to to https://github.com/bitpogo/maven-release/ just run:
 * - ./gradlew publishRelease
 *
 * This requires publishing-config.gradle.kts!
 */
plugins {
    id("tech.antibytes.gradle.plugin.script.versioning")
}

val taskGroup = "publishing"
val prefix = "maven"
val separator = "-"

val devRepoName = "$prefix${separator}dev"
val snapshotRepoName = "$prefix${separator}snapshots"
val releaseRepoName = "$prefix${separator}releases"

val basePath = "${rootProject.buildDir}/gitPublish"

lateinit var git: Git
lateinit var repository: Repository
lateinit var flavour: String
lateinit var suffix: String

data class Repository(
    val url: String,
    val name: String
)

val githubUser = (project.findProperty("gpr.user")
    ?: System.getenv("PACKAGE_REGISTRY_UPLOAD_USERNAME")).toString()
val githubToken = (project.findProperty("gpr.key")
    ?: System.getenv("PACKAGE_REGISTRY_UPLOAD_TOKEN")).toString()


val publishPackage: Task by tasks.creating {
    dependsOn("setProjectVersion")

    doLast {
        git = Git.open(File(repository.url))
        gitCommit()
        println("push")
        gitPush()
    }
}

// Dev
val cloneDevRepository: Task by tasks.creating {
    doLast {
        repository = Repository(
            "$basePath/$devRepoName",
            devRepoName
        )

        gitClone()
    }
}

val publishDev: Task by tasks.creating {
    group = taskGroup

    dependsOn(
        cloneDevRepository,
        "publishAllPublicationsToDevPackagesRepository",
        publishPackage
    )
}


// snapshot
val cloneSnapshotRepository: Task by tasks.creating {
    doLast {
        repository = Repository(
            "$basePath/$snapshotRepoName",
            snapshotRepoName
        )

        gitClone()
    }
}

val publishSnapshot: Task by tasks.creating {
    group = taskGroup

    dependsOn(
        cloneSnapshotRepository,
        "publishAllPublicationsToSnapshotPackagesRepository",
        publishPackage
    )
}

// release
val cloneReleaseRepository: Task by tasks.creating {
    doLast {
        repository = Repository(
            "$basePath/$releaseRepoName",
            releaseRepoName
        )

        gitClone()
    }
}

val publishRelease: Task by tasks.creating {
    group = taskGroup

    dependsOn(
        cloneReleaseRepository,
        "publishAllPublicationsToReleasePackagesRepository",
        publishPackage
    )
}

// Git calls
fun gitClone() {
    try {
        git = Git.open(File(repository.url))
        gitUpdate()
    } catch (exception: Exception) {
        Git.cloneRepository()
            .setURI("https://github.com/bitPogo/${repository.name}.git")
            .setCredentialsProvider(
                UsernamePasswordCredentialsProvider(
                    githubUser,
                    githubToken
                )
            )
            .setDirectory(File(repository.url))
            .call()
    }
}

fun gitUpdate() {
    git.fetch()
        .setForceUpdate(true)
        .setCredentialsProvider(
            UsernamePasswordCredentialsProvider(
                githubUser,
                githubToken
            )
        )
        .call()

    git.reset()
        .setMode(ResetCommand.ResetType.HARD)
        .setRef("origin/main")
        .call()
}

fun gitCommit() {
    git.add().addFilepattern(".").call()
    git.commit()
        .setMessage("Publish ${LibraryConfig.name} ${project.version}")
        .setSign(false)
        .call()
}

fun gitPush() {
    val results: Iterable<PushResult> = git.push()
        .setCredentialsProvider(
            UsernamePasswordCredentialsProvider(
                githubUser,
                githubToken
            )
        )
        .call()

    results.forEach { result ->
        (result.remoteUpdates as Collection<RemoteRefUpdate>).forEach { update ->
            if (
                update.status == RemoteRefUpdate.Status.REJECTED_NONFASTFORWARD
                || update.status == RemoteRefUpdate.Status.REJECTED_REMOTE_CHANGED
                || update.status == RemoteRefUpdate.Status.REJECTED_NODELETE
                || update.status == RemoteRefUpdate.Status.REJECTED_OTHER_REASON
            ) {
                throw IllegalStateException("Remote advanced! Please update first")
            }
            println(update.status)
        }
    }
}

