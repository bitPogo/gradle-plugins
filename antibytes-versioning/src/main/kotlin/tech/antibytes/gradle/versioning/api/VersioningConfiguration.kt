/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning.api

import tech.antibytes.gradle.versioning.VersioningContract

data class VersioningConfiguration(
    override val releasePrefixes: List<String> = listOf("main", "release"),
    override val featurePrefixes: List<String> = listOf("feature"),
    override val dependencyBotPrefixes: List<String> = listOf("dependabot"),
    override val issuePattern: Regex? = null,
    override val useGitHashFeatureSuffix: Boolean = false,
    override val useGitHashSnapshotSuffix: Boolean = false,
    override val versionPrefix: String = "v",
    override val normalization: Set<String> = emptySet(),
    override val suppressSnapshot: Boolean = false,
) : VersioningContract.VersioningConfiguration
