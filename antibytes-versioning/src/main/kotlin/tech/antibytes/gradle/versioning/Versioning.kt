/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning

import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration
import tech.antibytes.gradle.versioning.api.VersionInfo
import tech.antibytes.gradle.versioning.api.VersioningError

private fun<T> Boolean.ifElse(ifBranch: T, elseBranch: T): T {
    return if (this) {
        ifBranch
    } else {
        elseBranch
    }
}

class Versioning private constructor(
    private val versionDetails: Closure<VersionDetails>,
    private val configuration: VersioningConfiguration,
) : VersioningContract.Versioning {
    private fun removeVersionPrefix(version: String): String = version.substringAfter(configuration.versionPrefix)

    private fun MutableList<String>.addSnapshotSuffix() {
        if (!configuration.suppressSnapshot) {
            add(NON_RELEASE_SUFFIX)
        }
    }

    private fun String.amendRC(fullName: String): String {
        val suffix = if (fullName.contains(RELEASE_CANDIDATE_SUFFIX)) {
            val rcNumber = fullName.substringAfter(RELEASE_CANDIDATE_SUFFIX).substringBefore(SEPARATOR)

            "$RELEASE_CANDIDATE_SUFFIX$rcNumber"
        } else {
            ""
        }

        return "$this$suffix"
    }

    private fun cleanVersionName(
        version: String,
        commitDistance: Int,
    ): String {
        val name = version.substringBefore(".dirty")

        val cleanName = if (commitDistance > 0) {
            name.substringBefore(SEPARATOR).amendRC(name)
        } else {
            name
        }

        return removeVersionPrefix(cleanName)
    }

    // Schema:
    // version '-' [gitHash] '-' 'SNAPSHOT'
    private fun renderSnapshotName(
        details: VersionDetails,
        useGitHash: Boolean,
    ): String {
        val versionName = cleanVersionName(
            details.version,
            details.commitDistance,
        )

        val snapShotName = mutableListOf(versionName)

        if (useGitHash) {
            snapShotName.add(details.gitHash)
        }

        snapShotName.addSnapshotSuffix()

        return snapShotName.joinToString(SEPARATOR)
    }

    // Schema:
    // version
    private fun renderRelease(details: VersionDetails): String = removeVersionPrefix(details.version)

    private fun renderReleaseOrSnapshotBranch(
        details: VersionDetails,
        useGitHash: Boolean,
    ): String {
        return if (!details.isCleanTag || details.commitDistance > 0) {
            renderSnapshotName(details, useGitHash)
        } else {
            renderRelease(details)
        }
    }

    private fun normalizeVersionName(name: String): String {
        var normalized = name

        configuration.normalization.forEach { dangled ->
            normalized = normalized.replace(dangled, SEPARATOR)
        }

        return normalized
    }

    private fun resolveSnapshotPattern(prefixes: List<String>): Regex {
        return if (prefixes.isEmpty()) {
            "(.+)".toRegex()
        } else {
            val prefixPattern = prefixes.joinToString("|")

            "($prefixPattern)/(.+)".toRegex()
        }
    }

    private fun extractBranchName(
        prefixes: List<String>,
        name: String,
    ): String {
        val pattern = resolveSnapshotPattern(prefixes)
        val idx = prefixes.isEmpty().ifElse(1, 2)

        return pattern.matchEntire(name)!!.groups[idx]!!.value
    }

    private fun renderDependencyBotBranch(
        normalizedBranchName: String,
        details: VersionDetails,
    ): String {
        val infix = normalizeVersionName(
            extractBranchName(
                configuration.dependencyBotPrefixes,
                normalizedBranchName,
            ),
        )

        val versionName = cleanVersionName(
            details.version,
            details.commitDistance,
        )

        // Schema:
        // version '-' 'bump '-' infix '-' 'SNAPSHOT'
        return mutableListOf(versionName, "bump", infix).apply {
            addSnapshotSuffix()
        }.joinToString(SEPARATOR)
    }

    private fun createFeatureVersionName(
        details: VersionDetails,
        useGitHash: Boolean,
        versionName: String,
        infix: String,
    ): String {
        val version = mutableListOf(
            versionName,
            infix,
        )

        if (useGitHash) {
            version.add(details.gitHash)
        }

        version.addSnapshotSuffix()

        return version.joinToString(SEPARATOR)
    }

    private fun extractIssue(
        pattern: Regex,
        name: String,
    ): String = pattern.matchEntire(name)!!.groups[1]!!.value

    private fun renderFeatureBranch(
        normalizedBranchName: String,
        details: VersionDetails,
        useGitHash: Boolean,
    ): String {
        var infix = extractBranchName(
            configuration.featurePrefixes,
            normalizedBranchName,
        )

        infix = if (configuration.issuePattern is Regex && configuration.issuePattern!!.matches(infix)) {
            extractIssue(
                configuration.issuePattern!!,
                infix,
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
                details.commitDistance,
            ),
            normalizeVersionName(infix),
        )
    }

    private fun resolveReleaseBranchPattern(releaseBranchNames: List<String>): Regex {
        val branchNames = releaseBranchNames.joinToString("|")

        return "$branchNames/.*".toRegex()
    }

    private fun normalizeBranchName(
        configuration: VersioningConfiguration,
        details: VersionDetails,
    ): String? {
        val delimiter = StringBuilder().run {
            append(configuration.featurePrefixes.joinToString("|"))
            append("|")
            append(configuration.releasePrefixes.joinToString("|"))
            append("|")
            append(configuration.dependencyBotPrefixes.joinToString("|"))
        }.trimStart('|').toString() + "/?"

        val prefix = details.branchName?.run {
            ".*($delimiter).*".toRegex().matchEntire(this)?.groups?.get(1)?.value
        } ?: ""

        return details.branchName?.run {
            prefix + split(delimiter.toRegex()).last()
        }
    }

    private fun resolveVersionName(details: VersionDetails): String {
        val releaseBranchPattern = resolveReleaseBranchPattern(configuration.releasePrefixes)
        val dependencyBotPattern = resolveSnapshotPattern(configuration.dependencyBotPrefixes)
        val featureBranchPattern = resolveSnapshotPattern(configuration.featurePrefixes)
        val branchName = normalizeBranchName(configuration, details)

        return when {
            branchName == null -> renderReleaseOrSnapshotBranch(
                details,
                configuration.useGitHashSnapshotSuffix,
            )
            branchName.matches(releaseBranchPattern) -> renderReleaseOrSnapshotBranch(
                details,
                configuration.useGitHashSnapshotSuffix,
            )
            branchName.matches(dependencyBotPattern) -> renderDependencyBotBranch(branchName, details)
            branchName.matches(featureBranchPattern) -> renderFeatureBranch(
                branchName,
                details,
                configuration.useGitHashFeatureSuffix,
            )
            else -> throw VersioningError(
                "Ill named branch name (${details.branchName})! Please adjust it to match the project settings.",
            )
        }
    }

    override fun versionName(): String = resolveVersionName(versionDetails())

    override fun versionInfo(): VersionInfo {
        return VersionInfo(
            resolveVersionName(versionDetails()),
            versionDetails(),
        )
    }

    companion object : VersioningContract.VersioningFactory {
        private const val SEPARATOR = "-"
        private const val NON_RELEASE_SUFFIX = "SNAPSHOT"
        private const val RELEASE_CANDIDATE_SUFFIX = "${SEPARATOR}rc"

        override fun getInstance(
            project: Project,
            configuration: VersioningConfiguration,
        ): VersioningContract.Versioning {
            val versionDetails: Closure<VersionDetails> by project.extra

            return Versioning(versionDetails, configuration)
        }
    }
}
