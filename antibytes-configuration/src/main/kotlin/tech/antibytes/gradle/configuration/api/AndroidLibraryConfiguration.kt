/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.api

import tech.antibytes.gradle.configuration.ConfigurationApiContract

internal data class AndroidLibraryConfiguration(
    override val compileSdkVersion: Int,
    override val minSdkVersion: Int,
    override val targetSdkVersion: Int,
    override val prefix: String,
    override val compatibilityTargets: ConfigurationApiContract.Compatibility,
    override val fallbacks: Map<String, Set<String>>,
    override val mainSource: ConfigurationApiContract.MainSource,
    override val unitTestSource: ConfigurationApiContract.TestSource,
    override val publishVariants: Set<String>,
    override val androidTest: ConfigurationApiContract.TestSource?,
    override val testRunner: ConfigurationApiContract.TestRunner
) : ConfigurationApiContract.AndroidLibraryConfiguration
