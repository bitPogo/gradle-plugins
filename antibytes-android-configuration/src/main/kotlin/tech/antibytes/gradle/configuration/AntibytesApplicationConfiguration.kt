/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.configuration.android.AndroidApplicationConfigurator
import tech.antibytes.gradle.configuration.android.CompileTaskConfigurator
import tech.antibytes.gradle.configuration.android.DefaultAndroidApplicationConfigurationProvider
import tech.antibytes.gradle.util.applyIfNotExists

class AntibytesApplicationConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        target.applyIfNotExists("com.android.application")

        AndroidApplicationConfigurator.configure(
            target,
            DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(target),
        )

        CompileTaskConfigurator.configure(target, Unit)
    }
}
