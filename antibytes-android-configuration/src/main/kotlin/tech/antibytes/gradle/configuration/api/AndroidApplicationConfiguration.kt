/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.api

import tech.antibytes.gradle.configuration.AndroidConfigurationApiContract

internal data class AndroidApplicationConfiguration(
    override val compileSdkVersion: Int,
    override val minSdkVersion: Int,
    override val targetSdkVersion: Int,
    override val compatibilityTargets: AndroidConfigurationApiContract.Compatibility,
    override val fallbacks: Map<String, Set<String>>,
    override val mainSource: AndroidConfigurationApiContract.MainSource,
    override val unitTestSource: AndroidConfigurationApiContract.TestSource,
    override val testRunner: AndroidConfigurationApiContract.TestRunner,
    override val androidTest: AndroidConfigurationApiContract.TestSource,
) : AndroidConfigurationApiContract.AndroidApplicationConfiguration
