/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
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

internal class Versioning(
    project: Project,
    private val configuration: PublishingContract.VersioningConfiguration
) : PublishingContract.Versioning {
    private val versionDetails: Closure<VersionDetails> by project.extra

    private fun removeVersionPrefix(version: String): String {
        return version.substringAfter(configuration.versionPrefix.get())
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

        configuration.normalization.get().forEach { dangled ->
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
                configuration.dependencyBotPattern.get(),
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

    private fun renderFeatureBranch(details: VersionDetails): String {
        var infix = extractBranchName(
            configuration.featurePattern.get(),
            details.branchName
        )

        infix = if (configuration.issuePattern.isPresent && configuration.issuePattern.get().matches(infix)) {
            extractBranchName(
                configuration.issuePattern.get(),
                infix
            )
        } else {
            infix
        }

        infix = normalize(infix)

        val versionName = cleanVersionName(
            details.version,
            details.commitDistance
        )

        // Schema:
        // version '-' infix '-' 'SNAPSHOT'
        return "$versionName$SEPARATOR$infix$SEPARATOR$NON_RELEASE_SUFFIX"
    }

    override fun versionName(): String {
        val details = versionDetails()

        return when {
            details.branchName.matches(configuration.releasePattern.get()) -> renderReleaseBranch(details)
            details.branchName.matches(configuration.dependencyBotPattern.get()) -> renderDependencyBotBranch(details)
            details.branchName.matches(configuration.featurePattern.get()) -> renderFeatureBranch(details)
            else -> throw PublishingError.VersioningError(
                "Ill named branch name (${details.branchName})! Please adjust it to match the project settings."
            )
        }
    }

    override fun versionInfo(): PublishingApiContract.VersionInfo {
        return PublishingApiContract.VersionInfo(
            versionName(),
            versionDetails()
        )
    }
}
