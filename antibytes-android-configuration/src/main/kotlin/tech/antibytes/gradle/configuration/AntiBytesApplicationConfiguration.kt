/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.configuration.android.AndroidApplicationConfigurator
import tech.antibytes.gradle.configuration.android.DefaultAndroidApplicationConfigurationProvider
import tech.antibytes.gradle.util.applyIfNotExists

class AntiBytesApplicationConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        target.applyIfNotExists("com.android.application")

        AndroidApplicationConfigurator.configure(
            target,
            DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(target),
        )
    }
}
