/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.verisoning

import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import tech.antibytes.gradle.verisoning.VersioningContract.VersioningConfiguration
import tech.antibytes.gradle.verisoning.api.VersionInfo
import tech.antibytes.gradle.verisoning.api.VersioningError

class Versioning private constructor(
    private val versionDetails: Closure<VersionDetails>,
    private val configuration: VersioningConfiguration
) : VersioningContract.Versioning {
    private fun removeVersionPrefix(version: String): String {
        return version.substringAfter(configuration.versionPrefix)
    }

    private fun cleanVersionName(
        version: String,
        commitDistance: Int
    ): String {
        var cleanName = version.substringBefore(".dirty")

        cleanName = if (commitDistance > 0) {
            cleanName.substringBefore(SEPARATOR)
        } else {
            cleanName
        }

        return removeVersionPrefix(cleanName)
    }

    // Schema:
    // version '-' [gitHash] '-' 'SNAPSHOT'
    private fun renderSnapshotName(
        details: VersionDetails,
        useGitHash: Boolean
    ): String {
        val versionName = cleanVersionName(
            details.version,
            details.commitDistance
        )

        val snapShotName = mutableListOf(versionName)

        if (useGitHash) {
            snapShotName.add(details.gitHash)
        }

        snapShotName.add(NON_RELEASE_SUFFIX)

        return snapShotName.joinToString(SEPARATOR)
    }

    // Schema:
    // version
    private fun renderRelease(details: VersionDetails): String = removeVersionPrefix(details.version)

    private fun renderReleaseOrSnapshotBranch(
        details: VersionDetails,
        useGitHash: Boolean
    ): String {
        return if (!details.isCleanTag || details.commitDistance > 0) {
            renderSnapshotName(details, useGitHash)
        } else {
            renderRelease(details)
        }
    }

    private fun normalize(name: String): String {
        var normalized = name

        configuration.normalization.forEach { dangled ->
            normalized = normalized.replace(dangled, SEPARATOR)
        }

        return normalized
    }

    private fun resolveSnapshotPattern(prefixes: List<String>): Regex {
        val prefixPattern = prefixes.joinToString("|")

        return "($prefixPattern)/(.*)".toRegex()
    }

    private fun extractBranchName(
        prefixes: List<String>,
        name: String
    ): String {
        val pattern = resolveSnapshotPattern(prefixes)

        return pattern.matchEntire(name)!!.groups[2]!!.value
    }

    private fun renderDependencyBotBranch(details: VersionDetails): String {
        val infix = normalize(
            extractBranchName(
                configuration.dependencyBotPrefixes,
                details.branchName
            )
        )

        val versionName = cleanVersionName(
            details.version,
            details.commitDistance
        )

        // Schema:
        // version '-' 'bump '-' infix '-' 'SNAPSHOT'
        return "$versionName${SEPARATOR}bump$SEPARATOR$infix$SEPARATOR$NON_RELEASE_SUFFIX"
    }

    private fun createFeatureVersionName(
        details: VersionDetails,
        useGitHash: Boolean,
        versionName: String,
        infix: String
    ): String {
        val version = mutableListOf(
            versionName,
            infix
        )

        if (useGitHash) {
            version.add(details.gitHash)
        }

        version.add(NON_RELEASE_SUFFIX)

        return version.joinToString(SEPARATOR)
    }

    private fun extractIssue(
        pattern: Regex,
        name: String
    ): String = pattern.matchEntire(name)!!.groups[1]!!.value

    private fun renderFeatureBranch(details: VersionDetails, useGitHash: Boolean): String {
        var infix = extractBranchName(
            configuration.featurePrefixes,
            details.branchName
        )

        infix = if (configuration.issuePattern is Regex && configuration.issuePattern!!.matches(infix)) {
            extractIssue(
                configuration.issuePattern!!,
                infix
            )
        } else {
            infix
        }

        // Schema:
        // version '-' infix [- gitHash ] '-' 'SNAPSHOT'
        return createFeatureVersionName(
            details,
            useGitHash,
            cleanVersionName(
                details.version,
                details.commitDistance
            ),
            normalize(infix)
        )
    }

    private fun resolveReleaseBranchPattern(releaseBranchNames: List<String>): Regex {
        val branchNames = releaseBranchNames.joinToString("|")

        return "$branchNames/.*".toRegex()
    }

    private fun resolveVersionName(details: VersionDetails): String {
        val releaseBranchPattern = resolveReleaseBranchPattern(configuration.releasePrefixes)
        val dependencyBotPattern = resolveSnapshotPattern(configuration.dependencyBotPrefixes)
        val featureBranchPattern = resolveSnapshotPattern(configuration.featurePrefixes)

        return when {
            details.branchName == null -> renderReleaseOrSnapshotBranch(
                details,
                configuration.useGitHashSnapshotSuffix
            )
            details.branchName.matches(releaseBranchPattern) -> renderReleaseOrSnapshotBranch(
                details,
                configuration.useGitHashSnapshotSuffix
            )
            details.branchName.matches(dependencyBotPattern) -> renderDependencyBotBranch(details)
            details.branchName.matches(featureBranchPattern) -> renderFeatureBranch(
                details,
                configuration.useGitHashFeatureSuffix
            )
            else -> throw VersioningError(
                "Ill named branch name (${details.branchName})! Please adjust it to match the project settings."
            )
        }
    }

    override fun versionName(): String = resolveVersionName(versionDetails())

    override fun versionInfo(): VersionInfo {
        return VersionInfo(
            resolveVersionName(versionDetails()),
            versionDetails()
        )
    }

    companion object : VersioningContract.VersioningFactory {
        private const val SEPARATOR = "-"
        private const val NON_RELEASE_SUFFIX = "SNAPSHOT"

        override fun getInstance(
            project: Project,
            configuration: VersioningConfiguration
        ): VersioningContract.Versioning {
            val versionDetails: Closure<VersionDetails> by project.extra

            return Versioning(versionDetails, configuration)
        }
    }
}
