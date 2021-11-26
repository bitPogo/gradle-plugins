/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.api

import tech.antibytes.gradle.configuration.ConfigurationApiContract

data class AndroidLibraryConfiguration(
    override var minSdkVersion: Int,
    override var targetSdkVersion: Int,
    override var compatibilityTargets: ConfigurationApiContract.Compatibility,
    override var fallbacks: Map<String, Set<String>>,
    override var mainSource: ConfigurationApiContract.MainSource,
    override var unitTestSource: ConfigurationApiContract.TestSource,
    override var publishVariants: Set<String>,
    override var androidTest: ConfigurationApiContract.TestSource?,
    override var testRunner: ConfigurationApiContract.TestRunner
) : ConfigurationApiContract.AndroidLibraryConfiguration
