/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

internal object DependencyUpdate : DependencyContract.Update {
    private data class StabilityIndicator(
        val current: Boolean = true,
        val candidate: Boolean = true
    )

    private fun resolveStabilityIndicator(
        configuration: DependencyContract.Extension,
        current: String,
        candidate: String
    ): StabilityIndicator {
        var stability = StabilityIndicator()
        configuration.keywords.get().forEach { keyword ->
            stability = stability.copy(
                current = current.contains(keyword),
                candidate = candidate.contains(keyword)
            )
        }

        return stability
    }

    private fun isStable(
        version: String,
        regex: Regex,
        hasStableIndicator: Boolean
    ): Boolean {
        return regex.matches(version) || hasStableIndicator
    }

    private fun isApplicable(
        configuration: DependencyContract.Extension,
        current: String,
        candidate: String
    ): Boolean {
        val indicator = resolveStabilityIndicator(
            configuration = configuration,
            current = current.toUpperCase(),
            candidate = candidate.toUpperCase()
        )

        println(indicator)

        val versionRegex = configuration.versionRegex.get()

        return !isStable(current, versionRegex, indicator.current) &&
            isStable(candidate, versionRegex, indicator.candidate)
    }

    override fun configure(
        project: Project,
        configuration: DependencyContract.Extension
    ) {
        project.tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
            resolutionStrategy {
                componentSelection {
                    all {
                        if (!isApplicable(configuration, currentVersion, candidate.version)) {
                            reject("Release candidate")
                        }
                    }
                }
            }
        }
    }
}
