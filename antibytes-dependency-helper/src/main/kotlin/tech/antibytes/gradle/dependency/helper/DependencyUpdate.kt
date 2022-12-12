/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

internal object DependencyUpdate : DependencyContract.Update {
    private data class StabilityIndicator(
        val current: Boolean = false,
        val candidate: Boolean = false,
    )

    private fun resolveStabilityIndicator(
        configuration: DependencyContract.DependencyPluginExtension,
        current: String,
        candidate: String,
    ): StabilityIndicator {
        var stability = StabilityIndicator()
        configuration.keywords.get().forEach { keyword ->
            stability = stability.copy(
                current = current.contains(keyword, false) || stability.current,
                candidate = candidate.contains(keyword, false) || stability.candidate,
            )
        }

        return stability
    }

    private fun isStable(
        version: String,
        regex: Regex,
        hasStableIndicator: Boolean,
    ): Boolean {
        return regex.matches(version) || hasStableIndicator
    }

    private fun isRejectable(
        configuration: DependencyContract.DependencyPluginExtension,
        current: String,
        candidate: String,
    ): Boolean {
        val indicator = resolveStabilityIndicator(
            configuration = configuration,
            current = current.toUpperCase(),
            candidate = candidate.toUpperCase(),
        )

        val versionRegex = configuration.versionRegex.get()

        return isStable(current, versionRegex, indicator.current) &&
            !isStable(candidate, versionRegex, indicator.candidate)
    }

    override fun configure(
        project: Project,
        configuration: DependencyContract.DependencyPluginExtension,
    ) {
        project.tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
            resolutionStrategy {
                componentSelection {
                    all {
                        if (isRejectable(configuration, currentVersion, candidate.version)) {
                            reject("Release candidate")
                        }
                    }
                }
            }
        }
    }
}
