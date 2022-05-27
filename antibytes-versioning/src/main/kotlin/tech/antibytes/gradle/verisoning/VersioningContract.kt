/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.verisoning

import org.gradle.api.Project
import tech.antibytes.gradle.verisoning.api.VersionInfo

interface VersioningContract {
    interface VersioningConfiguration {
        val releasePrefixes: List<String>
        val featurePrefixes: List<String>
        val dependencyBotPrefixes: List<String>
        val issuePattern: Regex?
        val useGitHashFeatureSuffix: Boolean
        val useGitHashSnapshotSuffix: Boolean

        val versionPrefix: String
        val normalization: Set<String>
    }

    interface Versioning {
        fun versionName(): String
        fun versionInfo(): VersionInfo
    }

    interface VersioningFactory {
        fun getInstance(project: Project, configuration: VersioningConfiguration): Versioning
    }
}
