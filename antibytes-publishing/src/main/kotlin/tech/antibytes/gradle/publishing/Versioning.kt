/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import tech.antibytes.gradle.publishing.PublishingContract.Versioning.Companion.NON_RELEASE_SUFFIX
import tech.antibytes.gradle.publishing.PublishingContract.Versioning.Companion.SEPARATOR
import tech.antibytes.gradle.publishing.api.VersionInfo

internal object Versioning : PublishingContract.Versioning {
    lateinit var configuration: PublishingApiContract.VersioningConfiguration

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

    private fun renderReleaseBranch(details: VersionDetails): String {
        return if (!details.isCleanTag || details.commitDistance > 0) {
            cleanVersionName(
                details.version,
                details.commitDistance
            ) + SEPARATOR + NON_RELEASE_SUFFIX
        } else {
            removeVersionPrefix(details.version)
        }
    }

    private fun normalize(name: String): String {
        var normalized = name

        configuration.normalization.forEach { dangled ->
            normalized = normalized.replace(dangled, SEPARATOR)
        }

        return normalized
    }

    private fun extractBranchName(
        pattern: Regex,
        name: String
    ): String = pattern.matchEntire(name)!!.groups[1]!!.value

    private fun renderDependencyBotBranch(details: VersionDetails): String {
        val infix = normalize(
            extractBranchName(
                configuration.dependencyBotPattern,
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

    private fun renderFeatureBranch(details: VersionDetails, useGitHash: Boolean): String {
        var infix = extractBranchName(
            configuration.featurePattern,
            details.branchName
        )

        infix = if (configuration.issuePattern is Regex && configuration.issuePattern!!.matches(infix)) {
            extractBranchName(
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

    private fun resolveVersionName(details: VersionDetails): String {
        return when {
            details.branchName.matches(configuration.releasePattern) -> renderReleaseBranch(details)
            details.branchName.matches(configuration.dependencyBotPattern) -> renderDependencyBotBranch(details)
            details.branchName.matches(configuration.featurePattern) -> renderFeatureBranch(details, configuration.useGitHashFeatureSuffix)
            else -> throw PublishingError.VersioningError(
                "Ill named branch name (${details.branchName})! Please adjust it to match the project settings."
            )
        }
    }

    override fun versionName(
        project: Project,
        configuration: PublishingApiContract.VersioningConfiguration
    ): String {
        val versionDetails: Closure<VersionDetails> by project.extra
        this.configuration = configuration

        return resolveVersionName(versionDetails())
    }

    override fun versionInfo(
        project: Project,
        configuration: PublishingApiContract.VersioningConfiguration
    ): VersionInfo {
        val versionDetails: Closure<VersionDetails> by project.extra
        this.configuration = configuration

        return VersionInfo(
            resolveVersionName(versionDetails()),
            versionDetails()
        )
    }
}
